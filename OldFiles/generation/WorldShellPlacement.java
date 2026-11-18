
package com.hitsuji.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public final class WorldShellPlacement {

    private WorldShellPlacement() {
    }

    public static void place(
            WorldGenLevel level,
            StructureTemplate template,
            BlockPos position,
            Rotation rotation
    ) {

        StructurePlaceSettings settings =
                new StructurePlaceSettings()
                        .setRotation(rotation)
                        .setMirror(Mirror.NONE)
                        .setIgnoreEntities(true)
                        .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);

        template.placeInWorld(
                level,
                position,
                position,
                settings,
                level.getRandom(),
                2
        );
    }
}
