package de.exoworld.scoreboard;

import de.exoworld.scoreboard.Commands.ScoreboardCommands;
import de.exoworld.scoreboard.Listener.JoinQuitListener;
import de.exoworld.scoreboard.Listener.MoneyChangeListener;
import de.exoworld.scoreboard.Manager.LuckPermsManager;
import de.exoworld.scoreboard.Manager.ScoreboardManager;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    private static final Logger log = Logger.getLogger("CustomScoreboard");
    private static Main plugin = null;
    private static Economy econ = null;
    private static ScoreboardManager ScoreboardManagerInstance;
    private static LuckPermsManager LuckPermsManagerInstance;

    private static Settings ScoreboardConfig;
    private static PluginManager manager;
    private MoneyChangeListener MoneyChangeListener;

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

    }

    @Override
    public void onEnable() {
        plugin = this;
        ScoreboardConfig = new Settings();

        // Plugin startup logic
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Vault API nicht gefunden!", getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupLuckPerms() ) {
            log.severe(String.format("[%s] - LuckPerms API nicht gefunden!", getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        manager = Bukkit.getPluginManager();

        createEvents();
        createCommands();

        ScoreboardManagerInstance = new ScoreboardManager();
        MoneyChangeListener = new MoneyChangeListener();

        getLogger().log(Level.INFO, String.format("[%s] - Erfolgreich gestartet", getName()));
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    private boolean setupLuckPerms() {
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            return false;
        }
        RegisteredServiceProvider<LuckPerms> rsp = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (rsp == null) {
           return false;
        }
        LuckPermsManagerInstance = new LuckPermsManager();
        return true;
    }


    private void createEvents() {
        manager.registerEvents(new JoinQuitListener(), this);
    }

    private void createCommands() {
        getCommand("customscoreboard").setExecutor(new ScoreboardCommands());
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static ScoreboardManager getScoreboardManager() {
        return ScoreboardManagerInstance;
    }

    public static LuckPermsManager getLuckPermsManager() {
        return LuckPermsManagerInstance;
    }

    public static Settings getSettings() {
        return ScoreboardConfig;
    }

    public static Main getInstance() {
        return plugin;
    }

    public static Logger getPluginLogger() {
        return log;
    }
}
