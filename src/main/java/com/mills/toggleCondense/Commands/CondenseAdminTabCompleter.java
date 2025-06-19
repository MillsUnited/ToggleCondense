package com.mills.toggleCondense.Commands;

import com.mills.toggleCondense.AllowedItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CondenseAdminTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("tc.admin")) return List.of();

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("add", "remove", "set"), new ArrayList<>());
        }

        return List.of();
    }
}
