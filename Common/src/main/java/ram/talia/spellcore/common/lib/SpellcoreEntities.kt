package ram.talia.spellcore.common.lib

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import ram.talia.spellcore.api.SpellcoreAPI
import ram.talia.spellcore.api.SpellcoreAPI.modLoc
import ram.talia.spellcore.common.entities.SpellEntity
import java.util.function.BiConsumer

object SpellcoreEntities {
    @JvmStatic
    fun registerEntities(r: BiConsumer<EntityType<*>, ResourceLocation>) {
        for ((key, value) in ENTITIES.entries) {
            r.accept(value, key)
        }
    }

    private val ENTITIES: MutableMap<ResourceLocation, EntityType<*>> = LinkedHashMap()

    @JvmField
    val SPELL_ENTITY: EntityType<SpellEntity> = register(
        "spell_entity",
        EntityType.Builder.of(::SpellEntity, MobCategory.MISC)
            .sized(0.5f, 0.5f)
            .clientTrackingRange(32)
            .updateInterval(1)
            .build(SpellcoreAPI.MOD_ID + ":spell_entity")
    )

    private fun <T : Entity?> register(id: String, type: EntityType<T>): EntityType<T> {
        val old: EntityType<*>? = ENTITIES.put(modLoc(id), type)
        require(old == null) { "Typo? Duplicate id $id" }
        return type
    }
}