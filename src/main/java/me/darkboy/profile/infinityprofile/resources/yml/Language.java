package me.darkboy.profile.infinityprofile.resources.yml;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.darkboy.profile.infinityprofile.api.InfinityPlugin;
import me.darkboy.profile.infinityprofile.api.resource.yml.ResourceYaml;
import me.darkboy.profile.infinityprofile.api.util.Messages;
import me.darkboy.profile.infinityprofile.resources.ResourceType;
import me.darkboy.profile.infinityprofile.resources.Resources;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Language {

    PREFIX("prefix", "&3Cheat&bDestroyer &7➜"),

    CMD_NO_PERM("command.no-permission", "{prefix} &7You don't have the permission for that command."),

    MESSAGE_NOTIFYMSG("messages.notify-message", "{prefix} &b%player%&7 is using &3%check%&7"),

    KILLAURA_AUTOKICK_MSG("killaura.auto-kick-msg", "&cPlease remove &4KillAura &cor you will get banned!"),

    CMD_RELOAD_COMPLETE("command.reload-complete-msg", "{prefix} &7Plugin successfully reloaded."),

    ;

    public static ResourceYaml getLocale() {
        return (ResourceYaml) Resources.get().getResource(ResourceType.LANGUAGE);
    }

    private final String path;
    private final List<String> def;

    /**
     * @param path the path pointing to the message.
     * @param def  default value in case message is missing or null.
     */
    Language(String path, String... def) {
        this.path = path;
        this.def = Messages.colour(def);
    }

    /**
     * @return yaml path pointing to the message.
     */
    public String getPath() {
        return path;
    }

    public static void saveFile() {
        InfinityPlugin.getResourceProvider().saveResource(Language.getLocale());
    }

    /**
     * @return default value in case message is missing or {@code null}.
     */
    public List<String> getDefault() {
        return def;
    }

    public static String getPrefix() {
        return Language.PREFIX.getMessage().get(0);
    }

    /**
     * @return list of messages or {@link Language#def} if missing.
     */
    public List<String> getMessage(Object... rep) {
        List<String> replacements = Stream.of(rep)
                .map(String::valueOf)
                .collect(Collectors.toList());

        List<String> unreplaced;

        if (getLocale().getConfiguration().isString(path)) {
            unreplaced = Lists.newArrayList(getLocale().getString(path, def.get(0)));
        } else {
            unreplaced = getLocale().getConfiguration().getStringList(path);
        }

        // Use the default if the returned is empty.
        if (unreplaced.isEmpty()) {
            unreplaced = def;
        }

        // Not replacing anything if the message is the prefix.
        if (this == PREFIX) {
            return unreplaced;
        }

        Map<String, String> replace = Maps.newHashMap();
        List<String> replaced = Lists.newArrayList();

        // Replacements list acting as a key-value map:
        // key1, value1, key2, value2, key3, value3, etc..
        List<String> temp = Lists.newArrayList(replacements);
        temp.add("{prefix}");
        temp.add(Language.PREFIX.getMessage().get(0)); // No need to check length.

        // Convert key-value list to map.
        for (int i = 0; i < temp.size() - 1; i += 2) {
            replace.put(temp.get(i), temp.get(i + 1));
        }

        // Finally, replace.
        for (String s : unreplaced) {
            for (Map.Entry<String, String> r : replace.entrySet()) {
                s = s.replace(r.getKey(), r.getValue());
            }

            replaced.add(s);
        }

        return Messages.colour(replaced);
    }
}