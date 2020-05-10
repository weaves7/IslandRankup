package org.makershaven.islandrankup;

import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;
import world.bentobox.bentobox.api.addons.Addon;

import java.util.logging.Level;

public class IslandRankup extends Addon {

    private static Permission perms = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadRanks();
        registerListener(new LevelCalculatedListener(this));
        if (!setupPermissions()) {
            this.getLogger().log(Level.WARNING, "[IslandRankup] Could not hook into Vault's permissions!");
        }
        if (this.getConfig().getBoolean("metrics", true)) {
            Metrics metrics = new Metrics(this.getPlugin(), 6667);
            metrics.addCustomChart(new Metrics.SimplePie("addonVersion", () -> getDescription().getVersion()));
            this.getLogger().log(Level.INFO, "[IslandRankup] Thanks for enabling metrics!");
        }
    }

    @Override
    public void onDisable() {
        //Do nothing
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


