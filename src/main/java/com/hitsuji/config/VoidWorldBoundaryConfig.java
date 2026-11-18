package com.hitsuji.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class VoidWorldBoundaryConfig {

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("voidworld.properties");

    private static int worldRadius = 20;

    public static void load() {
        Properties properties = new Properties();
        System.out.println("Loading Void World configuration...");
        System.out.println("Config directory: " + CONFIG_PATH);

        try {
            if (Files.exists(CONFIG_PATH)) {
                try (InputStream input =
                             Files.newInputStream(CONFIG_PATH)) {
                    properties.load(input);
                }
                System.out.println("Configuration saved!");
            }

            worldRadius = Math.max(0,
                    Integer.parseInt(
                            properties.getProperty(
                                    "worldRadius", "20"
                            )
                    ));

            properties.setProperty(
                    "worldRadius",
                    String.valueOf(worldRadius)
            );

            Files.createDirectories(CONFIG_PATH.getParent());

            try (OutputStream output =
                         Files.newOutputStream(CONFIG_PATH)) {
                properties.store(output,
                        "Void World Configuration");
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println(
                    "Failed to load Void World configuration: "
                            + e.getMessage()
            );
            worldRadius = 20;
        }
    }

    public static int getWorldRadius() {
        return worldRadius;
    }
}
