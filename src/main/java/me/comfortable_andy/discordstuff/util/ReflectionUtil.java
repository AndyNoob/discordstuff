package me.comfortable_andy.discordstuff.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static <V> V getField(Object obj, String name) throws ReflectiveOperationException {
        Field field = obj.getClass().getDeclaredField(name);
        boolean accessible = field.isAccessible();
        Object toReturn;

        field.setAccessible(true);
        toReturn = field.get(obj);
        field.setAccessible(accessible);

        return (V) toReturn;
    }
}
