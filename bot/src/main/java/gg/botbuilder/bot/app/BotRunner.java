package gg.botbuilder.bot.app;

import gg.botbuilder.bot.conf.Constants;
import gg.botbuilder.bot.repo.DummyConfigRepository;
import gg.botbuilder.bot.repo.IConfigRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotRunner {
    private static final Logger LOGGER = LogManager.getLogger();

    public void runOne() throws InterruptedException {
        // https://github.com/DV8FromTheWorld/JDA
        LOGGER.info("Preparing to run one");

        IConfigRepository repo = new DummyConfigRepository(); // TODO: Dependency Injection
        var handler = new BotEventHandler(repo);

        JDA jda = JDABuilder
                .createDefault("MTExNDU4Njc1Nzk0NjY4NzU2OQ.GsT1kt.0YshMOelD4N4AMS9wRm2-68usB10E1Ea8gOyQA")
                .addEventListeners(handler)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.of(Activity.ActivityType.PLAYING, Constants.DEFAULT_STATUS))
                .build();

        handler.setJda(jda);

        jda.updateCommands()
                        .addCommands(Commands.slash("ping", "Calculate ping of the bot"))
                .queue();

        jda.awaitReady();
        LOGGER.info("Ran one");
    }
}
