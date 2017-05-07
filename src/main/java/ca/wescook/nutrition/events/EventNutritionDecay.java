package ca.wescook.nutrition.events;

import ca.wescook.nutrition.configs.Config;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.nutrition.NutritionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventNutritionDecay {
	private int fauxTick = 0; // This integer helps limit how often the payload fires

	@SubscribeEvent
	public void PlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Only run on server
		EntityPlayer player = event.player;
		if (player.getEntityWorld().isRemote)
			return;

		if (fauxTick >= Config.decayRate) { // When the elapsed tick count reaches the configured value, trigger payload
			if (player.getFoodStats().getFoodLevel() <= Config.decayHungerLevel) { // When the food level of the player is below the threshold
				for (Nutrient nutrient : NutrientList.get()) // Cycle through nutrient list
					player.getCapability(NutritionProvider.NUTRITION_CAPABILITY, null).subtract(nutrient, 0.1F); // And update player nutrition
				fauxTick = 0; // If the payload has been triggered, reset tick count
			}
		}
		fauxTick++; // Gotta keep count...
	}
}