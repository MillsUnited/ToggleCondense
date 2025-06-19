package com.mills.toggleCondense.Commands;

import com.mills.toggleCondense.AllowedItems;
import com.mills.toggleCondense.CondenseManager;
import com.mills.toggleCondense.Main;
import com.mills.toggleCondense.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CondenseAdmin implements CommandExecutor {

    private CondenseManager condenseManager;
    private AllowedItems allowedItems;

    public CondenseAdmin(Main main) {
        this.condenseManager = main.getCondenseManager();
        this.allowedItems = main.getAllowedItems();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (sender instanceof Player) {
            Player player =  (Player) sender;

            if (!player.hasPermission("tc.admin")) return false;

            if (args.length == 0) {
                player.sendMessage(condenseManager.prefix + ChatColor.RED + "/tcadmin <action>");
                return false;
            }

            ItemStack item = player.getItemInHand().clone();
            String itemName = "Item";
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                itemName = Utils.format(item.getItemMeta().getDisplayName());
            } else if (item.getType() != null) {
                itemName = Utils.format(item.getType().toString());
            }

            String subcommand =  args[0].toLowerCase();
            switch (subcommand) {
                case "add":

                    if (allowedItems.isItem(item)) {
                        if (item.getType().equals(Material.AIR)) {
                            player.sendMessage(condenseManager.prefix + ChatColor.RED + "You are not holding anything!");
                            return false;
                        }
                        player.sendMessage(condenseManager.prefix + "this item is already a condensed item!");
                        return false;
                    }

                    allowedItems.addItem(item);
                    player.sendMessage(condenseManager.prefix + ChatColor.translateAlternateColorCodes('&', "added " +
                            ChatColor.RED + itemName + ChatColor.GRAY + " to the allowed condensed items!"));

                    return true;
                case "remove":

                    if (!allowedItems.isItem(item)) {
                        if (item.getType().equals(Material.AIR)) {
                            player.sendMessage(condenseManager.prefix + ChatColor.RED + "You are not holding anything!");
                            return false;
                        }
                        player.sendMessage(condenseManager.prefix + ChatColor.RED + itemName + ChatColor.GRAY + " is not a condensedable item!");
                        return false;
                    }

                    allowedItems.removeItem(item);
                    player.sendMessage(condenseManager.prefix + ChatColor.translateAlternateColorCodes('&', "removed " +
                            ChatColor.RED + itemName + ChatColor.GRAY + " from the allowed condensed items!"));

                    return true;
                case "set":

                    if (!allowedItems.isItem(item)) {
                        player.sendMessage(condenseManager.prefix + ChatColor.RED + "This item is not allowed for condensing.");
                        return true;
                    }

                    ItemStack condensed = condenseManager.createCondensedItem(item, item.getAmount());
                    player.getInventory().setItemInMainHand(condensed);
                    player.sendMessage(condenseManager.prefix + ChatColor.GREEN + "Set your item as condensed: " + ChatColor.RED + itemName);
                    return true;

                default:
                    player.sendMessage(condenseManager.prefix + ChatColor.RED + "/tcadmin <action>");
                    break;
            }
        }

        return false;
    }
}
