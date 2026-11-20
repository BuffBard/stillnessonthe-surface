package com.hitsuji.mixin;

import com.hitsuji.world.VoidBiomeHolder;
import com.hitsuji.world.VoidWorldBoundary;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ChunkAccess.class)
    public abstract class VoidBiomeChunkAccessMixin {

    @ModifyVariable(
            method = "fillBiomesFromNoise",
            at = @At("HEAD"),
            argsOnly = true,
            name = "biomeResolver")
    private BiomeResolver stillnessonthesurface$replaceOutsideBiome(
            BiomeResolver biomeResolver
    ) {
        ChunkAccess chunk = (ChunkAccess) (Object) this;

        ChunkPos pos = chunk.getPos();

        if (!VoidWorldBoundary.isInside(pos.x(), pos.z())) {
            Holder<Biome> voidBiome = VoidBiomeHolder.get();
            if (voidBiome == null) {
                return biomeResolver;
            }
            return (quartX, quartY, quartZ) -> voidBiome;
        }
        return biomeResolver;
    }
    }
