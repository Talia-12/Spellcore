package ram.talia.spellcore.api.softphysics

import net.minecraft.world.phys.Vec3

data class Vertex(var pos: Vec3, val mass: Float)

data class Edge(val p0: Int, val p1: Int, val restLength: Double)

data class Face(val p0: Int, val p1: Int, val p2: Int)
