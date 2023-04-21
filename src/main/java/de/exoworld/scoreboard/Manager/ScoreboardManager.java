package de.exoworld.scoreboard.Manager;

import de.exoworld.scoreboard.Main;
import de.exoworld.scoreboard.Settings;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    private final org.bukkit.scoreboard.ScoreboardManager manager;
    private String scoreboardName;
    private final Map<Integer, String> infoMap = new HashMap<>();
    private final Map<Player, Double> moneyMap = new HashMap<>();
    private int maxInfoLength = 3;
    private int infoDelay = 60; //in seconds

    public ScoreboardManager() {
        scoreboardName = Settings.getServerName() + "-CustomScoreboard";
        manager = Bukkit.getScoreboardManager();
        createInfoMap();
        refreshScoreboards();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                createInfos(player);
            }
        }, infoDelay * 20L, infoDelay * 20L);
    }

    public void createScoreboard(Player player) {
        final Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective(scoreboardName, "dummy", Component.text(Settings.getServerName()));

        Team money = board.registerNewTeam("money");
        money.addEntry(ChatColor.RED + "" + ChatColor.WHITE);
        money.prefix(Component.text(Settings.getMoneyColor() + "" + String.format("%,.2f", (Main.getEconomy().getBalance(player))) + Settings.getMoneySuffix()));

        String primaryGroup = Main.getLuckPermsManager().getPrimaryGroup(player);
        String displayName = Main.getLuckPermsManager().getDisplayName(primaryGroup);
        String adminColor = Settings.getAdminColor(primaryGroup);
        Team rank = board.registerNewTeam("rank");
        rank.addEntry(ChatColor.AQUA + "" + ChatColor.WHITE);
        rank.prefix(Component.text(adminColor + displayName));

        Team info1 = board.registerNewTeam("info1");
        info1.addEntry(infoMap.get(0));
        Team info2 = board.registerNewTeam("info2");
        info2.addEntry(infoMap.get(1));
        Team info3 = board.registerNewTeam("info3");
        info3.addEntry(infoMap.get(2));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.displayName(Component.text(Settings.getServerName()));
        objective.getScore("").setScore(11);
        objective.getScore(Settings.getRankName()).setScore(10);
        objective.getScore(ChatColor.AQUA + "" + ChatColor.WHITE).setScore(9);
        objective.getScore(" ").setScore(8);
        objective.getScore(Settings.getMoneyName()).setScore(7);
        objective.getScore(ChatColor.RED + "" + ChatColor.WHITE).setScore(6);
        objective.getScore("  ").setScore(5);
        objective.getScore(Settings.getInfoName()).setScore(4);

        player.setScoreboard(board);

        createInfos(player);
    }


    public void createInfos(Player player) {
        Objective objective = player.getScoreboard().getObjective(scoreboardName);
        String[] rdmMessage = Settings.getRandomMessage();
        int length = maxInfoLength;

        for (int i = 0; i < infoMap.size(); i++) {
            if (objective.getScore(infoMap.get(i)).isScoreSet()) {
                objective.getScore(infoMap.get(i)).resetScore();
            }
        }

        for (int i = 0; i < rdmMessage.length; i++) {
            if (i + 1 <= maxInfoLength) {
                player.getScoreboard().getEntryTeam(infoMap.get(i)).prefix(Component.text(rdmMessage[i]));
                objective.getScore(infoMap.get(i)).setScore(length);
                length--;
            }
        }

    }


    public void changeMoney(Player player, double money) {
        player.getScoreboard().getEntryTeam(ChatColor.RED + "" + ChatColor.WHITE).prefix(Component.text(Settings.getMoneyColor() + "" + String.format("%,.2f", money) + Settings.getMoneySuffix()));
    }

    public void changeRank(Player player) {
        String primaryGroup = Main.getLuckPermsManager().getPrimaryGroup(player);
        String displayName = Main.getLuckPermsManager().getDisplayName(primaryGroup);
        String adminColor = Settings.getAdminColor(primaryGroup);
        player.getScoreboard().getEntryTeam(ChatColor.AQUA + "" + ChatColor.WHITE).prefix(Component.text(adminColor + displayName));

    }

    public void refreshScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            createScoreboard(player);
        }
    }

    public void createInfoMap() {
        infoMap.put(0, ChatColor.DARK_PURPLE + "" + ChatColor.WHITE);
        infoMap.put(1, ChatColor.BLACK + "" + ChatColor.WHITE);
        infoMap.put(2, ChatColor.GOLD + "" + ChatColor.WHITE);
    }

    public Map<Player, Double> getMoneyMap() {
        return moneyMap;
    }
}
