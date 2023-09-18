package de.exoworld.scoreboard;

import de.exoworld.scoreboard.Commands.ScoreboardCommands;
import de.exoworld.scoreboard.Listener.ItemChangeListener;
import de.exoworld.scoreboard.Listener.JoinQuitListener;
import de.exoworld.scoreboard.Listener.MoneyChangeListener;
import de.exoworld.scoreboard.Manager.LuckPermsManager;
import de.exoworld.scoreboard.Manager.ScoreboardManager;
import net.ess3.api.IEssentials;
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
    private static IEssentials ess = null;

    private static Settings ScoreboardConfig;
    private static PluginManager manager;

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

    }

    @Override
    public void onEnable() {
        plugin = this;
        ScoreboardConfig = new Settings();
        manager = getServer().getPluginManager();

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
        if (!setupEssentials()) {
            log.severe(String.format("[%s] - Essentials API nicht gefunden!", getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        createManager();
        createEvents();
        createCommands();

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
        new LuckPermsManager();
        return true;
    }

    private boolean setupEssentials() {
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            return false;
        }
        ess = (IEssentials) getServer().getPluginManager().getPlugin("Essentials");
        return true;
    }



    private void createEvents() {
        manager.registerEvents(new JoinQuitListener(), this);
        manager.registerEvents(new ItemChangeListener(), this);
        new MoneyChangeListener();
    }

    private void createCommands() {
        getCommand("customscoreboard").setExecutor(new ScoreboardCommands());
    }

    private void createManager() {
        new ScoreboardManager();
    }

    public static Economy getEconomy() {
        return econ;
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

    public static IEssentials getEss() {
        return ess;
    }
}
