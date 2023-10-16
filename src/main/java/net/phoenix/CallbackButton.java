package net.phoenix;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CallbackButton extends ListenerAdapter {

    public final String name;
    public final ButtonStyle style;
    public final String id;
    public final ButtonExecutor executor;
    public LinkedHashMap<Integer, Map.Entry<String, String>> storage = new LinkedHashMap<>();

    public CallbackButton(String name, ButtonStyle style, String id, ButtonExecutor executor) {
        this.name = name;
        this.style = style;
        this.id = id;
        this.executor = executor;
    }

    public Button build(JDA jda) {
        jda.addEventListener(this);
        return Button.of(style, id, name);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getComponentId().startsWith(id)) return;
        HashMap<String, String> values = new HashMap<>();
        String options = event.getId().replaceFirst(id, "");
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

    public interface ButtonExecutor {
        void execute(ButtonInteractionEvent event, Map<String, String> data);
    }

}