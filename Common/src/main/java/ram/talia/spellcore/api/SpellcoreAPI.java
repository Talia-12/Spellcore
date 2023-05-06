package ram.talia.spellcore.api;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public interface SpellcoreAPI
{
	String MOD_ID = "spellcore";
	Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	Supplier<SpellcoreAPI> INSTANCE = Suppliers.memoize(() -> {
		try {
			return (SpellcoreAPI) Class.forName("ram.talia.spellcore.common.impl.SpellcoreAPIImpl")
								 .getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			LogManager.getLogger().warn("Unable to find SpellcoreAPIImpl, using a dummy");
			return new SpellcoreAPI() {
			};
		}
	});
	
	static SpellcoreAPI instance() {
		return INSTANCE.get();
	}
	
	static ResourceLocation modLoc(String s) {
		return new ResourceLocation(MOD_ID, s);
	}
}
