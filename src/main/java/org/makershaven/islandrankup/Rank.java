package org.makershaven.islandrankup;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashSet;
import java.util.Set;


class Rank {
    static Set<Rank> ranks = new HashSet<>();
    private String name;
    private int start;
    private int end;

    Rank(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;

    }


    private boolean containsLevel(int islandLevel) {
        return (islandLevel >= start && islandLevel <= end);
    }

    private String getName() {
        return name;
    }

    static String findPlayersOldRankName(IslandRankup plugin, Player player) {
        String oldRankName = null;
        for (Rank rank : ranks) {
            Permission oldRankPerm = new Permission("islandrankup." + rank.getName(), PermissionDefault.FALSE);
            if (player.hasPermission(oldRankPerm)) {
                oldRankName = rank.getName();
            }
        }
        return oldRankName;
    }

    static String getRankNameFromLevel(IslandRankup plugin, int level) {
        String rankName = null;
        for (Rank rank : ranks) {
            if (rank.containsLevel(level)) {
                rankName = rank.getName();
            }
        }
        return rankName;
    }

}
