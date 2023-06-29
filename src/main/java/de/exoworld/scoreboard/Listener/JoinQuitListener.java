package de.exoworld.scoreboard.Listener;

import de.exoworld.scoreboard.Main;
import de.exoworld.scoreboard.Manager.LuckPermsManager;
import de.exoworld.scoreboard.Manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ScoreboardManager.getInstnace().getMoneyMap().put(e.getPlayer(), Main.getEconomy().getBalance(e.getPlayer()));
        ScoreboardManager.getInstnace().createScoreboard(e.getPlayer());
        ScoreboardManager.getInstnace().createInfos(e.getPlayer());

        LuckPermsManager.getInstance().getRankMap().put(e.getPlayer(), LuckPermsManager.getInstance().getPrimaryGroup(e.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ScoreboardManager.getInstnace().getMoneyMap().remove(e.getPlayer());
        LuckPermsManager.getInstance().getRankMap().remove((e.getPlayer()));
    }
}
