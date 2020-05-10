package org.makershaven.islandrankup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import world.bentobox.level.calculators.CalcIslandLevel;
import world.bentobox.level.event.IslandLevelCalculatedEvent;


class LevelCalculatedListener implements Listener {
    private IslandRankup plugin;

    LevelCalculatedListener(IslandRankup plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onLevelCalculated(IslandLevelCalculatedEvent event) {
        CalcIslandLevel.Results results = event.getResults();
        Player player = Bukkit.getPlayer(event.getOwner());

        if (player == null) return;

        int newLevel = (int) results.getLevel();

        String oldRankName = Rank.findPlayersOldRankName(plugin,player);
        String newRankName = Rank.getRankNameFromLevel(plugin,newLevel);

        if (oldRankName == null || !oldRankName.equals(newRankName)) {
            Permission oldRankPerm = new Permission("islandrankup." + oldRankName, PermissionDefault.FALSE);
            Permission newRankPerm = new Permission("islandrankup." + newRankName, PermissionDefault.FALSE);

            if (player.isOp() || player.hasPermission("*")){
                player.sendMessage(ChatColor.GREEN+"[IslandRank]"+ChatColor.RED+"Due to being opped you will need to manually remove your old island rank and add your new island rank, ["+newRankName+"]. Or deop yourself and rerun the command.");
            }
            else if (player.hasPermission(oldRankPerm)) {
            	plugin.getPerms().playerRemoveGroup(player,oldRankName);
            }

            if (!player.hasPermission(newRankPerm)) {
            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin.getPlugin(), () -> {
					plugin.getPerms().playerAddGroup(player, newRankName);
					if(plugin.getConfig().getBoolean("level_up.enabled")) {
						if(plugin.getConfig().getBoolean("level_up.broadcast_mode")) Bukkit.broadcastMessage(plugin.getConfig().getString("level_up.message").replace("<PLAYER>", player.getName()).replace("<RANK>", newRankName));
						else player.sendMessage(plugin.getConfig().getString("level_up.message").replace("<PLAYER>", player.getName()).replace("<RANK>", newRankName));
					}
				}, 3);
            }
        }


    }



}
