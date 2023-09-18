package de.exoworld.scoreboard;

import de.exoworld.scoreboard.Manager.LuckPermsManager;
import de.exoworld.scoreboard.Manager.ScoreboardManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

public final class Settings {
    private static FileConfiguration config;
    private static String serverName;

    private static String moneyName;
    private static String moneySuffix;
    private static String moneyColor;

    private static String rankName;
    //private Map<String, ChatColor> rankColors = new HashMap<>();
    private static final Map<String, String> adminColors = new HashMap<>();

    private static String infoName;
    private static int infoCooldown;
    private List<String> infoMessages;
    private static String worthTitle;
    private static String worthText;
    private static String notWorthText;
    private static final List<String[]> infoMessageList = new ArrayList<>();

    public Settings() {
        Main.getInstance().saveDefaultConfig();
        config = Main.getInstance().getConfig();

        serverName = config.getString("ServerName", "Placeholder");
        rankName = config.getString("Rank.Name", "Your Rank:");
        moneyName = config.getString("Money.Name", "Money:");
        moneySuffix = config.getString("Money.Suffix", "$");
        moneyColor = config.getString("Money.Color", "§6");
        infoName = config.getString("Info.Name", "Infos:");
        infoCooldown = config.getInt("Info.Cooldown", 60);
        infoMessages = config.getStringList("Info.Infos");
        worthTitle = config.getString("Worth.Title");
        worthText = config.getString("Worth.Text");
        notWorthText = config.getString("Worth.NotWorthText");

        createMessages(getList(infoMessages));
    }

    public void createAdminColors() {
        adminColors.clear();
        LuckPerms lp = LuckPermsManager.getInstance().getAPI();
        for(Group group : lp.getGroupManager().getLoadedGroups()) {
            String value = group.getCachedData().getMetaData().getMetaValue("CustomScoreboardColor");
            String color = Utils.convertColorString(value);

            adminColors.put(group.getName(), color);
        }
    }

    private void createMessages(List<String> list) {
        infoMessageList.clear();
        for (String s : list) {
            String[] temp = s.split("::");
            if (temp.length <= 3) {
                infoMessageList.add(temp);
            } else {
                Main.getPluginLogger().log(Level.WARNING, "Scoreboard info Message'" + s + "' is split too long.");
            }
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getMoneyName() {
        return moneyName;
    }

    public static String getMoneySuffix() {
        return moneySuffix;
    }

    public static String getMoneyColor() {
        return moneyColor;
    }

    public static String getRankName() {
        return rankName;
    }

    public static String getInfoName() {
        return infoName;
    }
    public static int getInfoCooldown() {
        return infoCooldown;
    }
    public static String getWorthTitle() {
        return worthTitle;
    }
    public static String getWorthText() {
        return worthText;
    }
    public static String getNotWorthText() {
        return notWorthText;
    }

    public static String getAdminColor(String group) {
        String color = "§f";
        if(group != null && adminColors.get(group) != null) {
            color = adminColors.get(group);
        }

        return color;
    }

    public static String[] getRandomMessage() {
        if (infoMessageList.size() > 0) {
            Random r = new Random();
            int rdm = r.nextInt(infoMessageList.size());

            return infoMessageList.get(rdm);
        }
        return new String[]{"Test Information"};
    }


    private List<String> getList(final List<String> list) {
        list.removeIf(string -> string.startsWith("example"));
        return list;
    }

    public void reloadSettings() {
        Main.getInstance().reloadConfig();
        config = Main.getInstance().getConfig();

        serverName = config.getString("ServerName", "Placeholder");
        rankName = config.getString("Rank.Name", "Your Rank:");
        moneyName = config.getString("Money.Name", "Money:");
        moneySuffix = config.getString("Money.Suffix", "$");
        moneyColor = config.getString("Money.Color", "§6");
        infoName = config.getString("Info.Name", "Infos:");
        infoCooldown = config.getInt("Info.Cooldown", 60);
        infoMessages = config.getStringList("Info.Infos");
        worthTitle = config.getString("Worth.Title");
        worthText = config.getString("Worth.Text");
        notWorthText = config.getString("Worth.NotWorthText");

        createAdminColors();
        createMessages(getList(infoMessages));
        ScoreboardManager.getInstance().refreshScoreboards();
        ScoreboardManager.getInstance().restartInfoTimer();
    }
}
