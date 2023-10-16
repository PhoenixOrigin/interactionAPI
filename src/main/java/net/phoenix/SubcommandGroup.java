package net.phoenix;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubcommandGroup {

    public final String name;
    private final String description;
    public List<Subcommand> options = new ArrayList<>();

    public SubcommandGroup(String name, String description, List<Subcommand> options) {
        this.name = name;
        this.description = description;
        this.options = options;
    }

    public SubcommandGroupData getCommandData() {
        return new SubcommandGroupData(name, description).addSubcommands(options.stream().map(Subcommand::getCommandData).collect(Collectors.toList()));
    }

}
