package net.phoenix;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.List;
import java.util.stream.Collectors;

public class SubcommandGroup {

    private final String name;
    private final String description;
    private final List<Subcommand> options;

    public SubcommandGroup(String name, String description, List<Subcommand> options) {
        this.name = name;
        this.description = description;
        this.options = options;
    }

    public SubcommandGroupData getCommandData() {
        return new SubcommandGroupData(name, description).addSubcommands(options.stream().map(Subcommand::getCommandData).collect(Collectors.toList()));
    }

    public void execute(SlashCommandInteractionEvent event) {
        for(Subcommand option : options) {
            if(option.name.equals(event.getSubcommandName())) {
                option.execute(event);
            }
        }
    }
    
}
