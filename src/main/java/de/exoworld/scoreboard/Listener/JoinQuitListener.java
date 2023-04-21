package de.exoworld.scoreboard.Listener;

import de.exoworld.scoreboard.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Main.getScoreboardManager().getMoneyMap().put(e.getPlayer(), Main.getEconomy().getBalance(e.getPlayer()));
        Main.getScoreboardManager().createScoreboard(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Main.getScoreboardManager().getMoneyMap().remove(e.getPlayer());
    }
}
