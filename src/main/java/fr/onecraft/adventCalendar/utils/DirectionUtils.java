package fr.onecraft.adventCalendar.utils;

import org.bukkit.block.BlockFace;

import java.security.InvalidParameterException;

public class DirectionUtils {

    public static BlockFace getLeftBlock(BlockFace relative) {
        switch (relative) {
            case NORTH:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.SOUTH;
            default:
                throw new InvalidParameterException("Invalid blockface");
        }
    }

    public static BlockFace getRightBlock(BlockFace relative) {
        return getLeftBlock(relative).getOppositeFace();
    }
}
