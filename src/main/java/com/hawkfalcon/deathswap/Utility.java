package com.hawkfalcon.deathswap;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Utility {

    public DeathSwap plugin;

    public Utility(DeathSwap ds) {
        this.plugin = ds;
    }

    public void message(String message, String sender) {
        plugin.getServer().getPlayer(sender).sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + message);
    }

    public void broadcast(String message) {
        plugin.getServer().broadcastMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + message);
    }

    public void broadcastLobby(String message) {
        for (String sender : plugin.lobby) {
            plugin.getServer().getPlayer(sender).sendMessage("[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.WHITE + "] " + ChatColor.GREEN + message);
        }
    }

    // 0=lobby 1=arena
    public void teleport(String n, int spawn) {
        if (spawn == 0) {
            String cloc = plugin.getConfig().getString("lobby_spawn");
            runTp(n, cloc);
        }
        if (spawn == 1) {
            String cloc = plugin.getConfig().getString("end_spawn");
            runTp(n, cloc);
        }
    }

    public void runTp(String n, String cloc) {
        if (!(cloc.equals("world,0,0,0,0,0"))) {
            plugin.getServer().getPlayer(n).teleport(plugin.loc.getLocation(cloc));
        } else {
            plugin.utility.message(ChatColor.RED + "You must set spawn points with /ds set <lobby/end> first!", n);
            plugin.utility.broadcastLobby(n + " left the game!");
            plugin.lobby.remove(n);
        }
    }

    public void clearArmor(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void swapEffects(Location loc) {
        World world = loc.getWorld();
        world.playEffect(loc, Effect.GHAST_SHRIEK, 0);
        for (int x = 5; x > 1; x--) {
            world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
        }
    }

    public void playerReset(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        clearArmor(player);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void checkForStart() {
        int size = plugin.lobby.size();
        if (size > 1) {
            String playerone = plugin.lobby.get(0);
            String playertwo = plugin.lobby.get(1);
            plugin.start.newGame(playerone, playertwo);
        }
    }
}
