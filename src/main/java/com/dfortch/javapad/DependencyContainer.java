package com.dfortch.javapad;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DependencyContainer {

    private static final ConcurrentMap<Class<?>, Object> instances = new ConcurrentHashMap<>();

    private DependencyContainer() {
    }

    public static <T> T get(Class<T> clazz) {
        T instance = (T) instances.get(clazz);
        if (instance == null) {
            throw new IllegalArgumentException("No instance found for class " + clazz.getName());
        }
        return instance;
    }

    public static synchronized <T> void register(Class<T> clazz, T instance) {
        if (instances.putIfAbsent(clazz, instance) != null) {
            throw new IllegalArgumentException("Instance already registered for class " + clazz.getName());
        }
    }
}
