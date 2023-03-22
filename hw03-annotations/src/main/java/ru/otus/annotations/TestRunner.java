package ru.otus.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Запускатор тестов
 */
public final class TestRunner {
    private static final String SUCCESS = "SUCCESS";
    private TestRunner() {}

    /**
     * выполнить запуск списка тестов
     *
     * @param tests список тестов
     */
    public static void run(List<Class<?>> tests) {
        List<TestResult> results = scanClasses(tests).entrySet()
                .stream()
                .parallel()
                .map(e -> runTest(e.getKey(), e.getValue()))
                .toList();

        System.out.printf("Всего тестов: %d. Успешно: %d\n", results.size(), results.stream().filter(r -> SUCCESS.equals(r.result)).count());
        System.out.println("-".repeat(100));
        results.forEach(t -> System.out.printf("%-50s|%-50s\n", t.name, Optional.ofNullable(t.result).orElse("")));
        System.out.println("-".repeat(100));
    }

    /**
     * выполнить запуск одиночного теста
     *
     * @param test класс теста
     */
    public static void run(Class<?> test) {
        run(Collections.singletonList(test));
    }

    private static TestResult runTest(Object testObject, Map<Class<? extends Annotation>, Collection<Method>> testMethods) {
        Optional<Method> method = Optional.empty();
        try {
            for (var beforeMethod : testMethods.get(Before.class)) {
                beforeMethod.invoke(testObject);
            }
            for (var testMethod : testMethods.get(Test.class)) {
                method = Optional.of(testMethod);
                testMethod.invoke(testObject);
            }
        } catch (Exception e) {
            return new TestResult(testObject.getClass().getName() + method.map(m -> ":" + m.getName()).orElse(""), e.getCause().getMessage());
        } finally {
            try {
                for (var afterMethod : testMethods.get(After.class)) {
                    afterMethod.invoke(testObject);
                }
            } catch (Exception e) {
                new TestResult(testObject.getClass().getName() + method.map(m -> ":" + m.getName()).orElse(""), e.getCause().getMessage());
            }
        }
        return new TestResult(testObject.getClass().getName() + method.map(m -> ":" + m.getName()).orElse(""), SUCCESS);
    }

    private static BiFunction<Class<?>, List<Method>, List<Method>> createOrUpdateFunc(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new RuntimeException("Метод " + method.getName() + " должен быть публичным");
        }
        return (k, v) -> {
            if (v == null) {
                var result = new ArrayList<Method>();
                result.add(method);
                return result;
            }
            v.add(method);
            return v;
        };
    }

    private static Map<?, Map<Class<? extends Annotation>, Collection<Method>>> scanClasses(List<Class<?>> tests) {
        Map<Class<?>, List<Method>> beforeMethods = new HashMap<>();
        Map<Class<?>, List<Method>> afterMethods = new HashMap<>();
        Map<Class<?>, List<Method>> testMethods = new HashMap<>();

        for (var test : tests) {
            for (var method : test.getDeclaredMethods()) {
                boolean annotationExists = false;
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.compute(test, createOrUpdateFunc(method));
                    annotationExists = true;
                }
                if (method.isAnnotationPresent(After.class)) {
                    afterMethods.compute(test, createOrUpdateFunc(method));
                    annotationExists = true;
                }
                if (method.isAnnotationPresent(Test.class)) {
                    testMethods.compute(test, createOrUpdateFunc(method));
                    if (annotationExists)
                        throw new RuntimeException("У тестируемого метода не должно присутствовать аннотаций @After или @Before");
                }
            }
        }

        return testMethods.values()
                .stream()
                .flatMap(Collection::stream)
                .map(m -> Map.entry(
                        newInstance(m.getDeclaringClass()),
                        Map.of(Test.class, Collections.singleton(m),
                               Before.class, Optional.ofNullable(beforeMethods.get(m.getDeclaringClass())).orElse(Collections.emptyList()),
                               After.class, Optional.ofNullable(afterMethods.get(m.getDeclaringClass())).orElse(Collections.emptyList()))
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Object newInstance(Class<?> clazz) {
        try {
           return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("Не удалось создать объект класса {0}, необходим конструктор по-умолчанию", clazz.getCanonicalName()));
        }
    }

    private record TestResult(String name, String result) {}
}
