
package com.hitsuji.world;

public enum BoundaryType {

    NONE,

    // Straight wall sections
    WALL_NORTH,
    WALL_SOUTH,
    WALL_EAST,
    WALL_WEST,

    // Outer wall corners
    OUTER_CORNER_NORTH_EAST,
    OUTER_CORNER_NORTH_WEST,
    OUTER_CORNER_SOUTH_EAST,
    OUTER_CORNER_SOUTH_WEST,

    // Inner wall corners
    INNER_CORNER_NORTH_EAST,
    INNER_CORNER_NORTH_WEST,
    INNER_CORNER_SOUTH_EAST,
    INNER_CORNER_SOUTH_WEST
}
