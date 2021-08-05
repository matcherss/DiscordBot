package me.matchers.bot;
/*/
Created by Matchers on 4/14/2021
 */

import me.matchers.bot.commands.CommandListener;
import me.matchers.bot.listeners.FilterListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import javax.security.auth.login.LoginException;

public class Bot {

    public static void main(String[] main) {

        try {
            String token = "token";
            JDABuilder builder = JDABuilder.createDefault(token);
            builder.setStatus(OnlineStatus.ONLINE);
            builder.addEventListeners(new CommandListener());
            builder.addEventListeners(new FilterListener());
            builder.build();
        } catch (LoginException e) {
            System.out.println("Unable to log in through the token. Is the token correct?");
        }
    }
}
