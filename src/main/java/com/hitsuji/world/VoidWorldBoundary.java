package com.hitsuji.world;
import com.hitsuji.config.VoidWorldBoundaryConfig;

public final class VoidWorldBoundary {


    private VoidWorldBoundary() {
    }

    public static boolean isInside(int chunkX, int chunkZ) {
        int radius = VoidWorldBoundaryConfig.getWorldRadius();
        return chunkX * chunkX + chunkZ * chunkZ <= radius * radius;
    }

    public static boolean isBoundaryChunk(int chunkX, int chunkZ) {
        if (isInside(chunkX, chunkZ)) {
            return false;
        }
        return isInside(chunkX + 1, chunkZ)
                || isInside(chunkX - 1, chunkZ)
                || isInside(chunkX, chunkZ + 1)
                || isInside(chunkX, chunkZ - 1);
    }
    public static boolean isInsideNorth(int chunkX, int chunkZ) {
        return isInside(chunkX, chunkZ - 1);
    }

    public static boolean isInsideSouth(int chunkX, int chunkZ) {
        return isInside(chunkX, chunkZ + 1);
    }

    public static boolean isInsideEast(int chunkX, int chunkZ) {
        return isInside(chunkX + 1, chunkZ);
    }

    public static boolean isInsideWest(int chunkX, int chunkZ) {
        return isInside(chunkX - 1, chunkZ);
    }

}