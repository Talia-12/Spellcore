package ram.talia.spellcore.api.softphysics

import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.div
import ram.talia.spellcore.api.minus
import ram.talia.spellcore.api.plus
import ram.talia.spellcore.api.times

data class Vertex(var pos: Vec3, var vel: Vec3, val mass: Double) {
    var force: Vec3 = Vec3.ZERO

    fun applyForce(dT: Double) {
        // TODO RK4
        var acc = force / mass
        vel += acc * dT
        pos += vel * dT - (acc * dT * dT / 2.0)
        force = Vec3.ZERO
    }
}

data class Edge(val p0: Vertex, val p1: Vertex, val restLength: Double)

data class Face(val p0: Vertex, val p1: Vertex, val p2: Vertex)
