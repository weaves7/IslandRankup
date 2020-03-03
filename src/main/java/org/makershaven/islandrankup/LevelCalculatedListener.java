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

            //TODO This works but I would like to find a way to automate it for ops. Loop and remove all?
            if (player.isOp() || player.hasPermission("*")){
                player.sendMessage(ChatColor.GREEN + "[IslandRankup] " + ChatColor.RED + "Due to being opped you will need to manually remove your old island rank and add your new island rank, [" + newRankName + "]. Or deop yourself and rerun the command.");
            }
            else if (player.hasPermission(oldRankPerm)) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                        "lp user " + player.getName() + " parent remove " + oldRankName);
            }

            if (!player.hasPermission(newRankPerm)) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                        "lp user " + player.getName() + " parent add " + newRankName);
            }
        }


    }



}
