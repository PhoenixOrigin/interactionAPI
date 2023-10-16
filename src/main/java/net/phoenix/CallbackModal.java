package net.phoenix;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CallbackModal extends ListenerAdapter {

    public final String name;
    public final List<ActionRow> rows;
    public final String id;
    public final ModalExecutor executor;
    public LinkedHashMap<Integer, Map.Entry<String, String>> storage = new LinkedHashMap<>();

    public CallbackModal(String name, String id, List<? extends ItemComponent> rows, ModalExecutor executor) {
        this.name = name;
        this.rows = rows.stream().map(ActionRow::of).collect(Collectors.toList());
        this.id = id;
        this.executor = executor;
    }

    public void addOption(String name, String data) {
        int index = storage.entrySet().stream().reduce((first, second) -> second).orElse(Map.entry(-1, Map.entry("", ""))).getKey() + 1;
        storage.put(index, Map.entry(name, data));
    }


    private Modal build() {
        if (!storage.isEmpty()) {
            StringBuilder modal_id = new StringBuilder(id);
            for (Map.Entry<Integer, Map.Entry<String, String>> entry : storage.entrySet()) {
                modal_id.append("-");
                modal_id.append(entry.getValue().getValue());
            }
            return Modal.create(modal_id.toString(), name).addComponents(rows).build();
        }
        return Modal.create(id, name).addComponents(rows).build();
    }

    public void reply(GenericCommandInteractionEvent e) {
        e.getJDA().addEventListener(this);
        e.replyModal(build()).queue();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (!event.getModalId().startsWith(id)) return;
        HashMap<String, String> values = new HashMap<>();
        String options = event.getModalId().replaceFirst(id, "");
        List<String> optionData = new ArrayList<>(List.of(options.split("-")));
        optionData.remove(0);
        for (int i = 0; i < optionData.size(); i++) {
            values.put(storage.get(i).getKey(), optionData.get(i));
        }
        this.executor.execute(event, values);
    }

    public void cleanup(JDA jda) {
        storage.clear();
        jda.removeEventListener(this);
    }

    public interface ModalExecutor {
        void execute(ModalInteractionEvent event, Map<String, String> values);
    }

}
