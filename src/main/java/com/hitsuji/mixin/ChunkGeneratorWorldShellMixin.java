
package com.hitsuji.mixin;

import com.hitsuji.world.VoidWorldBoundary;
import com.hitsuji.world.WorldShellGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorWorldShellMixin {

    // Don't generate biome features in the void.
    @Inject(
            method = "applyBiomeDecoration",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stillness$stopVoidDecoration(
            WorldGenLevel level,
            ChunkAccess chunk,
            StructureManager structureManager,
            CallbackInfo ci) {

        int chunkX = chunk.getPos().x();
        int chunkZ = chunk.getPos().z();

        if (!VoidWorldBoundary.isInside(chunkX, chunkZ)) {
            ci.cancel();
        }
    }

    // Generate the shell after normal decoration.
    @Inject(
            method = "applyBiomeDecoration",
            at = @At("RETURN")
    )
    private void stillness$generateWorldShell(
            WorldGenLevel level,
            ChunkAccess chunk,
            StructureManager structureManager,
            CallbackInfo ci) {

        WorldShellGenerator.generate(level,chunk);
    }
}
