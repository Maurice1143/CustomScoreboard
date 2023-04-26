package de.exoworld.scoreboard.Listener;

import de.exoworld.scoreboard.Main;
import de.exoworld.scoreboard.Manager.ScoreboardManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import org.bukkit.plugin.Plugin;

public class RankChangeListener {
    Plugin plugin;
    public RankChangeListener(LuckPerms API) {
        this.plugin = Main.getInstance();

        API.getEventBus().subscribe(plugin, UserPromoteEvent.class, e -> {
            Player player = Bukkit.getPlayer(e.getUser().getUniqueId());

            if (player != null && player.isOnline()) {
                ScoreboardManager.getInstnace().changeRank(player);
            }

        });
        API.getEventBus().subscribe(plugin, UserDemoteEvent.class, e -> {
            Player player = Bukkit.getPlayer(e.getUser().getUniqueId());

            if (player != null && player.isOnline()) {
                ScoreboardManager.getInstnace().changeRank(player);
            }

        });

    }
}
