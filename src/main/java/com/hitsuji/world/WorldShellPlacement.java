
package com.hitsuji.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public final class WorldShellPlacement {

    private WorldShellPlacement() {}


    public static void place(
            WorldGenLevel level,
            StructureTemplate template,
            BlockPos targetMin,
            Rotation rotation
    ) {
        placeInternal(level, template, targetMin, rotation);
    }

    private static void placeInternal(
            WorldGenLevel level,
            StructureTemplate template,
            BlockPos targetMin,
            Rotation rotation
    ) {
        // Correct the base orientation of our structure templates.
        Rotation actualRotation = switch (rotation) {
            case NONE -> Rotation.CLOCKWISE_180;
            case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
            case CLOCKWISE_180 -> Rotation.NONE;
            case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
        };

        StructurePlaceSettings settings =
                new StructurePlaceSettings()
                        .setRotation(actualRotation)
                        .setMirror(Mirror.NONE)
                        .setRotationPivot(BlockPos.ZERO)
                        .setIgnoreEntities(true);

        BlockPos origin = template.getZeroPositionWithTransform(
                targetMin,
                Mirror.NONE,
                actualRotation
        );

        template.placeInWorld(
                level,
                origin,
                origin,
                settings,
                level.getRandom(),
                2
        );
    }
}
