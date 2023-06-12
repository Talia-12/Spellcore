package ram.talia.spellcore.client.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.RenderHelper
import ram.talia.spellcore.api.SpellcoreAPI.modLoc
import ram.talia.spellcore.api.minus
import ram.talia.spellcore.api.softphysics.Physics.POINT_RADIUS
import ram.talia.spellcore.api.softphysics.Vertex
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
        RenderHelper.renderPoint(ps, vertexConsumer, Vec3.ZERO, POINT_RADIUS, 0f, 1f, 1f, 1f)

        ps.pushPose()
        ps.translate(-spell.x, -spell.y, -spell.z)

        for (vertex in spell.vertices) {
            RenderHelper.renderPoint(ps, vertexConsumer, vertex.pos, POINT_RADIUS, 1f, if (vertex.collisionThisTick) 0f else 1f, if (vertex.collisionThisTick) 0f else 1f, 1f)
        }

        for (face in spell.faces) {
            RenderHelper.renderLineTriangle(ps, vertexConsumer, face.p0.pos, face.p1.pos, face.p2.pos, 0.8f, 1f, 1f, 1f)
        }

        ps.popPose()
    }
}