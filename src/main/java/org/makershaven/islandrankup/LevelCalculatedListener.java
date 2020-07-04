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
        String world;

        if (player == null) return;
        world = plugin.getConfig().getBoolean("world_specific", false) ? player.getWorld().getName() : null;

        int newLevel = (int) results.getLevel();

        String oldRankName = Rank.findPlayersOldRankName(plugin, player);
        String newRankName = Rank.getRankNameFromLevel(plugin, newLevel);

        if (newRankName == null) return;

        if (oldRankName == null || !oldRankName.equals(newRankName)) {
            Permission oldRankPerm = new Permission("islandrankup." + oldRankName, PermissionDefault.FALSE);
            Permission newRankPerm = new Permission("islandrankup." + newRankName, PermissionDefault.FALSE);

            if (player.isOp() || player.hasPermission("*")) {
                player.sendMessage(ChatColor.GREEN + "[IslandRankup] " + ChatColor.RED + "Due to being opped you may need to manually remove your old island rank and add your new island rank, [" + newRankName + "].");
            }
            else if (player.hasPermission(oldRankPerm)) {
                plugin.getPerms().playerRemoveGroup(world, player, oldRankName);
            }

            if (!player.hasPermission(newRankPerm)) {
                //Some permission plugins bugging when instantly remove and give group.

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin.getPlugin(), () -> {

                    plugin.getPerms().playerAddGroup(world, player, newRankName);
                    if (plugin.getConfig().getBoolean("level_up.enabled", false)) {
                        if (plugin.getConfig().getBoolean("level_up.broadcast_mode", false))
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("level_up.message").replace("<PLAYER>", player.getName()).replace("<RANK>", newRankName)));
                        else
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("level_up.message").replace("<PLAYER>", player.getName()).replace("<RANK>", newRankName)));
                    }
                }, 3);
            }
        }


    }


}
