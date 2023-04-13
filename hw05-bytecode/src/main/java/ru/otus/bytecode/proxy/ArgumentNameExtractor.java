package ru.otus.bytecode.proxy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Хэлпер для извлечения имен аргументов, если нет параметра --parameters
 */
public final class ArgumentNameExtractor {
    private ArgumentNameExtractor(){}

    /**
     * Получить имена параметров метода
     *
     * @param method метод
     * @return имена параметров
     */
    public static String[] getParameterNames(Method method) {
        var declaringClass = method.getDeclaringClass();
        var declaringClassLoader = declaringClass.getClassLoader();

        var declaringType = Type.getType(declaringClass);
        var constructorDescriptor = Type.getMethodDescriptor(method);
        var url = declaringType.getInternalName() + ".class";

        try (var classFileInputStream = declaringClassLoader.getResourceAsStream(url)) {
            if (classFileInputStream == null) {
                throw new IllegalArgumentException("Не найден байткод класса " + declaringClass.getName());
            }

            var classNode = new ClassNode();
            var classReader = new ClassReader(classFileInputStream);
            classReader.accept(classNode, 0);

            var methods = classNode.methods;
            for (var methodNode : methods) {
                if (methodNode.name.equals(method.getName()) && methodNode.desc.equals(constructorDescriptor)) {
                    var argumentTypes = Type.getArgumentTypes(methodNode.desc);
                    var parameterNames = new String[method.getParameterCount()];
                    var localVariables = methodNode.localVariables;
                    for (int i = 1; i <= argumentTypes.length; i++) {
                        parameterNames[i - 1] = localVariables.get(i).name;
                    }

                    return parameterNames;
                }
            }
        } catch (IOException e) {
            return extractReflectionNames(method);
        }

        return extractReflectionNames(method);
    }

    private static String[] extractReflectionNames(Method method) {
        var result = new String[method.getParameterCount()];
        for (int i = 0; i < method.getParameterCount(); ++i) {
            result[i] = method.getParameters()[i].getName();
        }
        return result;
    }
}
