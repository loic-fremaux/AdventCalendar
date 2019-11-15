package fr.onecraft.adventCalendar.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class InventoryUtils {

    public static int getFreeSlots(Player user) {
        ItemStack[] contents = user.getInventory().getContents();
        return 36 - (int) Arrays.stream(contents)
                .filter(content -> content != null && content.getType() != null && content.getType() != Material.AIR)
                .count();
    }
}
