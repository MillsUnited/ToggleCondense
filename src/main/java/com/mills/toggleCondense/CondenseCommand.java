package com.mills.toggleCondense;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CondenseCommand implements CommandExecutor {

    private CondenseManager condenseManager;

    public CondenseCommand(CondenseManager condenseManager) {
        this.condenseManager = condenseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            condenseManager.toggleCondense(player);
        } else {
            sender.sendMessage("only players can run this command!");
        }

        return false;
    }
}
