package org.hexa.hungergameshexa.util;

import org.bukkit.ChatColor;

public class ChatUtil {
    public static String preformat(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static String format(String string){
        return preformat("&n&f&l[&c&lHEXA&f&l] " + string);
    }

    // &#FB0000&lH&#EB0055&lE&#DC00AA&lX&#CC00FF&lA
}
