package com.hitsuji.world.generation;

import com.hitsuji.world.BoundaryType;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

public final class BoundaryGeneration {

    private BoundaryGeneration() {
    }

    public static void register() {

        ServerChunkEvents.CHUNK_LOAD.register(
                (level, chunk, generated) -> {

                    // Only process newly generated chunks.
                    if (!generated) {
                        return;
                    }

                    ChunkPos pos = chunk.getPos();

                    int chunkX = pos.x();
                    int chunkZ = pos.z();

                    // Determine what structure belongs here.
                    BoundaryType type =
                            BoundaryDetector.getBoundaryType(
                                    chunkX,
                                    chunkZ
                            );

                    // Nothing to generate here.
                    if (type == BoundaryType.NONE) {
                        return;
                    }

                    // Generate the appropriate boundary structure.
                    generateBoundary(level, chunk, type);
                }
        );
    }

    private static void generateBoundary(
            net.minecraft.server.level.ServerLevel level,
            LevelChunk chunk,
            BoundaryType type
    ) {

        // Structure placement will go here.
    }
}
