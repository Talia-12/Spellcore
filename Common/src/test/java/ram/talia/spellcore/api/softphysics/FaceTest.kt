package ram.talia.spellcore.api.softphysics

import net.minecraft.world.phys.Vec3
import org.junit.jupiter.api.Test

class FaceTest {

    /*
     * 19	1	23	70	52
     * 61	56	73	84	13
     * 16	49	21	60	1
     * 76	36	48	56	61
     * 26	23	81	20	100
     * 48	24	90	58	45
     * 34	34	82	97	76
     * 23	59	40	86	81
     * 44	74	16	73	63
     * 6	85	6	1	100
     * 74	19	84	65	5
     * 7	74	28	57	90
     * 95	42	80	20	20
     * 13	2	21	25	53
     * 27	94	41	79	81
     * 62	45	92	79	74
     * 15	80	1	46	49
     * 74	18	15	89	44
     * 79	62	70	44	19
     * 54	22	69	86	40
     */

    @Test
    fun normal() {
        val p0 = Vertex(Vec3(43.0, 85.0, 19.0), Vec3.ZERO, 1.0)
        val p1 = Vertex(Vec3(1.0, 23.0, 70.0), Vec3.ZERO, 1.0)
        val p2 = Vertex(Vec3(61.0, 52.0, 56.0), Vec3.ZERO, 1.0)

        val face = Face(p0, p1, p2)

        val expected = Vec3(611.0, -2472.0, -2502.0).normalize()
        val normal = face.normal()

        assert(expected.distanceTo(normal) < 0.00001)
    }

    @Test
    fun angleTo() {
        val p0 = Vertex(Vec3(43.0, 85.0, 19.0), Vec3.ZERO, 1.0)
        val p1 = Vertex(Vec3(1.0, 23.0, 70.0), Vec3.ZERO, 1.0)
        val p2 = Vertex(Vec3(61.0, 52.0, 56.0), Vec3.ZERO, 1.0)
        val p3 = Vertex(Vec3(85.0, 6.0, 1.0), Vec3.ZERO, 1.0)

        val face0 = Face(p0, p1, p2)
        val face1 = Face(p1, p3, p2)

        val face2 = Face(p2, p1, p0)
        val face3 = Face(p2, p3, p1)

        val angleTo0 = face0.angleTo(face1, p1 to p2)
        val angleTo1 = face2.angleTo(face3, p2 to p1)

        val angleToSelf = face0.angleTo(face0, p1 to p2)
        val angleToInverse = face0.angleTo(face2, p1 to p2)
        println(angleTo0)
        println(angleTo1)
        println(angleTo0 + angleTo1)

        println(angleToSelf)
        println(angleToInverse) // TODO: currently having the problem where this is pi, not 2pi.
    }
}