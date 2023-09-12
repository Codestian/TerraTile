package com.codestian;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerraTile implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("terratile");

	@Override
	public void onInitialize() {
		LOGGER.info("Running TerraTile!");
	}
}