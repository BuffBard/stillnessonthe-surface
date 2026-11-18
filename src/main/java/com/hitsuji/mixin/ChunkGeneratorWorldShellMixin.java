
package com.hitsuji.mixin;

import com.hitsuji.world.VoidWorldBoundary;
import com.hitsuji.world.WorldShellGenerator;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorWorldShellMixin {

    @Inject(
            method = "applyBiomeDecoration",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stillness$handleWorldShell(
            WorldGenLevel level,
            ChunkAccess chunk,
            StructureManager structureManager,
            CallbackInfo ci
    ) {
        int chunkX = chunk.getPos().x();
        int chunkZ = chunk.getPos().z();

        if (!VoidWorldBoundary.isInside(chunkX, chunkZ)) {

            if (VoidWorldBoundary.isBoundaryChunk(chunkX, chunkZ)) {
                WorldShellGenerator.generate(level, chunk);
            }

            ci.cancel();
        }
    }
}
