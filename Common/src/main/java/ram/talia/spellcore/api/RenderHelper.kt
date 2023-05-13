package ram.talia.spellcore.api

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.softphysics.Face

object RenderHelper {
    fun renderLineFace(ps: PoseStack, vertexConsumer: VertexConsumer, face: Face, r: Float, g: Float, b: Float, alpha: Float) {
        renderLineTriangle(ps, vertexConsumer, face.p0.pos, face.p1.pos, face.p2.pos, r, g, b, alpha)
    }

    fun renderLineTriangle(
        ps: PoseStack,
        vertexConsumer: VertexConsumer,
        p0: Vec3,
        p1: Vec3,
        p2: Vec3,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float,
    ) {
        val pose = ps.last().pose()
        val normal = ps.last().normal()
        val p0x = p0.x.toFloat()
        val p0y = p0.y.toFloat()
        val p0z = p0.z.toFloat()
        val p1x = p1.x.toFloat()
        val p1y = p1.y.toFloat()
        val p1z = p1.z.toFloat()
        val p2x = p2.x.toFloat()
        val p2y = p2.y.toFloat()
        val p2z = p2.z.toFloat()

        vertexConsumer.vertex(pose, p0x, p0y, p0z).color(r, g, b, alpha).normal(normal, 1.0f, 0.0f, 0.0f).endVertex()
        vertexConsumer.vertex(pose, p1x, p1y, p1z).color(r, g, b, alpha).normal(normal, 1.0f, 0.0f, 0.0f).endVertex()
        vertexConsumer.vertex(pose, p1x, p1y, p1z).color(r, g, b, alpha).normal(normal, 1.0f, 0.0f, 0.0f).endVertex()
        vertexConsumer.vertex(pose, p2x, p2y, p2z).color(r, g, b, alpha).normal(normal, 1.0f, 0.0f, 0.0f).endVertex()
        vertexConsumer.vertex(pose, p2x, p2y, p2z).color(r, g, b, alpha).normal(normal, 1.0f, 0.0f, 0.0f).endVertex()
        vertexConsumer.vertex(pose, p0x, p0y, p0z).color(r, g, b, alpha).normal(normal, 1.0f, 0.0f, 0.0f).endVertex()
    }

    fun renderPoint(ps: PoseStack, vertexConsumer: VertexConsumer, p: Vec3, radius: Double, r: Float, g: Float, b: Float, alpha: Float) {
        LevelRenderer.renderLineBox(
            ps,
            vertexConsumer,
            p.x - radius,
            p.y - radius,
            p.z - radius,
            p.x + radius,
            p.y + radius,
            p.z + radius,
            r, g, b, alpha, r, g, b
        )
    }
}