package fr.onecraft.adventCalendar.core.objects;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class WallSign {
    private BlockFace blockFace;
    private Location location;

    public WallSign(BlockFace face, Location location) {
        this.blockFace = face;
        this.location = location;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public Location getLocation() {
        return location;
    }
}
