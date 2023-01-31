package me.jouu.itsurvivalcore.model.teleport;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MTeleportModel {
    private final @Getter Player player;
    private final @Getter Player target;
    private final @Getter BukkitRunnable bukkitRunnable;

    public MTeleportModel(Player player, Player target, BukkitRunnable bukkitRunnable) {
        this.player = player;
        this.target = target;
        this.bukkitRunnable = bukkitRunnable;
    }
}
