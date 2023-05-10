package ram.talia.spellcore.api.softphysics

import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.plus

object IcosphereGenerator {
    private const val X: Double = 0.5257311121191336
    private const val Z: Double = 0.8506508083520399
    private const val N: Double = 0.0

    private val baseVertices: List<Vec3> = listOf(
        Vec3(-X, N, Z), Vec3( X, N, Z), Vec3(-X, N,-Z), Vec3( X, N,-Z),
        Vec3( N, Z, X), Vec3( N, Z,-X), Vec3( N,-Z, X), Vec3( N,-Z,-X),
        Vec3( Z, X, N), Vec3(-Z, X, N), Vec3( Z,-X, N), Vec3(-Z,-X, N)
    )

    private val baseTriangles: List<Triple<Int, Int, Int>> = listOf(
        Triple(0,4,1), Triple(0,9,4), Triple(9,5,4), Triple(4,5,8), Triple(4,8,1),
        Triple(8,10,1), Triple(8,3,10), Triple(5,3,8), Triple(5,2,3), Triple(2,7,3),
        Triple(7,10,3), Triple(7,6,10), Triple(7,11,6), Triple(11,0,6), Triple(0,1,6),
        Triple(6,1,10), Triple(9,0,11), Triple(9,11,2), Triple(9,2,5), Triple(7,2,11)
    )

    private val baseEdges: List<Pair<Int, Int>> = edgesFromTriangles(baseTriangles)

    /**
     * Generate the edge vertices and face indices of an icosphere of size [n].
     */
    fun icosphere(n: Int): Triple<List<Vec3>, List<Pair<Int, Int>>, List<Triple<Int, Int, Int>>> {
        val vertices = baseVertices.toMutableList()
        var edges = baseEdges
        var triangles = baseTriangles

        repeat(n) {
            val subdividedLines = mutableMapOf<Pair<Int, Int>, Int>()
            val newEdges = mutableListOf<Pair<Int, Int>>()
            val newTriangles = mutableListOf<Triple<Int, Int, Int>>()

            for (tri in triangles) {
                val a = getMiddlePoint(tri.first, tri.second, subdividedLines, (it == n - 1), newEdges, vertices)
                val b = getMiddlePoint(tri.second, tri.third, subdividedLines, (it == n - 1), newEdges, vertices)
                val c = getMiddlePoint(tri.third, tri.first,  subdividedLines, (it == n - 1), newEdges, vertices)

                newTriangles.add(Triple(tri.first,  a, c))
                newTriangles.add(Triple(tri.second, b, a))
                newTriangles.add(Triple(tri.third,  c, b))
                newTriangles.add(Triple(a, b, c))
            }

            edges = newEdges
            triangles = newTriangles
        }

        return Triple(vertices, edges, triangles)
    }

    private fun getMiddlePoint(p0: Int, p1: Int, subdividedLines: MutableMap<Pair<Int, Int>, Int>, shouldAdd: Boolean, newEdges: MutableList<Pair<Int, Int>>, vertices: MutableList<Vec3>): Int {
        return subdividedLines[p0 to p1] ?: let {
            val mid = (vertices[p0] + vertices[p1]).normalize()
            val midIdx = vertices.size
            if (shouldAdd)
                newEdges.add(p0 to p1)
            vertices.add(mid)
            subdividedLines[p0 to p1] = midIdx
            midIdx
        }
    }

    private fun edgesFromTriangles(triangles: List<Triple<Int, Int, Int>>): List<Pair<Int, Int>> {
        val edges = mutableListOf<Pair<Int, Int>>()

        for (triangle in triangles) {
            if (!edges.contains(triangle.first to triangle.second) && !edges.contains(triangle.second to triangle.first))
                edges.add(triangle.first to triangle.second)
            if (!edges.contains(triangle.second to triangle.third) && !edges.contains(triangle.third to triangle.second))
                edges.add(triangle.second to triangle.third)
            if (!edges.contains(triangle.third to triangle.first) && !edges.contains(triangle.first to triangle.third))
                edges.add(triangle.third to triangle.first)
        }

        return edges
    }
}