package me.zortex.iminimessage.processor;

import me.zortex.iminimessage.converter.LegacyConverter;
import me.zortex.iminimessage.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ComponentProcessor {

    private final ConfigManager configManager;
    private final LegacyConverter legacyConverter;
    private final MiniMessage miniMessage;

    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");

    public ComponentProcessor(ConfigManager configManager, LegacyConverter legacyConverter) {
        this.configManager = configManager;
        this.legacyConverter = legacyConverter;
        this.miniMessage = MiniMessage.miniMessage();
    }

    public Component process(Component component) {
        if (component == null) return null;
        return processNode(component);
    }

    private Component processNode(Component node) {
        Component current = node;
        boolean isModifiedByMiniMessage = false;

        if (node instanceof TextComponent textNode) {
            String content = textNode.content();

            if (!content.isEmpty()) {
                if (configManager.isConvertLegacy()) {
                    content = legacyConverter.convertToMiniMessage(content);
                }

                boolean hasTags = TAG_PATTERN.matcher(content).find();
                if (!configManager.isParseOnlyWithTags() || hasTags) {
                    Component parsed = miniMessage.deserialize(content);
                    Style mergedStyle = parsed.style().merge(textNode.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                    current = parsed.style(mergedStyle);

                    isModifiedByMiniMessage = true;
                }
            }
        }

        if (!node.children().isEmpty()) {
            List<Component> processedChildren = new ArrayList<>();
            for (Component child : node.children()) {
                processedChildren.add(processNode(child));
            }

            if (isModifiedByMiniMessage) {
                List<Component> combined = new ArrayList<>(current.children());
                combined.addAll(processedChildren);
                current = current.children(combined);
            } else {
                current = current.children(processedChildren);
            }
        }

        return current;
    }
}