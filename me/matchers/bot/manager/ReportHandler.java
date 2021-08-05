package me.matchers.bot.manager;
/*/
Created by Matchers on 4/14/2021
 */

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Random;

public class ReportHandler {

    private static HashMap<Integer, String> reports = new HashMap();

    public static void createReport(String user, Guild guild, String reason) {
        Random random = new Random();
        int id = random.nextInt(1000);
        if (reports.containsKey(id)) {
            id = random.nextInt(id);
        }
        reports.put(id, user + " - " + reason);
        int finalId = id;
        //user.getUser().openPrivateChannel().flatMap(chat -> chat.sendMessage("Your report has been created with the id #" + finalId + ".")).queue();

        TextChannel channel = guild.getTextChannelById("831912143992782870");
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("New report! ID: #" + id);
        builder.setDescription(user + " - " + reason);

        assert channel != null;
        channel.sendMessage(builder.build()).queue();
    }

    public static HashMap<Integer, String> getReports() {
        return reports;
    }

    public static void closeReport(int id) {
        reports.remove(id);
    }
}
