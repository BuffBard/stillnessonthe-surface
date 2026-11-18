package com.hitsuji.mixin;

import com.hitsuji.world.VoidWorldBoundary;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

    @Inject(
            method = "createStructures",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stillnessonthesurface$stopOutsideTerrain(
            RegistryAccess registryAccess,
            ChunkGeneratorStructureState state,
            StructureManager structureManager,
            ChunkAccess centerChunk,
            StructureTemplateManager structureTemplateManager,
            ResourceKey<Level> level,
            CallbackInfo ci
    ) {
        int chunkX = centerChunk.getPos().x();
        int chunkZ = centerChunk.getPos().z();

        if (!VoidWorldBoundary.isInside(chunkX, chunkZ)) {
            ci.cancel();
        }

    }
}
