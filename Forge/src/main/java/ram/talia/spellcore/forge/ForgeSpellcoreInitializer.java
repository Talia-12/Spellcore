package ram.talia.spellcore.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import ram.talia.spellcore.api.softphysics.Physics;
import ram.talia.spellcore.common.lib.SpellcoreEntities;
import ram.talia.spellcore.forge.eventhandlers.PhysicsEventHandler;
import thedarkcolour.kotlinforforge.KotlinModLoadingContext;
import ram.talia.spellcore.api.SpellcoreAPI;
import ram.talia.spellcore.api.config.SpellcoreConfig;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(SpellcoreAPI.MOD_ID)
public class ForgeSpellcoreInitializer {
	
	public ForgeSpellcoreInitializer() {
		SpellcoreAPI.LOGGER.info("Hello Forge World!");
		initConfig();
		initRegistry();
		initListeners();
	}
	
	private static void initConfig () {
		Pair<ForgeSpellcoreConfig, ForgeConfigSpec> config = (new ForgeConfigSpec.Builder()).configure(ForgeSpellcoreConfig::new);
		Pair<ForgeSpellcoreConfig.Client, ForgeConfigSpec> clientConfig = (new ForgeConfigSpec.Builder()).configure(ForgeSpellcoreConfig.Client::new);
		Pair<ForgeSpellcoreConfig.Server, ForgeConfigSpec> serverConfig = (new ForgeConfigSpec.Builder()).configure(ForgeSpellcoreConfig.Server::new);
		SpellcoreConfig.setCommon(config.getLeft());
		SpellcoreConfig.setClient(clientConfig.getLeft());
		SpellcoreConfig.setServer(serverConfig.getLeft());
		ModLoadingContext mlc = ModLoadingContext.get();
		mlc.registerConfig(ModConfig.Type.COMMON, config.getRight());
		mlc.registerConfig(ModConfig.Type.CLIENT, clientConfig.getRight());
		mlc.registerConfig(ModConfig.Type.SERVER, serverConfig.getRight());
	}
	
	private static void initRegistry () {
		bind(Registry.ENTITY_TYPE_REGISTRY, SpellcoreEntities::registerEntities);
	}
	
	private static void initListeners () {
		IEventBus modBus = getModEventBus();
		IEventBus evBus = MinecraftForge.EVENT_BUS;
		
		modBus.register(ForgeSpellcoreClientInitializer.class);

		evBus.register(PhysicsEventHandler.class);
	}
	
	// https://github.com/VazkiiMods/Botania/blob/1.18.x/Forge/src/main/java/vazkii/botania/forge/ForgeCommonInitializer.java
	private static <T> void bind (ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		getModEventBus().addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> event.register(registry, rl, () -> t));
			}
		});
	}
	
	// This version of bind is used for BuiltinRegistries.
	private static <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		source.accept((t, id) -> Registry.register(registry, id, t));
	}
	
	private static IEventBus getModEventBus () {
		return KotlinModLoadingContext.Companion.get().getKEventBus();
	}
}