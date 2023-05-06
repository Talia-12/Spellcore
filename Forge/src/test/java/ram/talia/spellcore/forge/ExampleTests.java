package ram.talia.spellcore.forge;

import net.minecraft.gametest.framework.*;
import ram.talia.spellcore.api.SpellcoreAPI;

public class ExampleTests {
	@GameTest(templateNamespace = SpellcoreAPI.MOD_ID, template = "basic")
	public static void exampleTest(GameTestHelper helper) {
		SpellcoreAPI.LOGGER.debug("running example test");
		
		helper.onEachTick(() -> {
			SpellcoreAPI.LOGGER.debug("current tick: " + helper.getTick());
		});
	}
}
