package me.darkboy.profile.infinityprofile.guis;

import me.darkboy.profile.infinityprofile.MySQL.DataManager;
import me.darkboy.profile.infinityprofile.api.inventory.ClickableItem;
import me.darkboy.profile.infinityprofile.api.inventory.InfinityInventory;
import me.darkboy.profile.infinityprofile.api.inventory.content.InventoryContents;
import me.darkboy.profile.infinityprofile.api.inventory.content.InventoryProvider;
import me.darkboy.profile.infinityprofile.api.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Profile extends DataManager implements InventoryProvider {

    public static final InfinityInventory inv = InfinityInventory.builder()
            .id("profileGUI")
            .size(6, 9)
            .provider(new Profile())
            .title("§bYour player §3Profile")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName("§7✦ §b§lDream§c§lWars §7✦").build()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        long t = (long)((double)player.getStatistic(Statistic.PLAY_ONE_TICK) * 0.05D * 1000.0D);
        long[] time = this.formatDuration(t);

        String message = this.formatMessage(time, player);
        contents.set(2, 2, ClickableItem.empty(new ItemBuilder(Material.SKULL_ITEM).setLore("", "§6Time in Game §e➦ " + message, "§6Rank §e➦ " + getRank(player)).setDisplayName("§3" + player.getName()).setSkullOwner(player.getUniqueId()).build()));
        contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.REDSTONE_COMPARATOR).setLore("").setDisplayName("§bUser preferencer").build(), e -> Settings.inv.open(player)));
    }

    private long[] formatDuration(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        return new long[]{days, hours, minutes};
    }

    private String formatMessage(long[] time, Player player) {
        return ChatColor.translateAlternateColorCodes('&', "&3%d%&bD &3%h%&bH &3%m%&bM".replace("%d%", time[0] + "").replace("%h%", time[1] + "").replace("%m%", time[2] + "").replace("%p%", player.getName()));
    }
}