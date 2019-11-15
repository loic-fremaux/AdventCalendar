package fr.onecraft.adventCalendar.core.objects;

import fr.onecraft.adventCalendar.AdventCalendar;
import fr.onecraft.adventCalendar.core.helpers.Database;
import org.bukkit.Bukkit;

import java.util.*;

public class User {
    private int id;
    private UUID uuid;
    private Set<Integer> opened;

    private static final Map<UUID, User> USERS = new HashMap<>();

    public User(int id, UUID uuid) {
        this.id = id;
        this.uuid = uuid;
        this.opened = new HashSet<>();
    }

    public User(int id, UUID uuid, Set<Integer> opened) {
        this.id = id;
        this.uuid = uuid;
        this.opened = opened;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Set<Integer> getOpened() {
        return opened;
    }

    public void open(int day) {
        opened.add(day);
        Bukkit.getScheduler().runTaskAsynchronously(AdventCalendar.INSTANCE, () -> Database.openDay(id, day));
    }

    public boolean hasOpened(int day) {
        return opened.contains(day);
    }

    public static User get(UUID uuid) {
        return USERS.get(uuid);
    }

    public static void loadUser(UUID uuid) {
        USERS.put(uuid, Database.getUser(uuid));
    }

    public static void removeUserCache(UUID uuid) {
        USERS.remove(uuid);
    }
}
