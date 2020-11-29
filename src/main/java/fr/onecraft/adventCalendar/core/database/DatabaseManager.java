package fr.onecraft.adventCalendar.core.database;

import fr.lfremaux.queryBuilder.exceptions.DatabaseConfigurationException;
import fr.onecraft.adventCalendar.core.database.helpers.DatabaseConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseAccess dataBase;

    public static Connection getConnection() throws SQLException {
        return dataBase.getConnection();
    }

    public static void initDataBaseConnection(JavaPlugin plugin) throws DatabaseConfigurationException {
        dataBase = new DatabaseAccess(DatabaseConfig.getCredentials(plugin));
        dataBase.initPool(plugin);
    }

    public static void closeDataBaseConnection() {
        if (dataBase != null) dataBase.closePool();
    }
}