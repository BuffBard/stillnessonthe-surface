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
    public static boolean isDiagonalInside(
            int chunkX,
            int chunkZ,
            int offsetX,
            int offsetZ
    ) {
        return isInside(chunkX + offsetX, chunkZ + offsetZ);
    }

}