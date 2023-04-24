package de.exoworld.scoreboard.Manager;

import de.exoworld.scoreboard.Listener.RankChangeListener;
import de.exoworld.scoreboard.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsManager {
    private static LuckPermsManager instance;
    private LuckPerms API;
    private final GroupManager groupManager;
    private final UserManager userManager;


    public LuckPermsManager() {
        instance = this;
        setupLuckPerms();

        groupManager = API.getGroupManager();
        userManager = API.getUserManager();

        Main.getSettings().createAdminColors();

        new RankChangeListener(API);
    }

     private void setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> rsp = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        API = rsp.getProvider();
    }

    public LuckPerms getAPI() {
        return API;
    }

    public String getDisplayName(String group) {
        String name = "UNKNOWN";
        if (!groupManager.isLoaded(group) || groupManager.getGroup(group).getDisplayName() == null) {
            return name;
        } else {
            return groupManager.getGroup(group).getDisplayName();
        }
    }

    public String getPrimaryGroup(Player player) {
        return userManager.getUser(player.getUniqueId()).getPrimaryGroup();
    }

    public static LuckPermsManager getInstance() {
        return instance;
    }
}
