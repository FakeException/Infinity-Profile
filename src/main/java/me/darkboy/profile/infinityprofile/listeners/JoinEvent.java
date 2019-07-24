package me.darkboy.profile.infinityprofile.listeners;

import me.darkboy.profile.infinityprofile.mysql.MySQLDatabase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent extends MySQLDatabase implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        checkUser(player);
    }
}
