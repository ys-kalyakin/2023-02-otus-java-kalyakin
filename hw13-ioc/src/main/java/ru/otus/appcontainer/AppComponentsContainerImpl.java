package ru.otus.appcontainer;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>...initialConfigClass) {
        for (var config : initialConfigClass)
            checkConfigClass(config);

        Arrays.stream(initialConfigClass)
                .sorted(Comparator.comparing(config -> config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .map(this::processConfig)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(e -> e.getValue().getAnnotation(AppComponent.class).order()))
                .forEach(entry -> processComponent(entry.getKey(), entry.getValue()));
    }

    public AppComponentsContainerImpl(String pack) {
        this(getConfigurations(pack));
    }

    private List<Map.Entry<Object, Method>> processConfig(Class<?> configClass) {
        try {
            Object config = configClass.getDeclaredConstructor().newInstance();
            return Arrays.stream(configClass.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(AppComponent.class))
                    .map(m -> Map.entry(config, m))
                    .toList();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            System.err.println("Ошибка обработки конфигурации: " + e);
            return Collections.emptyList();
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        var result = appComponents.stream()
                .filter(componentClass::isInstance)
                .toList();

        if (result.size() != 1)
            throw new RuntimeException("Компонент отсутствует");
        return (C) result.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var component = appComponentsByName.get(componentName);
        if (component == null)
            throw new RuntimeException("Компонент отсутствует");
        return (C) component;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @SneakyThrows
    private void processComponent(Object config, Method method) {
        var annotationName = method.getAnnotation(AppComponent.class).name();
        var componentName = Objects.equals(annotationName, "") || annotationName == null ? method.getName() : annotationName;
        if (method.getParameters().length == 0) {
            addComponent(componentName, method.invoke(config));
        } else {
            Object[] arguments = new Object[method.getParameters().length];
            int i = 0;
            for (var parameter : method.getParameters()) {
                arguments[i++] = getAppComponent(parameter.getType());
            }
            addComponent(componentName, method.invoke(config, arguments));
        }
    }


    private void addComponent(String name, Object component) {
        if (appComponentsByName.containsKey(name))
            throw new RuntimeException("Не должно быть компонентов с одинаковым именем");
        appComponentsByName.put(name, component);
        appComponents.add(component);
    }

    private static Class<?>[] getConfigurations(String pack) {
        var reflections = new Reflections(pack, Scanners.TypesAnnotated);
        var configs = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        return configs.toArray(new Class<?>[0]);
    }
}
