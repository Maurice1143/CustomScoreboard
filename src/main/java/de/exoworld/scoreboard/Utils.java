package de.exoworld.scoreboard;

import org.bukkit.ChatColor;

public class Utils {
    public Utils() {}

    public static String convertColorString(String colorString) {
        String color = "§f";
        if (colorString != null && colorString.startsWith("&")) {
            color = colorString.replace("&", "§");
            return color;

        }
        //TODO Add ability to use Hexcolors
        //else if (colorString.startsWith("#")){

        //}
        return color;
    }
}
