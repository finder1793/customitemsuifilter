package me.apatuka.OraxenUIFilter;

import me.apatuka.OraxenUIFilter.Hooks.*;
import net.Indyuce.mmoitems.api.util.MMOItemReforger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

import me.apatuka.OraxenUIFilter.CMD.Commands;

import net.Indyuce.mmoitems.MMOItems;

public class OraxenUIFilter extends JavaPlugin {
	private static OraxenUIFilter instance;

	public void onEnable() {
		instance = this;

		if (Bukkit.getPluginManager().isPluginEnabled("MythicLib")) {
			getLogger().log(Level.INFO, "Hooked onto MythicLib");
			if (Bukkit.getPluginManager().isPluginEnabled("MMOItems")) {
				if (Bukkit.getPluginManager().isPluginEnabled("Oraxen")) {
					getLogger().log(Level.INFO, "Oraxen detected. Hooking with Oraxen");
					OraxenUIFilterHook.register();
				}

				if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
					getLogger().log(Level.INFO, "ItemsAdder detected. Hooking with ItemsAdder");
					ItemsAdderUIFilterHook.register();
				}

				getLogger().log(Level.INFO, "MMOItems detected, reloading config files...");

				MMOItems.plugin.getLanguage().reload();
				MMOItems.plugin.getDropTables().reload();
				MMOItems.plugin.getTypes().reload(isEnabled());
				MMOItems.plugin.getTiers().reload();
				MMOItems.plugin.getSets().reload();
				MMOItems.plugin.getUpgrades().reload();
				MMOItems.plugin.getWorldGen().reload();
				MMOItems.plugin.getCustomBlocks().reload();
				MMOItems.plugin.getLayouts().reload();
				MMOItems.plugin.getLore().reload();
				MMOItems.plugin.getTemplates().reload();
				MMOItems.plugin.getStats().reload(true);
				MMOItemReforger.reload();
			}

			getCommand("uimanager").setExecutor(new Commands());
		} else {
			getLogger().log(Level.WARNING, "MythicLib not found, disabling plugin ...");
			onDisable();
		}

	}

    @Override
    public void onDisable() {
    	instance = null;
    }

	// public static OraxenUIFilter getInstance() { return instance; }

}