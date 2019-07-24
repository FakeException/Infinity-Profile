package me.darkboy.profile.infinityprofile.guis;

import me.darkboy.profile.infinityprofile.api.inventory.ClickableItem;
import me.darkboy.profile.infinityprofile.api.inventory.InfinityInventory;
import me.darkboy.profile.infinityprofile.api.inventory.content.InventoryContents;
import me.darkboy.profile.infinityprofile.api.inventory.content.InventoryProvider;
import me.darkboy.profile.infinityprofile.api.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Settings implements InventoryProvider {

    private final Random random = new Random();

    public static InfinityInventory inv = InfinityInventory.builder()
            .id("settingsGUI")
            .provider(new Settings())
            .size(6, 9)
            .title("          §bSettings")
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName("§7✦ §b§lDream§c§lWars §7✦").build()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if(state % 5 != 0)
            return;

        short durability = (short) random.nextInt(15);

        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, durability).setDisplayName("§7✦ §b§lDream§c§lWars §7✦").build();
        contents.fillBorders(ClickableItem.empty(glass));
    }
}
