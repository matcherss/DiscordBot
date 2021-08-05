package me.matchers.bot.listeners;
/*/
Created by Matchers on 4/14/2021
 */

import me.matchers.bot.manager.FilterHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FilterListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (FilterHandler.check(event.getMessage().getContentRaw())) {
            event.getMessage().delete().queue();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.pink);
            builder.setDescription("Your message was automatically deleted.\nReason: **Blacklisted word**.");

            event.getChannel().sendMessage(builder.build()).queue();
        }
    }
}
