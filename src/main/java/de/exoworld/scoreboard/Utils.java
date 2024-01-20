package de.exoworld.scoreboard;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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

    public static String getMoneyString(double money) {
        DecimalFormat moneyFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMAN);
        moneyFormat.applyPattern("#,###.##");

        return moneyFormat.format(money);
    }
}
