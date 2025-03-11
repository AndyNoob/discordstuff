package me.comfortable_andy.discordstuff.util;

import me.comfortable_andy.discordstuff.DiscordStuffMain;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

public class ReflectionUtil {

    public static final Method ADD_COMPLETION;
    public static final Method REMOVE_COMPLETION;

    static {
        Method addCompletion;
        Method removeCompletion;
        try {
            addCompletion = Player.class.getDeclaredMethod("addCustomChatCompletions", Collection.class);
            removeCompletion = Player.class.getDeclaredMethod("removeCustomChatCompletions", Collection.class);
            DiscordStuffMain.getInstance().getLogger().info("@-ping and emoji tab completion is enabled.");
        } catch (NoSuchMethodException e) {
            addCompletion = null;
            removeCompletion = null;
        }
        ADD_COMPLETION = addCompletion;
        REMOVE_COMPLETION = removeCompletion;
    }

    @SuppressWarnings({"unchecked", "deprecation"})
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
