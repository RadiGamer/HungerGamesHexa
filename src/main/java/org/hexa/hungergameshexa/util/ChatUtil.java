package org.hexa.hungergameshexa.util;

import org.bukkit.ChatColor;
import org.hexa.hungergameshexa.HungerGamesHexa;

public class ChatUtil {

    private static String prefix = "&n&f&l[&c&lHEXA&f&l] &r";

    public static void loadPrefixFromConfig(HungerGamesHexa plugin) {
        prefix = plugin.getConfig().getString("messages.prefix");
    }
    public static String preformat(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static String format(String string){
        return preformat(prefix + string);
    }

    // &#FB0000&lH&#EB0055&lE&#DC00AA&lX&#CC00FF&lA
}
