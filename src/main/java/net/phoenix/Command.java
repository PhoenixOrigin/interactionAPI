package net.phoenix;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Command {
    private final String name;
    private final String description;
    private final CommandExecutor executor;
    public List<Subcommand> subcommands;
    public List<SubcommandGroup> subcommandGroups;
    private List<OptionData> options;

    public Command(String name, String description, CommandExecutor executor) {
        this.name = name;
        this.description = description;
        this.executor = executor;
    }

    public void addSubcommands(List<Subcommand> subcommands) {
        if (!this.options.isEmpty() || !this.subcommandGroups.isEmpty())
            throw new IllegalArgumentException("Multiple option types may not be used");
        this.subcommands = subcommands;
    }

    public void addSubcommanGroups(List<SubcommandGroup> subcommandGroups) {
        if (!this.options.isEmpty() || !this.subcommands.isEmpty())
            throw new IllegalArgumentException("Multiple option types may not be used");
        this.subcommandGroups = subcommandGroups;
    }

    public void addOptions(List<OptionData> options) {
        if (!this.options.isEmpty() || !this.subcommandGroups.isEmpty())
            throw new IllegalArgumentException("Multiple option types may not be used");
        this.options = options;
    }

    public CommandData getCommandData() {
        if (subcommands != null) {
            return Commands.slash(name, description).addSubcommands(subcommands.stream().map(Subcommand::getCommandData).collect(Collectors.toList()));
        }
        return Commands.slash(name, description).addOptions(options);
    }

    public void execute(SlashCommandInteractionEvent event) {
        executor.execute(event);
    }

    public enum CommandExecutors {
        SUBCOMMAND_GROUP(event -> {
            Command command = CommandRegistrar.commands.get(event.getName());
            if (command == null) return;
            command.subcommandGroups.forEach((subcommandGroup -> {
                if (Objects.equals(event.getSubcommandGroup(), subcommandGroup.name)) {
                    subcommandGroup.options.forEach(subcommand -> {
                        if (subcommand.name.equals(event.getSubcommandName())) {
                            subcommand.execute(event);
                        }
                    });
                }
            }));
        }),
        SUBCOMMAND(event -> {
            Command command = CommandRegistrar.commands.get(event.getName());
            if (command == null) return;
            command.subcommands.forEach(subcommand -> {
                if (subcommand.name.equals(event.getSubcommandName())) {
                    subcommand.execute(event);
                }
            });
        });

        final CommandExecutor executor;

        CommandExecutors(CommandExecutor executor) {
            this.executor = executor;
        }

        public CommandExecutor getExecutor() {
            return executor;
        }
    }

    public interface CommandExecutor {
        void execute(SlashCommandInteractionEvent event);
    }
}
