package me.darkboy.profile.infinityprofile.data;

import lombok.Getter;
import lombok.Setter;
import me.darkboy.profile.infinityprofile.InfinityProfile;
import me.darkboy.profile.infinityprofile.api.resource.TemporaryConfig;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerProfile {

    private Player player;
    private TemporaryConfig playerData;

    private String name;
    private String uuid;

    public PlayerProfile(Player player) {
        this.player = player;
        this.playerData = new TemporaryConfig("plugins/InfinityProfile", player.getName() + ".yml", InfinityProfile.getInstance());

        setupConfig();

        this.name = playerData.getConfig().getString("Name");
        this.uuid = playerData.getConfig().getString("UUID");
    }

    private void setupConfig() {
        playerData.getConfig().set("Name", player.getName());
        playerData.getConfig().set("UUID", player.getUniqueId().toString());
    }
}
