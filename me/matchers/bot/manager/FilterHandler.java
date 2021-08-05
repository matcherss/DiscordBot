package me.matchers.bot.manager;
/*/
Created by Matchers on 4/14/2021
 */

import java.util.Arrays;

public class FilterHandler {

    private static String[] words = {"nigger", "nigga", "discord.gg/", "discordapp.com", "dsc.gg/", "discord.gg/", "pornhub.com", "faggot", "fag", "jew", "allah", "shit server", "shitter"};

    public static String[] getWords() {
        return words;
    }

    public static boolean check(String message) {
        boolean found = false;
        for (String word : getWords()) {
            if (message.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }
}
