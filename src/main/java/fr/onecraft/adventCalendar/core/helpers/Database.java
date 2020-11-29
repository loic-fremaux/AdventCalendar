package fr.onecraft.adventCalendar.core.helpers;

import fr.lfremaux.queryBuilder.exceptions.DatabaseConnectionException;
import fr.lfremaux.queryBuilder.objects.Query;
import fr.lfremaux.queryBuilder.objects.result.QueryResultSet;
import fr.onecraft.adventCalendar.core.database.DatabaseManager;
import fr.onecraft.adventCalendar.core.objects.CalendarReward;
import fr.onecraft.adventCalendar.core.objects.User;
import fr.onecraft.adventCalendar.core.objects.WallSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Database {
    private static final String PREFIX = "xmas_";
    private static final String USERS = PREFIX + "users";
    private static final String OPENED = PREFIX + "opened";
    private static final String REWARDS = PREFIX + "rewards";
    private static final String SIGNS = PREFIX + "signs";

    public static User getUser(UUID uuid) {
        try {
            QueryResultSet result = new Query(DatabaseManager.getConnection())
                    .from(USERS)
                    .select()
                    .where("uuid", uuid.toString())
                    .execute();

            // create user if no result found
            if (!result.next()) {
                String name = "?";
                OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                if (target.isOnline() || !target.getName().isEmpty()) {
                    name = target.getName();
                }

                int id = new Query(DatabaseManager.getConnection())
                        .from(USERS)
                        .insert("name", name)
                        .insert("uuid", uuid.toString())
                        .getRow();

                return new User(id, uuid, new HashSet<>());
            }

            QueryResultSet openedResult = new Query(DatabaseManager.getConnection())
                    .from(OPENED)
                    .select()
                    .where("user_id", result.getInt("id"))
                    .execute();

            Set<Integer> opened = new HashSet<>();
            while (openedResult.next()) {
                opened.add(openedResult.getInt("day"));
            }

            return new User(result.getInt("id"), uuid, opened);
        } catch (DatabaseConnectionException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<Integer, CalendarReward> getCalendar() {
        try {
            QueryResultSet result = new Query(DatabaseManager.getConnection())
                    .from(REWARDS)
                    .select()
                    .where("server", Bukkit.getServerName())
                    .execute();

            Map<Integer, CalendarReward> rewards = new HashMap<>();
            while (result.next()) {
                rewards.put(
                        result.getInt("day"),
                        new CalendarReward(
                                result.getInt("id"),
                                result.getString("reward")
                        )
                );
            }

            return rewards;
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static WallSign getMainSignLocation() {
        try {
            QueryResultSet result = new Query(DatabaseManager.getConnection())
                    .from(SIGNS)
                    .select()
                    .where("server", Bukkit.getServerName())
                    .execute();

            if (result.next()) {
                return new WallSign(
                        BlockFace.valueOf(result.getString("face")),
                        new Location(
                                Bukkit.getWorld(result.getString("world")),
                                result.getInt("x"),
                                result.getInt("y"),
                                result.getInt("z")
                        )
                );
            }
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void openDay(int userId, int day) {
        try {
            new Query(DatabaseManager.getConnection())
                    .from(OPENED)
                    .insert("user_id", userId)
                    .insert("day", day)
                    .insert("server", Bukkit.getServerName())
                    .execute();
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
        }
    }
}
