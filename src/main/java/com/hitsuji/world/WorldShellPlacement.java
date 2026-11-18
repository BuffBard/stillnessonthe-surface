
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
        placeInternal(level, template, targetMin, rotation, true);
    }

    public static void placeReplacing(
            WorldGenLevel level,
            StructureTemplate template,
            BlockPos targetMin,
            Rotation rotation
    ) {
        placeInternal(level, template, targetMin, rotation, false);
    }

    private static void placeInternal(
            WorldGenLevel level,
            StructureTemplate template,
            BlockPos targetMin,
            Rotation rotation,
            boolean ignoreAir
    ) {
        StructurePlaceSettings settings =
                new StructurePlaceSettings()
                        .setRotation(rotation)
                        .setMirror(Mirror.NONE)
                        .setRotationPivot(BlockPos.ZERO)
                        .setIgnoreEntities(true);

        if (ignoreAir) {
            settings.addProcessor(BlockIgnoreProcessor.AIR);
        }

        BlockPos origin = template.getZeroPositionWithTransform(
                targetMin,
                Mirror.NONE,
                rotation
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
    public static void placeCorner(
            WorldGenLevel level,
            boolean diagonalInside,
            StructureTemplate inner,
            StructureTemplate outer,
            int innerSize,
            int outerSize,
            int anchorX,
            int anchorZ,
            boolean westSide,
            boolean northSide,
            int y,
            Rotation rotation,
            boolean replaceAir
    ) {
        StructureTemplate selected =
                diagonalInside ? inner : outer;

        int size = diagonalInside ? innerSize : outerSize;

        int x = westSide ? anchorX - size : anchorX + 1;
        int z = northSide ? anchorZ - size : anchorZ + 1;
        if (replaceAir && diagonalInside) {
            WorldShellPlacement.placeReplacing(
                    level,
                    selected,
                    new BlockPos(x, y, z),
                    rotation
            );
        } else {
            WorldShellPlacement.place(
                    level,
                    selected,
                    new BlockPos(x, y, z),
                    rotation
            );
        }
    }
}
