package me.darkboy.profile.infinityprofile.commands;

import me.darkboy.profile.infinityprofile.api.register.command.InfinityCommand;
import me.darkboy.profile.infinityprofile.guis.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCMD extends InfinityCommand {

    public ProfileCMD() {
        this.setAliases("profile");
        this.setDescription("Profile Command");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Profile.inv.open(player);
    }
}
