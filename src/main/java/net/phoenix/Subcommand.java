package net.phoenix;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public class Subcommand {

    public final String name;
    private final String description;
    private final Command.CommandExecutor executor;
    ;
    private List<OptionData> options = new ArrayList<>();

    public Subcommand(String name, String description, List<OptionData> options, Command.CommandExecutor executor) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.executor = executor;
    }

    public SubcommandData getCommandData() {
        return new SubcommandData(name, description).addOptions(options);
    }

    public void execute(SlashCommandInteractionEvent event) {
        executor.execute(event);
    }

    public interface CommandExecutor {
        void execute(SlashCommandInteractionEvent event);
    }

}
