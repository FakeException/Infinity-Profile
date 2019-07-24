package me.darkboy.profile.infinityprofile.resources.yml;

import com.google.common.collect.Lists;
import me.darkboy.profile.infinityprofile.InfinityProfile;
import me.darkboy.profile.infinityprofile.api.resource.yml.ResourceYaml;
import me.darkboy.profile.infinityprofile.resources.ResourceType;
import me.darkboy.profile.infinityprofile.resources.Resources;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public enum Config {

    CONFIG_VERSION("config-version", 2),
    AUTO_UPDATE("enable-auto-update", true),

    CHECK_NOFALL("checks.NoFall.enabled", true),
    CHECK_NOFALL_NAME("checks.NoFall.check-name", "No Fall"),
    CHECK_SPEED_FLY("checks.Speed-Fly.enabled", true),
    CHECK_SPEED_FLY_NAME("checks.Speed-Fly.check-name", "Fly/Speed"),
    CHECK_STEP("checks.Step.enabled", true),
    CHECK_STEP_NAME("checks.Step.check-name", "Step"),
    CHECK_COMBAT_HACKS("checks.CombatHacks.enabled", true),
    CHECK_COMBAT_HACKS_NAME("checks.CombatHacks.check-name", "KillAura"),
    CHECK_FASTLADDER("checks.FastLadder.enabled", true),
    CHECK_FASTLADDER_NAME("checks.FastLadder.check-name", "Fast Ladder"),
    CHECK_IMPOSSIBLEMOVEMENTS("checks.ImpossibleMovements.enabled", true),
    CHECK_IMPOSSIBLEMOVEMENTS_NAME("checks.ImpossibleMovements.check-name", "Impossible Movements"),
    CHECK_INVALIDPITCH("checks.InvalidPitch.enabled", true),
    CHECK_INVALIDPITCH_NAME("checks.InvalidPitch.check-name", "Invalid Pitch"),
    CHECK_INVWALK("checks.InventoryWalk.enabled", true),
    CHECK_INVWALK_NAME("checks.InventoryWalk.check-name", "Inventory Walk"),
    CHECK_SLIMEJUMP("checks.SlimeJump.enabled", true),
    CHECK_SLIMEJUMP_NAME("checks.SlimeJump.check-name", "Slime Jump"),
    CHECK_NOWEB("checks.NoWeb.enabled", true),
    CHECK_NOWEB_NAME("checks.NoWeb.check-name", "No Web"),
    CHECK_GROUND_SPOOF("checks.GroundSpoof.enabled", true),
    CHECK_GROUND_SPOOF_NAME("checks.GroundSpoof.check-name", "Ground Spoof"),
    CHECK_TIMER("checks.Timer.enabled", true),
    CHECK_TIMER_NAME("checks.Timer.check-name", "Timer"),
    CHECK_INVALID_LOCATION("checks.InvalidLocation.enabled", true),
    CHECK_INVALID_LOCATION_NAME("checks.InvalidLocation.check-name", "Invalid Location"),
    CHECK_FASTBOW("checks.FastBow.enabled",true),
    CHECK_FASTBOW_NAME("checks.FastBow.check-name", "FastBow"),
    CHECK_NOSLOWDOWN("checks.NoSlowDown.enabled", true),
    CHECK_NOSLOWDOWN_NAME("checks.NoSlowDown.check-name", "NoSlowDown"),
    CHECK_CRITICALS("checks.Criticals.enabled", true),
    CHECK_CRITICALS_NAME("checks.Criticals.check-name", "Criticals"),
    CHECK_AUTOSNEAK("checks.AutoSneak.enabled", true),
    CHECK_AUTOSNEAK_NAME("checks.AutoSneak.check-name", "AutoSneak"),

    KILLAURA_AUTOKICK("Settings.KillAura-AutoKick-Beta", false),

    CHECKSET_REACH_MAX_DISTANCE("checksettings.Reach.max-distance", 4.4)

    ;

    public static ResourceYaml getConfig() {
        return (ResourceYaml) Resources.get().getResource(ResourceType.CONFIG);
    }

    private final String path;
    private final Object def;

    /**
     * @param path the path to the setting.
     * @param def  default value of the setting.
     */
    Config(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    /**
     * @return path of the setting in Config.yml.
     */
    public String getPath() {
        return path;
    }

    public static void saveFile() {
        InfinityProfile.getResourceProvider().saveResource(Config.getConfig());
    }

    /**
     * @return value of the path as a string, also replaces the {prefix} placeholder.
     */
    public String getAsString() {
        return getConfig().getString(path, String.valueOf(def));
    }

    /**
     * @return value of the path as a string list.
     */
    public List<String> getAsStringList() {
        List<String> list = Lists.newArrayList();

        if (getConfig().getConfiguration().isString(path)) {
            list.add(getAsString());
        } else {
            list.addAll(getConfig().getConfiguration().getStringList(path));
        }

        return list.stream()
                .map(s -> s.replace("{prefix}", Language.PREFIX.getMessage().get(0)))
                .collect(Collectors.toList());
    }

    /**
     * @return value of the path as an int.
     */
    public int getAsInt() {
        return getConfig().getInt(path, Integer.parseInt(getAsString()));
    }

    /**
     * @return default value as an integer.
     */
    public int getAsIntDefault() {
        return Integer.parseInt(String.valueOf(def));
    }

    /**
     * @return value of the path as a short.
     */
    public short getAsShort() {
        return getConfig().getShort(path, Short.parseShort(getAsString()));
    }

    /**
     * @return value of the path as a double.
     */
    public double getAsDouble() {
        return getConfig().getDouble(path, Double.parseDouble(getAsString()));
    }

    /**
     * @return value of the path as a boolean.
     */
    public boolean getAsBoolean() {
        return getConfig().getBoolean(path, Boolean.parseBoolean(getAsString()));
    }

    public void setValue(Object value) {
        getConfig().set(path, value);
    }

    /**
     * @return value of the path as a configuration section.
     */
    public ConfigurationSection getAsConfigurationSection() {
        return getConfig().getConfigurationSection(path);
    }
}