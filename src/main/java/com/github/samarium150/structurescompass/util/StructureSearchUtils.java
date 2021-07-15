package com.github.samarium150.structurescompass.util;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class StructureSearchUtils {
    @Nullable
    public static <T extends World> BlockPos search(
        String structureName,
        BlockPos startingPoint,
        T world,
        boolean findUnExplored
    ) {
        structureName = nameStandardize(structureName);
        GeneralUtils.logger.debug("Standardized name: " + structureName);
        if (
            structureName.equals("Desert Pyramid") ||
                structureName.equals("Jungle Pyramid") ||
                structureName.equals("Swamp Hut") ||
                structureName.equals("Igloo")
        ) {
            // Special Cases?
            GeneralUtils.logger.debug("Searching for scattered structure...");
            int retryThreshHold = 100;
            int retryTimes = 0;
            do {
                BlockPos pos = new BlockPos(
                    startingPoint.getX() + retryTimes * (GeneralUtils.randGen.nextInt(64) - 32),
                    startingPoint.getY(),
                    startingPoint.getZ() + retryTimes * (GeneralUtils.randGen.nextInt(64) - 32)
                );
                BlockPos targetPos = world.findNearestStructure("Temple", pos, findUnExplored);
                if (targetPos == null) {
                    continue;
                }
                int chunkX = targetPos.getX() >> 4;
                int chunkZ = targetPos.getZ() >> 4;

                Biome biome = world.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
                if (biome != Biomes.JUNGLE && biome != Biomes.JUNGLE_HILLS) {
                    if (biome == Biomes.SWAMPLAND) {
                        // swamp hut
                        if (structureName.equals("Swamp Hut")) {
                            return targetPos;
                        }
                    } else if (biome != Biomes.DESERT && biome != Biomes.DESERT_HILLS) {
                        if (biome == Biomes.ICE_PLAINS || biome == Biomes.COLD_TAIGA) {
                            // igloo
                            if (structureName.equals("Igloo")) {
                                return targetPos;
                            }
                        }
                    } else {
                        // desert pyramid
                        if (structureName.equals("Desert Pyramid")) {
                            return targetPos;
                        }
                    }
                } else {
                    // jungle pyramid
                    if (structureName.equals("Jungle Pyramid")) {
                        return targetPos;
                    }
                }
            } while (retryTimes++ > retryThreshHold);
            GeneralUtils.logger.debug("Max retries. Quit searching.");
            return null;
        } else {
            return world.findNearestStructure(structureName, startingPoint, findUnExplored);
        }
    }

    private static String nameStandardize(String name) {
        StringBuilder ret = new StringBuilder();
        boolean lastIsSpace = true;
        for (char ch : name.toCharArray()) {
            if (ch == ' ' || ch == '_') {
                ret.append(' ');
                lastIsSpace = true;
            } else {
                if (lastIsSpace) {
                    lastIsSpace = false;
                    ret.append(Character.toUpperCase(ch));
                } else {
                    ret.append(Character.toLowerCase(ch));
                }
            }
        }
        return ret.toString();
    }
}
