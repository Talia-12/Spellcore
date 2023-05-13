package ram.talia.spellcore.client.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import ram.talia.spellcore.api.RenderHelper
import ram.talia.spellcore.api.SpellcoreAPI.modLoc
import ram.talia.spellcore.common.entities.SpellEntity

class SpellRenderer(context: EntityRendererProvider.Context) : EntityRenderer<SpellEntity>(context) {
    private val SPELL: ResourceLocation = modLoc("textures/entity/spell.png")

    override fun getTextureLocation(p0: SpellEntity): ResourceLocation = SPELL

    override fun render(spell: SpellEntity, yaw: Float, partialTicks: Float, ps: PoseStack, bufferSource: MultiBufferSource, packedLight: Int) {

//        val minecraft = Minecraft.getInstance()
//        val camera: Camera = minecraft.gameRenderer.mainCamera
//        val levelAccessor: LevelAccessor = minecraft.level ?: return
//        val dimensionType = levelAccessor.dimensionType()
//        val blockPos = BlockPos(camera.position.x, 0.0, camera.position.z)
        val vertexConsumer: VertexConsumer = bufferSource.getBuffer(RenderType.lines())

        for (vertex in spell.vertices) {
            RenderHelper.renderPoint(ps, vertexConsumer, vertex.pos, RADIUS, 1f, 1f, 1f, 1f)
        }

        for (face in spell.faces) {
            RenderHelper.renderLineTriangle(ps, vertexConsumer, face.p0.pos, face.p1.pos, face.p2.pos, 0.8f, 1f, 1f, 1f)
        }
    }

    companion object {
        const val RADIUS = 0.005
    }
}