package dev.arctic.exampleMenu.events;

import dev.arctic.interactivemenuapi.objects.Element;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractatEntityListener implements Listener {

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        //This is all you need to make the menu interactable.
        //element.doExternalFunction activates the BiConsumer function in the Element class,
        // so if your logic is sound - it'll work.
        if (entity.isValid() && entity.getType() == EntityType.INTERACTION) {

            if (entity.getMetadata("InteractiveMenu").isEmpty()) return;

            Element element = (Element) entity.getMetadata("InteractiveMenu").get(0).value();
            element.doExternalFunction(player, null);  // Pass the player and null as the input to onInteract
        }
    }
}
