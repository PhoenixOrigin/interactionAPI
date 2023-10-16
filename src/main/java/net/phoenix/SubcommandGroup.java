package net.phoenix;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.List;
import java.util.stream.Collectors;

public class SubcommandGroup {

    public final String name;
    public final List<Subcommand> options;
    private final String description;

    public SubcommandGroup(String name, String description, List<Subcommand> options) {
        this.name = name;
        this.description = description;
        this.options = options;
    }

    public SubcommandGroupData getCommandData() {
        return new SubcommandGroupData(name, description).addSubcommands(options.stream().map(Subcommand::getCommandData).collect(Collectors.toList()));
    }

}
