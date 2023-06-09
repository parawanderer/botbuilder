package gg.botbuilder.bot;

import gg.botbuilder.bot.app.BotRunner;

class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("FOO");

        var runner = new BotRunner();

        runner.runOne();

        System.out.println("bar");
    }
}