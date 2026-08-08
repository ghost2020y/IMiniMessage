package me.zortex.iminimessage.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IMiniMessageTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if (sender.hasPermission("iminimessage.reload")) {
                completions.add("reload");
            }
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}