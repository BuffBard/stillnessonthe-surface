
package com.hitsuji.mixin;

import com.hitsuji.world.VoidWorldBoundary;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {

    @Inject(
            method = "buildTerrain",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stillnessonthesurface$stopOutsideTerrain(
            ChunkAccess chunk,
            Blender blender,
            RandomState randomState,
            StructureManager structureManager,
            BiomeManager biomeManager,
            @Nullable WorldGenRegion carverBiomeRegion,
            Set<Holder<Biome>> possibleBiomes,
            CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir
    ) {
        int chunkX = chunk.getPos().x();
        int chunkZ = chunk.getPos().z();

        if (!VoidWorldBoundary.isInside(chunkX, chunkZ)) {

            cir.setReturnValue(
                    CompletableFuture.completedFuture(chunk)
            );
        }
    }
}
