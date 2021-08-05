package me.matchers.bot.commands;
/*/
Created by Matchers on 4/14/2021
 */

import me.matchers.bot.manager.PermissionHandler;
import me.matchers.bot.manager.ReportHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentStripped().split(" ");
        String raw_message = event.getMessage().getContentDisplay();
        TextChannel channel = event.getChannel();

        //command handler
        if (message[0].startsWith("/")) {
            switch (message[0].toLowerCase()) {
                case "/help": {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setColor(Color.cyan);
                    builder.setTitle("Venom Discord Bot - Command Usage");
                    builder.addField("/help", "- Displays this page.", false);
                    builder.addField("/report", "- Creates a report.", false);
                    builder.addField("/apply", "- Displays the application info message.", false);
                    channel.sendMessage(builder.build()).queue();
                    break;
                }
                case "/apply": {
                    if (PermissionHandler.isMod(Objects.requireNonNull(event.getMember()))) {
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.cyan);
                        builder.setTitle("Looking to become a staff member?");
                        builder.setDescription("You may apply to become a moderator at venomh.cf/apply");
                        builder.setFooter("This message was sent by a moderator");
                        channel.sendMessage(builder.build()).queue();
                        break;
                    }
                }
                case "/report": {
                    if (message.length == 3) {
                        ReportHandler.createReport(message[1], event.getGuild(), message[2]);
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.green);
                        builder.setDescription("Your report has been created!");

                        channel.sendMessage(builder.build()).queue();
                        break;
                    } else {
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.red);
                        builder.setDescription("Usage: /report <name> <reason>");

                        channel.sendMessage(builder.build()).queue();
                    }
                    break;
                }
                case "/activereports": {
                    if (PermissionHandler.isMod(Objects.requireNonNull(event.getMember()))) {
                        event.getMember().getUser().openPrivateChannel().queue((channel1) -> {
                            EmbedBuilder builder = new EmbedBuilder();
                            builder.setTitle("Active reports");
                            //gets a list of active reports...
                            for (Map.Entry<Integer, String> entry : ReportHandler.getReports().entrySet()) {
                                int id = entry.getKey();
                                String reasonanduser  = entry.getValue();
                                builder.addField(String.valueOf(id), reasonanduser, false); //add to embed
                            }
                            builder.setColor(Color.YELLOW); //setcolor
                            channel1.sendMessage(builder.build()).queue();
                        });
                    }
                    break;
                }
                case "/denyreport": {
                    if (PermissionHandler.isMod(Objects.requireNonNull(event.getMember()))) {
                        if (message.length == 2) {
                            ReportHandler.closeReport(Integer.parseInt(message[1]));
                            EmbedBuilder builder = new EmbedBuilder();
                            builder.setColor(Color.red);
                            builder.setDescription("Denied report successfully.");
                            channel.sendMessage(builder.build()).queue();
                        }
                    }
                    break;
                }
                case "/acceptreport": {
                    if (PermissionHandler.isMod(Objects.requireNonNull(event.getMember()))) {
                        if (message.length == 2) {
                            ReportHandler.closeReport(Integer.parseInt(message[1]));
                            EmbedBuilder builder = new EmbedBuilder();
                            builder.setColor(Color.green);
                            builder.setDescription("Accepted report successfully.");
                            channel.sendMessage(builder.build()).queue();
                        }
                    }
                    break;
                }
                case "/broadcast":
                case "/bc":
                case "/announce": {
                    if (PermissionHandler.isMod(Objects.requireNonNull(event.getMember()))) {
                        EmbedBuilder builder = new EmbedBuilder();

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();

                        builder.setColor(Color.red);
                        builder.setTitle("Announcement!");
                        builder.setDescription(raw_message
                                .replaceAll("/announce ", "")
                                .replaceAll("/bc ", "")
                                .replaceAll("/broadcast ", "")
                        );
                        builder.setFooter("This message was broadcasted by " + event.getMember().getEffectiveName() + " | " + dtf.format(now));
                        channel.sendMessage(builder.build()).queue();
                    }
                    break;
                }
                
                default: {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setColor(Color.red);
                    builder.setDescription("Command not recognized... Try ''/help''");

                    channel.sendMessage(builder.build()).queue();

                }
            }
            event.getMessage().delete().queue();
        }
        System.out.println("[BOT] Received message: " + event.getMessage().getContentDisplay());
    }
}
