package me.matchers.bot.manager;
/*/
Created by Matchers on 4/14/2021
 */

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class PermissionHandler {

    public static boolean isMod(Member user) {
        return user.hasPermission(Permission.MESSAGE_MANAGE);
    }
}
