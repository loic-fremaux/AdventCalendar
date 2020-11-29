package fr.onecraft.adventCalendar.commands;

import fr.onecraft.adventCalendar.AdventCalendar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdCalendar implements CommandExecutor {
    private AdventCalendar plugin;

    public CmdCalendar(AdventCalendar plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        return false;
    }
}
