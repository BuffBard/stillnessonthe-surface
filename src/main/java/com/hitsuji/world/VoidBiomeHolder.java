package com.hitsuji.world;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class VoidBiomeHolder {
    private static Holder<Biome> voidBiome;

    public static void set(Holder<Biome> biome) {
        voidBiome = biome;
    }

    public static Holder<Biome> get() {
        return voidBiome;
    }
}