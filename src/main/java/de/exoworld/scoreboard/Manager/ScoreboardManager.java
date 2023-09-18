package de.exoworld.scoreboard.Manager;

import de.exoworld.scoreboard.Main;
import de.exoworld.scoreboard.Settings;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    private static ScoreboardManager instance;
    private final org.bukkit.scoreboard.ScoreboardManager manager;
    private String scoreboardName;
    private final Map<Integer, String> infoMap = new HashMap<>();
    private final Map<Player, Double> moneyMap = new HashMap<>();
    private int infoTimerId = -1;


    public ScoreboardManager() {
        instance = this;

        scoreboardName = Settings.getServerName() + "-CustomScoreboard";
        manager = Bukkit.getScoreboardManager();
        createInfoMap();
        createInfoTimer();
        refreshScoreboards();
    }

    public void createScoreboard(Player player) {
        Scoreboard board = manager.getNewScoreboard();
        

        Team money = board.registerNewTeam("money");
        money.addEntry(ChatColor.RED + "" + ChatColor.WHITE);

        Team rank = board.registerNewTeam("rank");
        rank.addEntry(ChatColor.AQUA + "" + ChatColor.WHITE);

        Team worth = board.registerNewTeam("worth");
        worth.addEntry(ChatColor.DARK_GRAY + "" + ChatColor.WHITE);

        Team info1 = board.registerNewTeam("info1");
        info1.addEntry(infoMap.get(0));
        Team info2 = board.registerNewTeam("info2");
        info2.addEntry(infoMap.get(1));
        Team info3 = board.registerNewTeam("info3");
        info3.addEntry(infoMap.get(2));

        player.setScoreboard(board);
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective(scoreboardName);
        if (objective != null) objective.unregister();
        objective = board.registerNewObjective(scoreboardName, Criteria.DUMMY, Component.text(Settings.getServerName()));

        Team money = board.getTeam("money");
        Team rank = board.getTeam("rank");
        //Team worth = board.getTeam("worth");

        String primaryGroup = LuckPermsManager.getInstance().getPrimaryGroup(player);
        String displayName = LuckPermsManager.getInstance().getDisplayName(primaryGroup);
        String adminColor = Settings.getAdminColor(primaryGroup);

        money.prefix(Component.text(Settings.getMoneyColor() + "" + String.format("%,.2f", (Main.getEconomy().getBalance(player))) + Settings.getMoneySuffix()));
        rank.prefix(Component.text(adminColor + displayName));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.displayName(Component.text(Settings.getServerName()));
        objective.getScore("").setScore(14);
        objective.getScore(Settings.getRankName()).setScore(13);
        objective.getScore(ChatColor.AQUA + "" + ChatColor.WHITE).setScore(12);
        objective.getScore(" ").setScore(11);
        objective.getScore(Settings.getMoneyName()).setScore(10);
        objective.getScore(ChatColor.RED + "" + ChatColor.WHITE).setScore(9);
        objective.getScore("  ").setScore(8);
        objective.getScore(Settings.getWorthTitle()).setScore(7);
        objective.getScore(ChatColor.DARK_GRAY + "" + ChatColor.WHITE).setScore(6);
        objective.getScore("   ").setScore(5);
        objective.getScore(Settings.getInfoName()).setScore(4);

        changeItemPrice(player, player.getInventory().getHeldItemSlot());
    }

    public void createInfos(Player player) {
        Objective objective = player.getScoreboard().getObjective(scoreboardName);
        String[] rdmMessage = Settings.getRandomMessage();
        final int maxInfoLength = 3;
        int length = maxInfoLength;
        if (objective != null) {
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
    }


    public void changeMoney(Player player, double money) {
        player.getScoreboard().getEntryTeam(ChatColor.RED + "" + ChatColor.WHITE).prefix(Component.text(Settings.getMoneyColor() + "" + String.format("%,.2f", money) + Settings.getMoneySuffix()));
    }

    public void changeRank(Player player) {
        String primaryGroup = LuckPermsManager.getInstance().getPrimaryGroup(player);
        String displayName = LuckPermsManager.getInstance().getDisplayName(primaryGroup);
        String adminColor = Settings.getAdminColor(primaryGroup);
        player.getScoreboard().getEntryTeam(ChatColor.AQUA + "" + ChatColor.WHITE).prefix(Component.text(adminColor + displayName));

    }

    public void refreshScoreboards() {
        LuckPermsManager.getInstance().getRankMap().clear();
        getMoneyMap().clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            LuckPermsManager.getInstance().getRankMap().put(player, LuckPermsManager.getInstance().getPrimaryGroup(player));
            getMoneyMap().put(player, Main.getEconomy().getBalance(player));
            createScoreboard(player);
            updateScoreboard(player);
            createInfos(player);
        }
    }

    public void changeItemPrice(Player p, Integer newSlot) {
        ItemStack item = p.getInventory().getItem(newSlot);
        String temp = Settings.getNotWorthText();
        if (item != null) {
            BigDecimal price = Main.getEss().getWorth().getPrice(Main.getEss(), p.getInventory().getItem(newSlot));

            if (price != null && price.intValue() > 0 ) {
                temp = Settings.getWorthText().replaceAll("%WORTH%", String.valueOf(price));
            }
        }

        p.getScoreboard().getEntryTeam(ChatColor.DARK_GRAY + "" + ChatColor.WHITE).prefix(Component.text(temp));
    }


    public void createInfoMap() {
        infoMap.put(0, ChatColor.DARK_PURPLE + "" + ChatColor.WHITE);
        infoMap.put(1, ChatColor.BLACK + "" + ChatColor.WHITE);
        infoMap.put(2, ChatColor.GOLD + "" + ChatColor.WHITE);
    }

    public void createInfoTimer() {
        infoTimerId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                createInfos(player);
            }
        }, Settings.getInfoCooldown() * 20L, Settings.getInfoCooldown() * 20L);
    }

    public void cancelInfoTimer() {
        if (infoTimerId != -1) {
            Bukkit.getScheduler().cancelTask(infoTimerId);
            infoTimerId = -1;
        }

    }

    public void restartInfoTimer() {
        cancelInfoTimer();
        createInfoTimer();
    }
    public Map<Player, Double> getMoneyMap() {
        return moneyMap;
    }

    public static ScoreboardManager getInstance() {
        return instance;
    }
}
