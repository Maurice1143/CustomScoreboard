package de.exoworld.scoreboard.Listener;

import de.exoworld.scoreboard.Manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class ItemChangeListener implements Listener {
    @EventHandler
    public void onItemInHandChange(PlayerItemHeldEvent e) {
        ScoreboardManager.getInstance().changeItemPrice(e.getPlayer(), e.getNewSlot());

    }
}
