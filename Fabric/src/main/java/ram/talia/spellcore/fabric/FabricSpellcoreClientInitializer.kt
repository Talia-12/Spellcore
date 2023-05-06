package ram.talia.spellcore.fabric

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import ram.talia.spellcore.client.RegisterClientStuff

object FabricSpellcoreClientInitializer : ClientModInitializer {
    override fun onInitializeClient() {
        RegisterClientStuff.init()

        RegisterClientStuff.registerBlockEntityRenderers(object : RegisterClientStuff.BlockEntityRendererRegisterer {
            override fun <T : BlockEntity> registerBlockEntityRenderer(type: BlockEntityType<T>, berp: BlockEntityRendererProvider<in T>) {
                BlockEntityRendererRegistry.register(type, berp)
            }
        })
    }
}