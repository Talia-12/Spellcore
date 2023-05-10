package ram.talia.spellcore.fabric

import net.fabricmc.api.ModInitializer
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import ram.talia.spellcore.api.SpellcoreAPI
import ram.talia.spellcore.common.lib.SpellcoreEntities
import java.util.function.BiConsumer

object FabricSpellcoreInitializer : ModInitializer {

    override fun onInitialize() {
        SpellcoreAPI.LOGGER.info("Hello Fabric World!")

        FabricSpellcoreConfig.setup()

        initListeners()

        initRegistries()
    }

    private fun initListeners() {}

    private fun initRegistries() {
        SpellcoreEntities.registerEntities(bind(Registry.ENTITY_TYPE))
    }


    private fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
        BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
}