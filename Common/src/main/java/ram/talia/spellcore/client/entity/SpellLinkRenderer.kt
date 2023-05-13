package ram.talia.spellcore.client.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.*
import ram.talia.spellcore.api.softphysics.Face
import ram.talia.spellcore.api.softphysics.Vertex
import ram.talia.spellcore.api.softphysics.toKey
import ram.talia.spellcore.common.entities.SpellLinkEntity

class SpellLinkRenderer(context: EntityRendererProvider.Context) : EntityRenderer<SpellLinkEntity>(context) {
    private val SPELL: ResourceLocation = SpellcoreAPI.modLoc("textures/entity/spell.png")

    override fun getTextureLocation(spellLink: SpellLinkEntity): ResourceLocation = SPELL

    override fun render(spellLink: SpellLinkEntity, yaw: Float, partialTicks: Float, ps: PoseStack, bufferSource: MultiBufferSource, packedLight: Int) {
        val p0 = Vertex(Vec3(0.0, 0.5, 0.0), Vec3.ZERO, 1.0)
        val p1 = Vertex(Vec3(0.0, -0.5, 0.0), Vec3.ZERO, 1.0)
        val p2 = Vertex(Vec3(0.0, -0.5, 1.0), Vec3.ZERO, 1.0)
        val p3 = Vertex(Vec3(-1.0, -0.5, 0.0), Vec3.ZERO, 1.0)

        val face0 = Face.make(p0, p1, p2)
        val face1 = Face.make(p3, p2, p1)

        val vertexConsumer: VertexConsumer = bufferSource.getBuffer(RenderType.lines())

        RenderHelper.renderPoint(ps, vertexConsumer, p0.pos, SpellRenderer.RADIUS, 1f, 0f, 0f, 1f)
        RenderHelper.renderPoint(ps, vertexConsumer, p1.pos, SpellRenderer.RADIUS, 0f, 1f, 0f, 1f)
        RenderHelper.renderPoint(ps, vertexConsumer, p2.pos, SpellRenderer.RADIUS, 0f, 0f, 1f, 1f)
        RenderHelper.renderPoint(ps, vertexConsumer, p3.pos, SpellRenderer.RADIUS, 0f, 0.7f, 0.7f, 1f)

        RenderHelper.renderLineFace(ps, vertexConsumer, face0, 0.7f, 0.7f, 0f, 1f)
        RenderHelper.renderLineFace(ps, vertexConsumer, face1, 0.7f, 0f, 0.7f, 1f)

        val sharedEdge = (p1 to p2).toKey()

        val alignedSharedEdge = if (sharedEdge in face0.edges()) sharedEdge else (sharedEdge.p1 to sharedEdge.p0).toKey()

        val edgeVec = (alignedSharedEdge.p1 - alignedSharedEdge.p0).normalize()
        val edgeMid = 0.5 * (alignedSharedEdge.p1 + alignedSharedEdge.p0)

        // a point on the plane of this face, forming a vector with the shared edge perpendicular to it
        val thisPointIntermediary = edgeVec.cross(face0.normal())
        val thisPoint = (face0.otherPoint(sharedEdge).pos - edgeMid).dot(thisPointIntermediary).times(thisPointIntermediary).normalize()
        val otherPointIntermediary = edgeVec.cross(face1.normal())
        val otherPoint = (face1.otherPoint(sharedEdge).pos - edgeMid).dot(otherPointIntermediary).times(otherPointIntermediary).normalize()

        RenderHelper.renderPoint(ps, vertexConsumer, edgeMid + thisPoint, SpellRenderer.RADIUS, 0.7f, 0.7f, 0f, 1f)
        RenderHelper.renderPoint(ps, vertexConsumer, edgeMid + otherPoint, SpellRenderer.RADIUS, 0.7f, 0f, 0.7f, 1f)
    }
}