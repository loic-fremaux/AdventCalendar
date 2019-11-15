package fr.onecraft.adventCalendar.core;

import fr.onecraft.adventCalendar.AdventCalendar;
import fr.onecraft.adventCalendar.commands.CmdCalendar;
import fr.onecraft.adventCalendar.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class RegisterManager {
    private AdventCalendar plugin;

    public RegisterManager(AdventCalendar plugin) {
        this.plugin = plugin;
    }

    public void register() {
        // listeners
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerListeners(plugin), plugin);

        // commands
        plugin.getCommand("calendar").setExecutor(new CmdCalendar(plugin));
    }
}
