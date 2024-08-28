package dev.arctic.exampleMenu.menu;

import dev.arctic.exampleMenu.ExampleMenu;
import dev.arctic.interactivemenuapi.builders.*;
import dev.arctic.interactivemenuapi.interfaces.*;
import dev.arctic.interactivemenuapi.animation.AnimationType;
import dev.arctic.interactivemenuapi.objects.Division;
import dev.arctic.interactivemenuapi.objects.Menu;
import dev.arctic.interactivemenuapi.objects.elements.DisplayElement;
import dev.arctic.interactivemenuapi.objects.elements.TextElement;
import dev.arctic.interactivemenuapi.objects.elements.ToggleElement;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class ExampleMenuUI {

    public static IMenu createMenu(Location rootLocation, Player owner) {


        /*
            Creating a menu is a funny order, but it makes sense ultimately - you need to create the menu, then the division, then the elements.
            After you attach these elements to the division, you attach the division to the menu.

            Menu -> Division -> Elements -> Division -> Menu, a bit like an Accordion
         */


        // Create the menu
        Menu menu = new MenuBuilder()
                .setRootLocation(rootLocation)
                .setPlugin(ExampleMenu.getPlugin(ExampleMenu.class))
                .setOwner(owner)
                .setDivisions(new ArrayList<>())
                .setTimeoutSeconds(600)
                .setDoCleanup(false)
                .build();

        /*
                menu.getObjectStorage().add(menu);

                example of how to store objects in the objectStorage.
                It takes a generic Object and puts it into a List<Object> for you to access later.
                You will need to know how to query for the object you want.
         */

        // Create division
        Division division = new DivisionBuilder()
                .setAnimationType(AnimationType.LEFT)
                .setAnimationStepper(0.1f)
                .setElements(new ArrayList<>())
                .setInitialLocation(rootLocation)
                .setOffset(new Vector(0,0,0))
                .setParentMenu(menu)
                .build();

        // Create display element
        DisplayElement displayElement = new DisplayElementBuilder()
                .setDisplayItem(new ItemStack(Material.RED_CONCRETE_POWDER))
                .setParentMenu(menu)
                .setParentDivision(division)
                .setOffset(new Vector(0, 2, 0))
                .build();

        displayElement.getInteractionEntity()
                .setInteractionWidth(1f);
        displayElement.getInteractionEntity()
                .setInteractionHeight(1f);

        // Set the interaction behavior
        displayElement.setExternalFunction((player, input) -> {
            ItemStack currentItem = displayElement.getDisplayItem();
            if (currentItem.getType() != Material.POTATO) {
                displayElement.setDisplayItem(new ItemStack(Material.POTATO));
            } else {
                displayElement.setDisplayItem(new ItemStack(Material.RED_CONCRETE_POWDER));
            }
        });

        // Create text element
        TextElement textElement = new TextElementBuilder()
                .setText(Component.text("Test Text Element :D"))
                .setParentMenu(menu)
                .setParentDivision(division)
                .setOffset(new Vector(0, 0, 0))
                .build();

        // Create toggle element
        ToggleElement toggleElement = new ToggleElementBuilder()
                .storePrimaryText(Component.text("Primary Text"))
                .storeSecondaryText(Component.text("Secondary Text"))
                .setPressAnimationType(AnimationType.PRESSED)
                .setPressAnimationStepper(0.1f)
                .setOffset(new Vector(0, 1, 0))
                .setParentMenu(menu)
                .setParentDivision(division)
                .build();

        /*
            if you need to set custom sizes for the interaction entity - this is how you do it!

            Note that by default interactions are 1x1x1 in size, so its a good idea to set this to
            the size of your text.

            Setting this as part of the Builder is on the roadmap
         */
        toggleElement.getInteractionEntity()
                .setInteractionWidth(0.5f);
        toggleElement.getInteractionEntity()
                .setInteractionHeight(0.2f);

        //this is how you add custom functionality to the elements, for toggles especially, use a runnable to delay the task and apply animations
        //also note that Animations are a roadmap item as well - for now they can be created by updating the locations and offsets.
        toggleElement.setExternalFunction((player, input) -> {
            toggleElement.toggle();
            toggleElement.applyAnimation(10);
            new BukkitRunnable() {
                @Override
                public void run() {
                    menu.cleanup();
                }
            }.runTaskLater(ExampleMenu.getPlugin(ExampleMenu.class), 10L);
        });

        // Add the elements to the division
        division.addElement(toggleElement);
        division.addElement(textElement);
        division.addElement(displayElement);

        // Add the division to the menu
        menu.addDivision(division);

        return menu;
    }
}
