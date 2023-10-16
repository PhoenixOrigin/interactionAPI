package net.phoenix;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;

public class CommandRegistrar extends ListenerAdapter {
    public static HashMap<String, Command> commands = new HashMap<>();
    private final JDA jda;

    public CommandRegistrar(JDA jda) {
        this.jda = jda;
        jda.addEventListener(this);
    }

    public void registerCommand(Command command) {
        CommandData commandData = command.getCommandData();
        jda.upsertCommand(commandData).queue();
        commands.put(commandData.getName(), command);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command cmd = commands.get(event.getName());
        if (cmd == null) return;
        cmd.execute(event);
    }

}
