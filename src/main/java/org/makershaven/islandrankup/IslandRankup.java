package org.makershaven.islandrankup;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;
import world.bentobox.bentobox.api.addons.Addon;

public class IslandRankup extends Addon {

    private static Permission perms = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadRanks();
        registerListener(new LevelCalculatedListener(this));
        setupPermissions();
    }

    @Override
    public void onDisable() {

    }

    private void loadRanks() {
        for (String rankName : getConfig().getConfigurationSection("ranks").getKeys(false)) {

            int start = getConfig().getInt("ranks." + rankName + ".start");
            int end = getConfig().getInt("ranks." + rankName + ".end");
            Rank.ranks.add(new Rank(rankName, start, end));
            getLogger().info("[IslandRankup] Loaded rank " + rankName + " from level " + start + " to " + end);
        }
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    public Permission getPerms() {
    	return perms;
    }

}


