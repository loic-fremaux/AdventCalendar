package fr.onecraft.adventCalendar.core.objects;

import fr.onecraft.adventCalendar.AdventCalendar;
import fr.onecraft.adventCalendar.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CalendarReward {
    private int id;
    private int coins;
    private List<ItemStack> items;

    public CalendarReward(int id, String content) {
        this.id = id;
        this.items = new ArrayList<>();
        if (content.startsWith("Item: ")) {
            content = content.substring(6);
            for (String item : content.split(",")) {
                String[] args = item.split(":");
                this.items.add(new ItemStack(
                        Material.valueOf(args[0]),
                        args.length == 1 ? 1 : Integer.parseInt(args[1])
                ));
            }
        } else if (content.startsWith("Money: ")) {
            this.coins = Integer.parseInt(content.substring(7));
        } else {
            this.coins = -1;
        }
    }

    public boolean giveTo(Player player) {
        if (!this.items.isEmpty()) {
            if (InventoryUtils.getFreeSlots(player) < this.items.size()) {
                player.sendMessage(AdventCalendar.PREFIX + "Fais un peu de place dans ton inventaire avant d'ouvrir ton calendrier !");
                return false;
            }

            items.forEach(item -> player.getInventory().addItem(item.clone()));
            String quantity = this.items.size() == 1 ? "un" : "des";
            String plural = this.items.size() == 1 ? "" : "s";
            player.sendMessage(AdventCalendar.PREFIX + "Tu as reçu " + quantity + " item" + plural + " ! Regarde dans ton inventaire :D");
        } else {
            AdventCalendar.INSTANCE.getEconomy().depositPlayer(player, this.coins);
            player.sendMessage(AdventCalendar.PREFIX + "Tu as reçu §a" + this.coins + "§7 coins aujourd'hui ! J'espère te revoir demain :)");
        }

        return true;
    }
}
