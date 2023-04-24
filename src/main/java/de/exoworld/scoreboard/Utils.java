package de.exoworld.scoreboard;

import org.bukkit.ChatColor;

public class Utils {
    public Utils() {}

    public static ChatColor convertColorString(String colorString) {
        if (colorString != null && colorString.startsWith("&")) {
            ChatColor color = ChatColor.getByChar(colorString.charAt(1));

            if (color != null) {
                return color;
            }

        }
        //TODO Add ability to use Hexcolors
        //else if (colorString.startsWith("#")){

        //}
        return ChatColor.getByChar("f");
    }
}
