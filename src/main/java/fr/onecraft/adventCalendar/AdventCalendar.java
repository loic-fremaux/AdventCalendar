package fr.onecraft.adventCalendar;

import fr.lfremaux.queryBuilder.exceptions.DatabaseConfigurationException;
import fr.onecraft.adventCalendar.core.RegisterManager;
import fr.onecraft.adventCalendar.core.database.DatabaseManager;
import fr.onecraft.adventCalendar.core.database.helpers.DatabaseConfig;
import fr.onecraft.adventCalendar.core.objects.User;
import fr.onecraft.adventCalendar.core.objects.WallCalendar;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdventCalendar extends JavaPlugin {
    public static final String PREFIX = "§9Calendrier > §7";
    public static AdventCalendar INSTANCE;
    private Economy econ;

    @Override
    public void onEnable() {
        INSTANCE = this;

        // setup vault
        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // copy default database configuration
        DatabaseConfig.copyDefaultConfig(this);

        // connect to database
        try {
            DatabaseManager.initDataBaseConnection(this);
        } catch (DatabaseConfigurationException e) {
            error("Unable to connect to the database, plugin will shut down...");
            e.printStackTrace();
            this.onDisable();
        }

        // load candy textures, candy locations and users
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            WallCalendar.load();
            Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).forEach(User::loadUser);
        });

        // register events and commands
        new RegisterManager(this).register();

        info("Plugin has been enabled !");
    }

    @Override
    public void onDisable() {
        info("Plugin is disabling...");

        // disconnect database
        DatabaseManager.closeDataBaseConnection();

        info("Plugin has been disabled !");
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public void logToFile(String action, String message) {
        try {
            Date now = Calendar.getInstance().getTime();
            String today = new SimpleDateFormat("yyyy-MM-dd").format(now);
            String time = new SimpleDateFormat("HH:mm:ss").format(now);

            File file = new File(this.getDataFolder() + "/logs/", today + ".txt");
            if (!file.exists()) file.getParentFile().mkdirs();

            PrintWriter writer = new PrintWriter(new FileWriter(file, true));
            writer.println("[" + time + "][" + action + "] " + message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void info(String message) {
        getLogger().info("[" + this.getName() + "] " + message);
    }

    private void error(String message) {
        getLogger().severe("[" + this.getName() + "] " + message);
    }
}
