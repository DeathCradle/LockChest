package com.craftmin.bukkit.Listeners;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.craftmin.bukkit.LockChest;
import com.craftmin.bukkit.Chest.Chest;
import com.craftmin.bukkit.Chest.ChestDefinition;

public class lcBlockListener extends BlockListener {

	LockChest plugin = null;
	
	public lcBlockListener(LockChest Plugin) {
		plugin = Plugin;
	}
	
	public void onBlockDamage(BlockDamageEvent event) {
		if(event.getBlock().getTypeId() != 54) { return; }
		if(plugin.isEnabled) { return; }
		if(!ChestDefinition.isLocked(event.getBlock(), event.getPlayer(), plugin)) {
			return;
		}
		event.setCancelled(true);
	}
	
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getBlock().getTypeId() != 54) { return; }
		if(!plugin.isEnabled) { return; }
		if(!ChestDefinition.isLocked(event.getBlock(), event.getPlayer(), plugin)) {
			if(plugin.dataSource == null) { return; }
			Chest chest = plugin.dataSource.getChest(event.getBlock().getLocation(), event.getPlayer().getWorld().getName());
			if(chest != null) {
				chest.UnLock();
				if(chest.getUserList() != null) {
					chest.getUserList().clear();
				}
				plugin.dataSource.addChest(chest, event.getPlayer().getWorld().getName());
			}
			return;
		}
		event.setCancelled(true);
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.getBlock().getTypeId() != 54) { return; }
		if(!plugin.isEnabled) { return; }
		Chest chest = new Chest();
		chest.setLocation(event.getBlock().getLocation());
		Location block = chest.isDoubleChest(event.getPlayer().getWorld());
		if(block != null) {
			if(ChestDefinition.isLocked(event.getPlayer().getWorld().getBlockAt(block), event.getPlayer(), plugin)) {
				event.setCancelled(true);
			}
		}
	}
	
}
