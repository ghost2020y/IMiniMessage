package me.zortex.iminimessage.processor;

import me.zortex.iminimessage.converter.LegacyConverter;
import me.zortex.iminimessage.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ComponentProcessor {

    private final ConfigManager configManager;
    private final LegacyConverter legacyConverter;
    private final MiniMessage miniMessage;

    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");
    private static final int MAX_CACHE_SIZE = 512;

    public final Map<String, Component> cache = Collections.synchronizedMap(
            new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Component> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            }
    );

    public ComponentProcessor(ConfigManager configManager, LegacyConverter legacyConverter) {
        this.configManager = configManager;
        this.legacyConverter = legacyConverter;
        this.miniMessage = MiniMessage.miniMessage();
    }

    public Component process(Component component) {
        if (component == null) return null;
        return processNode(component);
    }

    public Component processNode(Component node) {
        if (node == null) return null;

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
                    Component parsed = cache.computeIfAbsent(content, miniMessage::deserialize);
                    Style mergedStyle = parsed.style().merge(textNode.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                    current = parsed.style(mergedStyle);
                    isModifiedByMiniMessage = true;
                }
            }
        } else if (node instanceof TranslatableComponent translatableNode) {
            List<TranslationArgument> newArgs = new ArrayList<>();
            boolean argsModified = false;

            for (TranslationArgument arg : translatableNode.arguments()) {
                if (arg.value() instanceof Component argComponent) {
                    Component processedArg = processNode(argComponent);
                    if (processedArg != argComponent) {
                        argsModified = true;
                    }
                    newArgs.add(TranslationArgument.component(processedArg));
                } else {
                    newArgs.add(arg);
                }
            }

            if (argsModified) {
                current = translatableNode.arguments(newArgs);
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