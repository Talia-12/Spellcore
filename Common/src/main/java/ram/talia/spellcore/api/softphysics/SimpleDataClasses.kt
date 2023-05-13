package ram.talia.spellcore.api.softphysics

import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.div
import ram.talia.spellcore.api.minus
import ram.talia.spellcore.api.plus
import ram.talia.spellcore.api.times
import kotlin.math.PI
import kotlin.math.atan2


data class Vertex(var pos: Vec3, var vel: Vec3, val mass: Double) {
    var force: Vec3 = Vec3.ZERO

    var collisionThisTick = false

    operator fun plus(other: Vertex): Vec3 = this.pos + other.pos
    operator fun minus(other: Vertex): Vec3 = this.pos - other.pos

    fun applyForce(dT: Double) {
        // TODO RK4
        val acc = force / mass
        vel += acc * dT
        pos += vel * dT - (acc * dT * dT / 2.0)
        force = Vec3.ZERO
    }
}

data class EdgeKey(val p0: Vertex, val p1: Vertex) {
    fun inverse(): EdgeKey = EdgeKey(p1, p0)

    fun strictEquals(other: EdgeKey?): Boolean {
        return (other != null) && other.p0 == this.p0 && other.p1 == this.p1
    }

    override fun equals(other: Any?): Boolean {
        return other != null &&
                other is EdgeKey && ((
                other.p0 == this.p0 &&
                        other.p1 == this.p1
                ) || (
                other.p0 == this.p1 &&
                        other.p1 == this.p0
                )
            )
    }

    override fun hashCode(): Int {
        return p0.hashCode() + p1.hashCode()
    }
}

fun Pair<Vertex, Vertex>.toKey(): EdgeKey = EdgeKey(this.first, this.second)

data class Edge(val p0: Vertex, val p1: Vertex, val restLength: Double) {
    fun toKey(): EdgeKey = EdgeKey(p0, p1)
}

abstract class Face {
    abstract val p0: Vertex
    abstract val p1: Vertex
    abstract val p2: Vertex

    abstract var shape: Shape?
    abstract var outerVolume: Volume?
    abstract var innerVolume: Volume?

    abstract fun inverse(): Face

    fun edges(): List<EdgeKey> = mutableListOf((p0 to p1).toKey(), (p1 to p2).toKey(), (p2 to p0).toKey())

    fun normal(): Vec3 = (p2 - p0).cross(p1 - p0).normalize()

    /**
     * Given an edge, return the vertex of this face not on that edge.
     */
    fun otherPoint(edge: EdgeKey): Vertex {
        return when (edge) {
            (p0 to p1).toKey() -> p2
            (p1 to p0).toKey() -> p2
            (p1 to p2).toKey() -> p0
            (p2 to p1).toKey() -> p0
            (p2 to p0).toKey() -> p1
            (p0 to p2).toKey() -> p1
            else -> { throw IllegalArgumentException("$edge was not one of the edges of $this") }
        }
    }

    /**
     * Takes two faces and the edge between them. returns the [0, 2PI] angle between the faces. Otherwise.
     */
    fun angleTo(other: Face, sharedEdge: EdgeKey): Double {
//        assert(p0 == sharedEdge.first && p1 == sharedEdge.second ||
//                p1 == sharedEdge.first && p2 == sharedEdge.second ||
//                p2 == sharedEdge.first && p0 == sharedEdge.second)
        if (this.inverse() == other)
            return 2*PI

        // TODO: figure out if this is necessary.
        val alignedSharedEdge = if (checkSidedness(sharedEdge)) sharedEdge else (sharedEdge.p1 to sharedEdge.p0).toKey()


        val edgeVec = (alignedSharedEdge.p1 - alignedSharedEdge.p0).normalize()
        val edgeMid = 0.5 * (alignedSharedEdge.p1 + alignedSharedEdge.p0)

        // a point on the plane of this face, forming a vector with the shared edge perpendicular to it
        val thisPointIntermediary = edgeVec.cross(this.normal())
        val thisPoint = (this.otherPoint(sharedEdge).pos - edgeMid).dot(thisPointIntermediary).times(thisPointIntermediary).normalize()
        val otherPointIntermediary = edgeVec.cross(other.normal())
        val otherPoint = (other.otherPoint(sharedEdge).pos - edgeMid).dot(otherPointIntermediary).times(otherPointIntermediary).normalize()

        val dot = thisPoint.dot(otherPoint)
        val det = edgeVec.dot(thisPoint.cross(otherPoint))

        return atan2(det, dot).let { if (it >= 0) it else it + 2*PI }
    }

