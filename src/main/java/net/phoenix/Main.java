package net.phoenix;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault("").build();
        CommandRegistrar registrar = new CommandRegistrar(jda);
        Subcommand cmd = new Subcommand("", "", Collections.emptyList(), event -> {
        });
        Command command = new Command("", "", Command.CommandExecutors.SUBCOMMAND.getExecutor());
        command.addSubcommands(List.of(cmd));
        registrar.registerCommand(command);
    }

}
