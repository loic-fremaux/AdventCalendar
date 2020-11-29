package fr.onecraft.adventCalendar.core.objects;

import fr.onecraft.adventCalendar.AdventCalendar;
import fr.onecraft.adventCalendar.core.helpers.Database;
import fr.onecraft.adventCalendar.core.helpers.Time;
import fr.onecraft.adventCalendar.utils.DirectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WallCalendar {
    public static final String L40 = "§8Trop tard... :c";
    public static final String L41 = "§8§lClique ici !";
    public static final String L42 = "§8Bientôt ! :D";
    private static final String L1 = "§c§lCALENDRIER";
    private static final String L2 = "§2%d";
    private static final String L3 = "§2%d Décembre";
    private static final long DAY_TIME = 86_400_000;
    private static Map<Integer, CalendarReward> REWARDS = new HashMap<>();
    private static WallCalendar INSTANCE;
    private final Sign[] signs;
    private int lastCheckDay;

    public WallCalendar(WallSign wallSign) {
        this.signs = new Sign[3];
        this.lastCheckDay = Time.getDayNumber(new Date());
        Bukkit.getScheduler().runTask(AdventCalendar.INSTANCE, () -> setup(wallSign));
        INSTANCE = this;

        Bukkit.getScheduler().runTaskTimer(AdventCalendar.INSTANCE, () -> {
            int currentDay = Time.getDayNumber(new Date());
            if (lastCheckDay != currentDay) {
                lastCheckDay = currentDay;

                setup(wallSign);
            }
        }, 20 * 60, 20 * 60);
    }

    public static CalendarReward getReward(int day) {
        return REWARDS.get(day);
    }

    public static boolean isCalendar(Location location) {
        return Arrays.stream(INSTANCE.signs).anyMatch(sign -> sign.getLocation().getBlockX() == location.getBlockX()
                && sign.getLocation().getBlockY() == location.getBlockY()
                && sign.getLocation().getBlockZ() == location.getBlockZ());
    }

    public static void load() {
        REWARDS = Database.getCalendar();
        new WallCalendar(Database.getMainSignLocation());
    }

    private void setup(WallSign wallSign) {
        Location location = wallSign.getLocation();

        BlockFace main = wallSign.getBlockFace().getOppositeFace();
        initSign(new WallSign(main, location.clone()), (byte) 1);

        BlockFace left = DirectionUtils.getLeftBlock(wallSign.getBlockFace());
        initSign(new WallSign(main, location.clone().add(
                left.getModX(),
                left.getModY(),
                left.getModZ()
        )), (byte) 0);

        BlockFace right = DirectionUtils.getRightBlock(wallSign.getBlockFace());
        initSign(new WallSign(main, location.clone().add(
                right.getModX(),
                right.getModY(),
                right.getModZ()
        )), (byte) 2);
    }

    private void initSign(WallSign wallSign, byte index) {
        Block block = wallSign.getLocation().getBlock();
        Sign sign;
        if (!(block.getState() instanceof Sign)) {
            block.setType(Material.WALL_SIGN);
        }
        sign = (Sign) block.getState();

        org.bukkit.material.Sign signData = new org.bukkit.material.Sign(Material.WALL_SIGN);
        signData.setFacingDirection(wallSign.getBlockFace());
        sign.setData(signData);

        long relative = index == 1 ? 0 : index == 0 ? -DAY_TIME : DAY_TIME;
        Date date = new Date(System.currentTimeMillis() + relative);

        sign.setLine(0, L1);
        sign.setLine(1, L2.replace("%d", Time.getDay(date)));
        sign.setLine(2, L3.replace("%d", Time.getDayNumber(date) + ""));
        sign.setLine(3, index == 1 ? L41 : index == 0 ? L40 : L42);

        sign.update();

        this.signs[index] = sign;
    }
}
