
package com.hitsuji.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

public final class WorldShellGenerator {

    private WorldShellGenerator() {}

    public static void generate(WorldGenLevel level, ChunkAccess chunk) {
        ChunkPos pos = chunk.getPos();

        int chunkX = pos.x();
        int chunkZ = pos.z();

        // Only generate in outside chunks immediately next to the world.
        if (VoidWorldBoundary.isInside(chunkX, chunkZ)
                || !VoidWorldBoundary.isBoundaryChunk(chunkX, chunkZ)) {
            return;
        }

        Optional<WorldShellTemplates> loaded =
                WorldShellTemplates.load(level);

        if (loaded.isEmpty()) {
            return;
        }

        WorldShellTemplates t = loaded.get();

        int minX = pos.getMinBlockX();
        int minZ = pos.getMinBlockZ();

        // These directions indicate which neighboring chunks are inside.
        boolean north = VoidWorldBoundary.isInside(chunkX, chunkZ - 1);
        boolean south = VoidWorldBoundary.isInside(chunkX, chunkZ + 1);
        boolean east = VoidWorldBoundary.isInside(chunkX + 1, chunkZ);
        boolean west = VoidWorldBoundary.isInside(chunkX - 1, chunkZ);
        boolean noCardinalNeighbors = !north && !south && !east && !west;

        if (noCardinalNeighbors) {
            boolean northwest = VoidWorldBoundary.isInside(
                    chunkX - 1, chunkZ - 1);
            boolean northeast = VoidWorldBoundary.isInside(
                    chunkX + 1, chunkZ - 1);
            boolean southeast = VoidWorldBoundary.isInside(
                    chunkX + 1, chunkZ + 1);
            boolean southwest = VoidWorldBoundary.isInside(
                    chunkX - 1, chunkZ + 1);

            if (northwest) {
                placeOuterCorner(level, t, minX, minZ, Rotation.NONE);
            } else if (northeast) {
                placeOuterCorner(level, t, minX, minZ, Rotation.CLOCKWISE_90);
            } else if (southeast) {
                placeOuterCorner(level, t, minX, minZ, Rotation.CLOCKWISE_180);
            } else if (southwest) {
                placeOuterCorner(level, t, minX, minZ, Rotation.COUNTERCLOCKWISE_90);
            }
            return;
        }

        if (north && west) {
            boolean diagonalInside =
                    VoidWorldBoundary.isInside(chunkX - 1, chunkZ - 1);

            placeCorner(
                    level, t, diagonalInside,
                    minX, minZ,
                    Rotation.NONE
            );
        } else if (north && east) {
            boolean diagonalInside =
                    VoidWorldBoundary.isInside(chunkX + 1, chunkZ - 1);

            placeCorner(
                    level, t, diagonalInside,
                    minX, minZ,
                    Rotation.CLOCKWISE_90
            );
        } else if (south && east) {
            boolean diagonalInside =
                    VoidWorldBoundary.isInside(chunkX + 1, chunkZ + 1);

            placeCorner(
                    level, t, diagonalInside,
                    minX, minZ,
                    Rotation.CLOCKWISE_180
            );
        } else if (south && west) {
            boolean diagonalInside =
                    VoidWorldBoundary.isInside(chunkX - 1, chunkZ + 1);

            placeCorner(
                    level, t, diagonalInside,
                    minX, minZ,
                    Rotation.COUNTERCLOCKWISE_90
            );
        } else if (north) {
            placeEdge(level, t, minX, minZ, Rotation.NONE);
        } else if (east) {
            placeEdge(level, t, minX, minZ, Rotation.CLOCKWISE_90);
        } else if (south) {
            placeEdge(level, t, minX, minZ, Rotation.CLOCKWISE_180);
        } else if (west) {
            placeEdge(level, t, minX, minZ, Rotation.COUNTERCLOCKWISE_90);
        }
    }


    private static void placeEdge(
            WorldGenLevel level,
            WorldShellTemplates t,
            int minX,
            int minZ,
            Rotation rotation
    ) {
        int bottomY = level.getMinY();
        int seaLevel = level.getSeaLevel()+64;

        int segmentHeight = t.wall.getSize().getY();

        if (segmentHeight <= 0) {
            System.err.println("[WorldShell] Invalid wall height!");
            return;
        }

        int capstoneY = bottomY;

        while (capstoneY <= seaLevel) {
            WorldShellPlacement.place(
                    level,
                    t.wall,
                    new BlockPos(minX, capstoneY, minZ),
                    rotation
            );

            capstoneY += segmentHeight;
        }

        WorldShellPlacement.place(
                level,
                t.capstone,
                new BlockPos(minX, capstoneY, minZ),
                rotation
        );
    }

    private static void placeCorner(
            WorldGenLevel level,
            WorldShellTemplates t,
            boolean diagonalInside,
            int minX,
            int minZ,
            Rotation rotation
    ) {
        StructureTemplate wall = diagonalInside
                ? t.wallInnerCorner
                : t.wallOuterCorner;

        StructureTemplate capstone = diagonalInside
                ? t.capstoneInnerCorner
                : t.capstoneOuterCorner;

        int bottomY = level.getMinY();
        int seaLevel = level.getSeaLevel()+64;

        int segmentHeight = wall.getSize().getY();

        if (segmentHeight <= 0) {
            System.err.println("[WorldShell] Invalid corner height!");
            return;
        }

        int capstoneY = bottomY;

        while (capstoneY <= seaLevel) {
            WorldShellPlacement.place(
                    level,
                    wall,
                    new BlockPos(minX, capstoneY, minZ),
                    rotation
            );

            capstoneY += segmentHeight;
        }

        WorldShellPlacement.place(
                level,
                capstone,
                new BlockPos(minX, capstoneY, minZ),
                rotation
        );
    }
    private static void placeOuterCorner(
            WorldGenLevel level,
            WorldShellTemplates t,
            int minX,
            int minZ,
            Rotation rotation
    ) {
        int bottomY = level.getMinY();
        int seaLevel = level.getSeaLevel()+64;

        int segmentHeight = t.wallOuterCorner.getSize().getY();

        if (segmentHeight <= 0) {
            System.err.println("[WorldShell] Invalid outer corner height!");
            return;
        }

        int capstoneY = bottomY;

        while (capstoneY <= seaLevel) {
            WorldShellPlacement.place(
                    level,
                    t.wallOuterCorner,
                    new BlockPos(minX, capstoneY, minZ),
                    rotation
            );

            capstoneY += segmentHeight;
        }

        WorldShellPlacement.place(
                level,
                t.capstoneOuterCorner,
                new BlockPos(minX, capstoneY, minZ),
                rotation
        );
    }
}