    /**
     * If the given edge moves with the direction of rotation of the face (assumes the edge is in fact an edge of this face).
     */
    fun checkSidedness(edge: EdgeKey): Boolean = edge in edges()

    override fun equals(other: Any?): Boolean {
        return other != null &&
                other is Face && ((
                other.p0 == this.p0 &&
                        other.p1 == this.p1 &&
                        other.p2 == this.p2
                ) || (
                other.p0 == this.p1 &&
                        other.p1 == this.p2 &&
                        other.p2 == this.p0
                ) || (
                other.p0 == this.p2 &&
                        other.p1 == this.p0 &&
                        other.p2 == this.p1
                )
                )
    }

    override fun hashCode(): Int {
        return p0.hashCode() + p1.hashCode() + p2.hashCode()
    }

    private class ConcreteFace(override val p0: Vertex, override val p1: Vertex, override val p2: Vertex) : Face() {
        override var shape: Shape? = null
        override var outerVolume: Volume? = null
        override var innerVolume: Volume? = null

        private val inverse = InverseFace(this)

        override fun inverse(): Face = inverse
    }

    private class InverseFace(val concreteFace: ConcreteFace) : Face() {
        override val p0: Vertex
            get() = concreteFace.p2
        override val p1: Vertex
            get() = concreteFace.p1
        override val p2: Vertex
            get() = concreteFace.p0
        override var shape: Shape?
            get() = concreteFace.shape
            set(value) { concreteFace.shape = value }
        override var outerVolume: Volume?
            get() = concreteFace.outerVolume
            set(value) { concreteFace.outerVolume = value }
        override var innerVolume: Volume?
            get() = concreteFace.innerVolume
            set(value) { concreteFace.innerVolume = value }

        override fun inverse(): Face = concreteFace
    }

    companion object {
        @JvmStatic
        fun make(p0: Vertex, p1: Vertex, p2: Vertex): Face = ConcreteFace(p0, p1, p2)
    }
}

data class Shape private constructor(val faces: MutableList<Face>) : MutableList<Face> by faces {

    var outerVolume: Volume? = null
    var innerVolume: Volume? = null

    companion object {
        fun startShape(face: Face) = Shape(mutableListOf(face))

        /**
         * Pass a list of [Face]s, and the map of [EdgeKey]s to list of faces generated by calling [Physics.comupteFacesByEdge] on that list of faces,
         * returns the shape containing that list of faces if [areConnected] is true, and null otherwise. (also returns null if the list of faces is empty.)
         */
        fun makeShape(faces: MutableList<Face>, facesByEdge: Map<EdgeKey, List<Face>>): Shape? {
            return if (faces.isNotEmpty() && areConnected(faces, facesByEdge)) Shape(faces) else null
        }

        /**
         * Pass a list of [Face]s, and the map of [EdgeKey]s to list of faces generated by calling [Physics.comupteFacesByEdge] on that list of faces,
         * and returns whether all of those faces can be reached by traversing the surface of the shape.
         */
        fun areConnected(faces: List<Face>, facesByEdge: Map<EdgeKey, List<Face>>): Boolean {
            val undiscoveredFaces = faces.toMutableSet()

            val toExplore = mutableListOf(faces.first())

            while (toExplore.isNotEmpty()) {
                val currentFace = toExplore.removeFirst()

                // find all faces adjacent to the current face that haven't been explored yet, remove them from undiscoveredFaces and add them to toExplore.
                for (edge in currentFace.edges()) {
                    val toAdd = facesByEdge[edge]?.intersect(undiscoveredFaces) ?: throw IllegalArgumentException("facesByEdge didn't contain entry for edge $edge of face $currentFace")
                    undiscoveredFaces.removeAll(toAdd)
                    toExplore.addAll(toAdd)
                }

                if (undiscoveredFaces.isEmpty())
                    return true
            }

            // can only reach this point if the exploration from the first face has run out of new faces to discover, and there are still undiscovered faces.
            return false
        }
    }
}

data class Volume(val adjacentShapes: MutableList<Shape>, var volume: Double, var mass: Double, var temperature: Double)