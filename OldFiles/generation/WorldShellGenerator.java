
package com.hitsuji.world;

import com.hitsuji.world.generation.BoundaryDetector;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Optional;

public final class WorldShellGenerator {

    private WorldShellGenerator() {}

    public static void register() {
        ServerChunkEvents.CHUNK_LOAD.register(
                (level, chunk, newlyGenerated) -> {
                    if (!newlyGenerated) {
                        return;
                    }

                    onChunkGenerate(level, chunk);
                }
        );
    }

    private static void onChunkGenerate(ServerLevel level, LevelChunk chunk) {
        int chunkX = chunk.getPos().x();
        int chunkZ = chunk.getPos().z();

        BoundaryType boundaryType =
                BoundaryDetector.getBoundaryType(chunkX, chunkZ);

        if (boundaryType == BoundaryType.NONE) {
            return;
        }

        Optional<WorldShellTemplates> loaded =
                WorldShellTemplates.load(level);

        if (loaded.isEmpty()) {
            return;
        }

        WorldShellTemplates templates = loaded.get();

        switch (boundaryType) {
            case WALL_NORTH ->
                    placeWall(level, templates.wall, chunkX, chunkZ,
                            Rotation.NONE);

            case WALL_EAST ->
                    placeWall(level, templates.wall, chunkX, chunkZ,
                            Rotation.CLOCKWISE_90);

            case WALL_SOUTH ->
                    placeWall(level, templates.wall, chunkX, chunkZ,
                            Rotation.CLOCKWISE_180);

            case WALL_WEST ->
                    placeWall(level, templates.wall, chunkX, chunkZ,
                            Rotation.COUNTERCLOCKWISE_90);

            default -> {
                // Corners will be added after the basic wall works.
            }
        }
    }

    private static void placeWall(
            ServerLevel level,
            net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate wall,
            int chunkX,
            int chunkZ,
            Rotation rotation
    ) {
        int minX = chunkX * 16;
        int minZ = chunkZ * 16;

        int minY = level.getMinY();
        int seaLevel = level.getSeaLevel();

        int segmentHeight = wall.getSize().getY();

        if (segmentHeight <= 0) {
            System.err.println("[WorldShell] Wall template has an invalid height!");
            return;
        }

        for (int y = minY; y <= seaLevel; y += segmentHeight) {
            BlockPos placementPos = new BlockPos(minX, y, minZ);

            WorldShellPlacement.place(
                    level,
                    wall,
                    placementPos,
                    rotation
            );
        }

        System.out.println(
                "[WorldShell] Placed wall at chunk "
                        + chunkX + ", " + chunkZ
        );
    }
}
