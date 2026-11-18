
package com.hitsuji.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

public final class WorldShellGenerator {

    private static final int WALL_DEPTH = 16;
    private static final int CAPSTONE_DEPTH = 16;

    private WorldShellGenerator() {
    }

    public static void generate(
            WorldGenLevel level,
            ChunkAccess chunk
    ) {

        ChunkPos pos = chunk.getPos();

        int chunkX = pos.x();
        int chunkZ = pos.z();

        if (!VoidWorldBoundary.isInside(chunkX, chunkZ)) {
            return;
        }

        Optional<WorldShellTemplates> loaded =
                WorldShellTemplates.load(level);

        if (loaded.isEmpty()) {
            return;
        }

        WorldShellTemplates templates = loaded.get();

        int minX = pos.getMinBlockX();
        int minZ = pos.getMinBlockZ();

        int maxX = minX + 15;
        int maxZ = minZ + 15;

        // Determines where the regular edges are.

        boolean north = !VoidWorldBoundary.isInside(
                chunkX, chunkZ - 1);

        boolean south = !VoidWorldBoundary.isInside(
                chunkX, chunkZ + 1);

        boolean east = !VoidWorldBoundary.isInside(
                chunkX + 1, chunkZ);

        boolean west = !VoidWorldBoundary.isInside(
                chunkX - 1, chunkZ);



        //Determines where the inner corners are.
        boolean northWestInner =
                north&&west&&south&&east&&!VoidWorldBoundary.isInside(chunkX-1,chunkZ-1);

        boolean northEastInner =
                north&&west&&south&&east&&!VoidWorldBoundary.isInside(chunkX-1,chunkZ-1);

        boolean southEastInner =
                north&&west&&south&&east&&!VoidWorldBoundary.isInside(chunkX-1,chunkZ-1);

        boolean southWestInner =
                north&&west&&south&&east&&!VoidWorldBoundary.isInside(chunkX-1,chunkZ-1);

        boolean hasInnerCorner =
                northWestInner ||
                        northEastInner ||
                        southEastInner ||
                        southWestInner;

        // Determine vertical placement.

        int bottomY = level.getMinY();
        int seaLevel = level.getSeaLevel();

        int capstoneY = bottomY;

        if (hasInnerCorner) {

            placeInnerCornerSection(
                    level,
                    templates,
                    minX,
                    minZ,
                    bottomY,
                    seaLevel,
                    northWestInner,
                    northEastInner,
                    southEastInner,
                    southWestInner
            );
        }

        // Place complete 16-block wall sections.
        // The final section is never shortened.

        while (capstoneY <= seaLevel) {
            if(!hasInnerCorner) {
                placeWallEdges(
                        level, templates,
                        minX, minZ, maxX, maxZ,
                        capstoneY,
                        north, south, east, west
                );

                placeWallOuterCorners(
                        level, templates,
                        chunkX, chunkZ,
                        minX, minZ, maxX, maxZ,
                        capstoneY,
                        north, south, east, west
                );
            }

            capstoneY += 16;
        }

        // Capstone sits above the final wall section.
        if(!hasInnerCorner) {
            placeCapstoneEdges(
                    level, templates,
                    minX, minZ, maxX, maxZ,
                    capstoneY,
                    north, south, east, west
            );

            placeCapstoneOuterCorners(
                    level, templates,
                    chunkX, chunkZ,
                    minX, minZ, maxX, maxZ,
                    capstoneY,
                    north, south, east, west
            );
        }
    }

    private static void placeWallEdges(
            WorldGenLevel level,
            WorldShellTemplates t,
            int minX, int minZ,
            int maxX, int maxZ,
            int y,
            boolean north,
            boolean south,
            boolean east,
            boolean west
    ) {

        if (north) {
            WorldShellPlacement.place(
                    level, t.wall,
                    new BlockPos(minX, y, minZ - WALL_DEPTH),
                    Rotation.NONE
            );
        }

        if (south) {
            WorldShellPlacement.place(
                    level, t.wall,
                    new BlockPos(minX, y, maxZ + 1),
                    Rotation.CLOCKWISE_180
            );
        }

        if (east) {
            WorldShellPlacement.place(
                    level, t.wall,
                    new BlockPos(maxX + 1, y, minZ),
                    Rotation.CLOCKWISE_90
            );
        }

        if (west) {
            WorldShellPlacement.place(
                    level, t.wall,
                    new BlockPos(minX - WALL_DEPTH, y, minZ),
                    Rotation.COUNTERCLOCKWISE_90
            );
        }
    }

