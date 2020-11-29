package fr.onecraft.adventCalendar.listeners;

import fr.onecraft.adventCalendar.AdventCalendar;
import fr.onecraft.adventCalendar.core.helpers.Time;
import fr.onecraft.adventCalendar.core.objects.CalendarReward;
import fr.onecraft.adventCalendar.core.objects.User;
import fr.onecraft.adventCalendar.core.objects.WallCalendar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

public class PlayerListeners implements Listener {
    private final AdventCalendar plugin;

    public PlayerListeners(AdventCalendar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!event.getClickedBlock().getType().equals(Material.WALL_SIGN)) return;
        if (!WallCalendar.isCalendar(event.getClickedBlock().getLocation())) return;

        Player player = event.getPlayer();
        Sign sign = (Sign) event.getClickedBlock().getState();
        if (sign.getLine(3).equals(WallCalendar.L40)) {
            player.sendMessage(AdventCalendar.PREFIX + "Trop tard... ce jour est déjà passé !");
            return;
        } else if (sign.getLine(3).equals(WallCalendar.L42)) {
            player.sendMessage(AdventCalendar.PREFIX + "Patience... n'ouvre pas ton calendrier trop tôt !");
            return;
        }

        User user = User.get(player.getUniqueId());
        int day = Time.getDayNumber(new Date());
        if (user.hasOpened(day)) {
            player.sendMessage(AdventCalendar.PREFIX + "Tu as déjà ouvert ton calendrier aujourd'hui... reviens demain !");
            return;
        }

        CalendarReward reward = WallCalendar.getReward(day);
        if (reward.giveTo(player)) {
            user.open(day);
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> User.loadUser(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        User.removeUserCache(event.getPlayer().getUniqueId());
    }
}
