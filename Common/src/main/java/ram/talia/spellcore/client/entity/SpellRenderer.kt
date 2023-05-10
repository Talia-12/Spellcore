package ram.talia.spellcore.client.entity

import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import ram.talia.spellcore.api.SpellcoreAPI.modLoc
import ram.talia.spellcore.common.entities.SpellEntity

class SpellRenderer(context: EntityRendererProvider.Context) : EntityRenderer<SpellEntity>(context) {
    private val SPELL: ResourceLocation = modLoc("textures/entity/spell.png")

    override fun getTextureLocation(p0: SpellEntity): ResourceLocation = SPELL
}