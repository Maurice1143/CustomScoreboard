package de.exoworld.scoreboard.Listener;

import de.exoworld.scoreboard.Main;
import de.exoworld.scoreboard.Manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class MoneyChangeListener {

    public MoneyChangeListener() {
        //in seconds
        int checkInterval = 1;
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            ScoreboardManager manager = ScoreboardManager.getInstance();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(!manager.getMoneyMap().containsKey(player) || !manager.getMoneyMap().get(player).equals(Main.getEconomy().getBalance(player))){
                    manager.getMoneyMap().remove(player);
                    manager.getMoneyMap().put(player, Main.getEconomy().getBalance(player));
                    manager.changeMoney(player, Main.getEconomy().getBalance(player));
                }
            }
        }, checkInterval * 20, checkInterval * 20);
    }
}
