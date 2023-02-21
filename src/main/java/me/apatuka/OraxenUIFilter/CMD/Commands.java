package me.apatuka.OraxenUIFilter.CMD;

import me.apatuka.OraxenUIFilter.OraxenUIFilter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Location;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;

public class Commands implements CommandExecutor {
	private static OraxenUIFilter plugin;

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("uimanager")) {
			if(args[0].equalsIgnoreCase("debug")) {
				if(sender instanceof Player) {
					Player player = (Player)sender;
					ItemStack item = player.getInventory().getItemInMainHand();
					player.sendMessage(CustomStack.byItemStack(item).getNamespacedID());
					player.sendMessage(CustomStack.byItemStack(item).getNamespace());
					player.sendMessage(CustomStack.byItemStack(item).getId());
				}


				return false;
			}

			if(args[0].equalsIgnoreCase("check")) {
				ItemStack item = CustomStack.getInstance(args[1]).getItemStack();
				System.out.println(item);
			}

			if(args[0].equalsIgnoreCase("list")) {
				for(String name : CustomStack.getNamespacedIdsInRegistry()) {
					System.out.println(name);
				}
			}
		}
		return false;
	}
}