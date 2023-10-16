package net.phoenix;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.stream.Collectors;

public class Command {
    private final String name;
    private final String description;
    private final List<OptionData> options;
    public List<Subcommand> subcommands;
    private final CommandExecutor executor;

    public Command(String name, String description, List<OptionData> options, CommandExecutor executor) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.executor = executor;
    }

    public void addSubcommands(List<Subcommand> subcommands) {
        if(!options.isEmpty()) throw new IllegalArgumentException("Options list must be empty!");
        this.subcommands = subcommands;
    }

    public CommandData getCommandData() {
        if(subcommands != null) {
            return Commands.slash(name, description).addSubcommands(subcommands.stream().map(Subcommand::getCommandData).collect(Collectors.toList()));
        }
        return Commands.slash(name, description).addOptions(options);
    }

    public void execute(SlashCommandInteractionEvent event) {
        executor.execute(event);
    }

    public interface CommandExecutor {
        void execute(SlashCommandInteractionEvent event);
    }
}
