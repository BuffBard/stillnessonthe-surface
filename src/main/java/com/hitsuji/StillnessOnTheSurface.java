package com.hitsuji;
import com.hitsuji.config.VoidWorldBoundaryConfig;
import com.hitsuji.world.VoidBiomeHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StillnessOnTheSurface implements ModInitializer {
	public static final String MOD_ID = "stillness-on-the-surface";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		VoidWorldBoundaryConfig.load();
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			VoidBiomeHolder.set(
					server.registryAccess()
							.lookupOrThrow(Registries.BIOME)
							.getOrThrow(Biomes.THE_VOID)
			);
		});
	}


	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
