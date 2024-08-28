package dev.arctic.exampleMenu.commands;

import dev.arctic.exampleMenu.menu.ExampleMenuUI;
import dev.arctic.interactivemenuapi.interfaces.IMenu;
import dev.arctic.interactivemenuapi.objects.Menu;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        /*
            Note that I'm creating this UI via command, but there's tons of other ways to do it
            including just having them be persistent in the world.
         */
        Player player = (Player) commandSender;
        Location playerLocation = player.getLocation();
        Location rootLocation = createRootLocation(playerLocation);

        // Create the menu here
        ExampleMenuUI.createMenu(rootLocation, player);

        return true;
    }


    /*
        The goal here is to use the Player's Location to derive a rootLocation for the UI,
        we'll do this by getting the player's loc. and yaw, then create the menu 3 blocks in
        front of them, and one block higher. We set the rootLoc YAW to be facing the player.
     */
    protected Location createRootLocation(Location playerLocation) {
        float playerYaw = playerLocation.getYaw();

        // Normalize the player's yaw to a positive value within [0, 360)
        float normalizedYaw = (playerYaw % 360 + 360) % 360;

        // Determine rootYaw based on normalizedYaw
        float rootYaw;
        if (normalizedYaw >= 0 && normalizedYaw < 90) {
            rootYaw = 180; // Facing South
        } else if (normalizedYaw >= 90 && normalizedYaw < 180) {
            rootYaw = 270; // Facing West
        } else if (normalizedYaw >= 180 && normalizedYaw < 270) {
            rootYaw = 0;   // Facing North
        } else {
            rootYaw = 90;  // Facing East
        }

        Location rootLocation = playerLocation.clone();

        // Adjust rootLocation based on player's yaw
        if (normalizedYaw >= 0 && normalizedYaw < 90) {
            rootLocation.add(0, 1, 3);    // Facing South
        } else if (normalizedYaw >= 90 && normalizedYaw < 180) {
            rootLocation.add(-3, 1, 0);   // Facing West
        } else if (normalizedYaw >= 180 && normalizedYaw < 270) {
            rootLocation.add(0, 1, -3);   // Facing North
        } else {
            rootLocation.add(3, 1, 0);    // Facing East
        }

        rootLocation.setYaw(rootYaw);
        return rootLocation;
    }

}
