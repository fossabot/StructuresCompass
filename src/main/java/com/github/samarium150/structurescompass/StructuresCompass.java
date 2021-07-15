/*
 * Copyright (c) 2020-2021 Samarium, SnightW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, version 3 of the License
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-3.0.html>
 */
package com.github.samarium150.structurescompass;

import com.github.samarium150.structurescompass.command.StructureCompassCommand;
import com.github.samarium150.structurescompass.util.GeneralUtils;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**
 * Main class of the mod
 *
 * @see Mod
 */
@Mod(
    modid = GeneralUtils.MOD_ID,
    name = GeneralUtils.MOD_NAME,
    version = GeneralUtils.Version
)
public enum StructuresCompass {
    INSTANCE;

    @Mod.InstanceFactory
    public static StructuresCompass getInstance() {
        return INSTANCE;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GeneralUtils.logger.info("Loading StructureCompass.");
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new StructureCompassCommand());
    }
}
