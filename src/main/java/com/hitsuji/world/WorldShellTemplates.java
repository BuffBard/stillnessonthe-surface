
package com.hitsuji.world;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

public final class WorldShellTemplates {

    private static final String NAMESPACE =
            "stillness-on-the-surface";

    public final StructureTemplate wall;
    public final StructureTemplate wallInnerCorner;
    public final StructureTemplate wallOuterCorner;

    public final StructureTemplate capstone;
    public final StructureTemplate capstoneInnerCorner;
    public final StructureTemplate capstoneOuterCorner;

    private WorldShellTemplates(
            StructureTemplate wall,
            StructureTemplate wallInnerCorner,
            StructureTemplate wallOuterCorner,
            StructureTemplate capstone,
            StructureTemplate capstoneInnerCorner,
            StructureTemplate capstoneOuterCorner
    ) {
        this.wall = wall;
        this.wallInnerCorner = wallInnerCorner;
        this.wallOuterCorner = wallOuterCorner;
        this.capstone = capstone;
        this.capstoneInnerCorner = capstoneInnerCorner;
        this.capstoneOuterCorner = capstoneOuterCorner;
    }

    public static Optional<WorldShellTemplates> load(
            WorldGenLevel level
    ) {
        var manager =
                level.getLevel().getStructureTemplateManager();

        var wall = manager.get(id("worldshell_wall"));
        var wallInner = manager.get(id("worldshell_wallinnercorner"));
        var wallOuter = manager.get(id("worldshell_walloutercorner"));

        var capstone = manager.get(id("worldshell_capstone"));
        var capstoneInner = manager.get(id("worldshell_capstoneinnercorner"));
        var capstoneOuter = manager.get(id("worldshell_capstoneoutercorner"));

        if (wall.isEmpty()
                || wallInner.isEmpty()
                || wallOuter.isEmpty()
                || capstone.isEmpty()
                || capstoneInner.isEmpty()
                || capstoneOuter.isEmpty()) {

            System.err.println(
                    "[WorldShell] One or more structure templates are missing!"
            );

            return Optional.empty();
        }

        return Optional.of(new WorldShellTemplates(
                wall.get(),
                wallInner.get(),
                wallOuter.get(),
                capstone.get(),
                capstoneInner.get(),
                capstoneOuter.get()
        ));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, path);
    }
}
