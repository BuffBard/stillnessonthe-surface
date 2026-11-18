package com.hitsuji.world.generation;

import com.hitsuji.world.BoundaryType;
import com.hitsuji.world.VoidWorldBoundary;

public final class BoundaryDetector {

    private BoundaryDetector() {
    }

    public static BoundaryType getBoundaryType(int chunkX, int chunkZ) {

        if (VoidWorldBoundary.isInside(chunkX, chunkZ)) {
            return BoundaryType.NONE;
        }

        // Cardinal neighbors
        boolean north = VoidWorldBoundary.isInside(chunkX, chunkZ - 1);
        boolean south = VoidWorldBoundary.isInside(chunkX, chunkZ + 1);
        boolean east  = VoidWorldBoundary.isInside(chunkX + 1, chunkZ);
        boolean west  = VoidWorldBoundary.isInside(chunkX - 1, chunkZ);

        // Diagonal neighbors
        boolean northEast = VoidWorldBoundary.isInside(chunkX + 1, chunkZ - 1);
        boolean northWest = VoidWorldBoundary.isInside(chunkX - 1, chunkZ - 1);
        boolean southEast = VoidWorldBoundary.isInside(chunkX + 1, chunkZ + 1);
        boolean southWest = VoidWorldBoundary.isInside(chunkX - 1, chunkZ + 1);

        // Inner corners: two adjacent cardinal neighbors
        if (north && east) {
            return BoundaryType.INNER_CORNER_NORTH_EAST;
        }

        if (north && west) {
            return BoundaryType.INNER_CORNER_NORTH_WEST;
        }

        if (south && east) {
            return BoundaryType.INNER_CORNER_SOUTH_EAST;
        }

        if (south && west) {
            return BoundaryType.INNER_CORNER_SOUTH_WEST;
        }

        // Straight walls
        if (north) return BoundaryType.WALL_NORTH;
        if (south) return BoundaryType.WALL_SOUTH;
        if (east) return BoundaryType.WALL_EAST;
        if (west) return BoundaryType.WALL_WEST;

        // Outer corners: diagonal-only contact
        if (northEast) return BoundaryType.OUTER_CORNER_NORTH_EAST;
        if (northWest) return BoundaryType.OUTER_CORNER_NORTH_WEST;
        if (southEast) return BoundaryType.OUTER_CORNER_SOUTH_EAST;
        if (southWest) return BoundaryType.OUTER_CORNER_SOUTH_WEST;

        return BoundaryType.NONE;
    }
}