    private static void placeCapstoneEdges(
            WorldGenLevel level,
            WorldShellTemplates t,
            int minX, int minZ,
            int maxX, int maxZ,
            int y,
            boolean north,
            boolean south,
            boolean east,
            boolean west
    ) {

        if (north) {
            WorldShellPlacement.place(
                    level, t.capstone,
                    new BlockPos(minX, y, minZ - CAPSTONE_DEPTH),
                    Rotation.NONE
            );
        }

        if (south) {
            WorldShellPlacement.place(
                    level, t.capstone,
                    new BlockPos(minX, y, maxZ + 1),
                    Rotation.CLOCKWISE_180
            );
        }

        if (east) {
            WorldShellPlacement.place(
                    level, t.capstone,
                    new BlockPos(maxX + 1, y, minZ),
                    Rotation.CLOCKWISE_90
            );
        }

        if (west) {
            WorldShellPlacement.place(
                    level, t.capstone,
                    new BlockPos(minX - CAPSTONE_DEPTH, y, minZ),
                    Rotation.COUNTERCLOCKWISE_90
            );
        }
    }


    private static void placeWallOuterCorners(
            WorldGenLevel level,
            WorldShellTemplates t,
            int chunkX, int chunkZ,
            int minX, int minZ,
            int maxX, int maxZ,
            int y,
            boolean north,
            boolean south,
            boolean east,
            boolean west
    ) {
        if (north && west) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX - 1, chunkZ - 1),
                    t.wallInnerCorner, t.wallOuterCorner,
                    5, 5, minX, minZ,
                    true, true, y, Rotation.NONE, false);
        }

        if (north && east) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX + 1, chunkZ - 1),
                    t.wallInnerCorner, t.wallOuterCorner,
                    5, 5, maxX, minZ,
                    false, true, y, Rotation.CLOCKWISE_90, false);
        }

        if (south && east) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX + 1, chunkZ + 1),
                    t.wallInnerCorner, t.wallOuterCorner,
                    5, 5, maxX, maxZ,
                    false, false, y, Rotation.CLOCKWISE_180, false);
        }

        if (south && west) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX - 1, chunkZ + 1),
                    t.wallInnerCorner, t.wallOuterCorner,
                    5, 5, minX, maxZ,
                    true, false, y, Rotation.COUNTERCLOCKWISE_90, false);
        }
    }

    private static void placeCapstoneOuterCorners(
            WorldGenLevel level,
            WorldShellTemplates t,
            int chunkX, int chunkZ,
            int minX, int minZ,
            int maxX, int maxZ,
            int y,
            boolean north,
            boolean south,
            boolean east,
            boolean west
    ) {
        if (north && west) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX - 1, chunkZ - 1),
                    t.capstoneInnerCorner, t.capstoneOuterCorner,
                    6, 6, minX, minZ,
                    true, true, y, Rotation.NONE, false);
        }

        if (north && east) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX + 1, chunkZ - 1),
                    t.capstoneInnerCorner, t.capstoneOuterCorner,
                    6, 6, maxX, minZ,
                    false, true, y, Rotation.CLOCKWISE_90, false);
        }

        if (south && east) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX + 1, chunkZ + 1),
                    t.capstoneInnerCorner, t.capstoneOuterCorner,
                    6, 6, maxX, maxZ,
                    false, false, y, Rotation.CLOCKWISE_180, false);
        }

        if (south && west) {
            WorldShellPlacement.placeCorner(level,
                    VoidWorldBoundary.isInside(chunkX - 1, chunkZ + 1),
                    t.capstoneInnerCorner, t.capstoneOuterCorner,
                    6, 6, minX, maxZ,
                    true, false, y, Rotation.COUNTERCLOCKWISE_90, false);
        }
    }
    private static void placeInnerCornerSection(
            WorldGenLevel level,
            WorldShellTemplates t,
            int minX,
            int minZ,
            int bottomY,
            int seaLevel,
            boolean northWest,
            boolean northEast,
            boolean southEast,
            boolean southWest
    ) {
        Rotation rotation;
        StructureTemplate wallTemplate;
        StructureTemplate capstoneTemplate;

        if (northWest) {
            rotation = Rotation.NONE;
        } else if (northEast) {
            rotation = Rotation.CLOCKWISE_90;
        } else if (southEast) {
            rotation = Rotation.CLOCKWISE_180;
        } else {
            rotation = Rotation.COUNTERCLOCKWISE_90;
        }

        wallTemplate = t.wallInnerCorner;
        capstoneTemplate = t.capstoneInnerCorner;

        int capstoneY = bottomY;

        while (capstoneY <= seaLevel) {
            WorldShellPlacement.place(
                    level,
                    wallTemplate,
                    new BlockPos(minX, capstoneY, minZ),
                    rotation
            );

            capstoneY += 16;
        }

        WorldShellPlacement.place(
                level,
                capstoneTemplate,
                new BlockPos(minX, capstoneY, minZ),
                rotation
        );
    }
}
