package org.makershaven.islandrankup;

import world.bentobox.bentobox.api.addons.Addon;

public class IslandRankup extends Addon {


    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadRanks();
        registerListener(new LevelCalculatedListener(this));
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

}


