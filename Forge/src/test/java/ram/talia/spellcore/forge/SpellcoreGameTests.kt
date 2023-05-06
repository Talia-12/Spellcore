package ram.talia.spellcore.forge

import net.minecraftforge.event.RegisterGameTestsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import ram.talia.spellcore.api.SpellcoreAPI

@EventBusSubscriber(modid = SpellcoreAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object SpellcoreGameTests {
	@SubscribeEvent
	fun registerTests(event: RegisterGameTestsEvent) {
		SpellcoreAPI.LOGGER.debug("registering tests")
		event.register(ExampleTests::class.java)
	}
}