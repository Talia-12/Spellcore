package ram.talia.spellcore.api.softphysics

import com.google.common.math.IntMath.pow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ram.talia.spellcore.api.softphysics.IcosphereGenerator.icosphere


class IcosphereGeneratorTest {

    @Test
    fun testIcosphere() {
        println(icosphere(0))
        println(icosphere(1))
        println(icosphere(2))

        repeat(8) {
            println(it)
            val (vertices, edges, triangles) = icosphere(it)
            assertEquals(20 * pow(4, it) - 8, vertices.size)
            assertEquals(15 * pow(4, it) + if (it == 0) 15 else 0, edges.size)
            assertEquals(20 * pow(4, it), triangles.size)
        }
    }
}