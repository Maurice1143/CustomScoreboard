package de.exoworld.scoreboard.Listener;

import de.exoworld.scoreboard.Main;
import de.exoworld.scoreboard.Manager.LuckPermsManager;
import de.exoworld.scoreboard.Manager.ScoreboardManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class RankChangeListener {
    Plugin plugin;
    public RankChangeListener(LuckPerms API) {
        this.plugin = Main.getInstance();

        API.getEventBus().subscribe(plugin, UserDataRecalculateEvent.class, e -> {
            Player player = Bukkit.getPlayer(e.getUser().getUniqueId());
            Map<Player, String> rankMap = LuckPermsManager.getInstance().getRankMap();
            if (player != null && player.isOnline()) {
                if (rankMap.containsKey(player) && !rankMap.get(player).equals(e.getUser().getPrimaryGroup())) {
                    ScoreboardManager.getInstnace().changeRank(player);
                    rankMap.replace(player, e.getUser().getPrimaryGroup());
                }
            }
        });
    }
}
