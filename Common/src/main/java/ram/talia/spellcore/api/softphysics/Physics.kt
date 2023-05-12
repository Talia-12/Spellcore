package ram.talia.spellcore.api.softphysics

import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.TypedUUID
import ram.talia.spellcore.api.plus
import ram.talia.spellcore.api.times
import ram.talia.spellcore.common.entities.SpellEntity

object Physics {
    const val OUTER_PRESSURE = 4.0
    const val OUTER_TEMPERATURE = 4.0

    val spellEntitiesByLevel: MutableMap<Level, MutableList<TypedUUID<SpellEntity>>> = mutableMapOf()

    fun addSpellEntity(level: Level, spell: SpellEntity) {
        spellEntitiesByLevel.computeIfAbsent(level) { _ -> mutableListOf() }.add(TypedUUID(spell))
    }

    fun runPhysicsTick() {
        for ((level, spellEntityUUIDs) in spellEntitiesByLevel) {
            val spellEntities = mutableListOf<SpellEntity>()
            val allVertices = mutableListOf<Vertex>()
            val allEdges = mutableListOf<Edge>()
            val allFaces = mutableListOf<Face>()

            for (uuid in spellEntityUUIDs) {
                val spell = level.entities.get(uuid.uuid) as? SpellEntity ?: continue
                spellEntities.add(spell)

                allVertices.addAll(spell.vertices)
                allEdges.addAll(spell.edges)
                allFaces.addAll(spell.faces)

//                for ((otherUUID, edges) in spell.interSpellEdges) {
//                    level.entities.get(otherUUID.uuid) as? SpellEntity ?: continue
//                    allEdges.addAll(edges)
//                }
//                for ((otherUUID, faces) in spell.interSpellFaces) {
//                    level.entities.get(otherUUID.uuid) as? SpellEntity ?: continue
//                    allFaces.addAll(faces)
//                }
            }

            physicsStep(allVertices, allEdges, allFaces, 0.05, level)

            for (entity in spellEntities)
                entity.recenter()
        }
    }

    fun physicsStep(vertices: MutableList<Vertex>, edges: MutableList<Edge>, faces: MutableList<Face>, dT: Double, level: Level) {

        for (vertex in vertices) {
            vertex.force += vertex.mass * Vec3(0.0, -3.0, 0.0)
        }



        for (vertex in vertices) {
            vertex.applyForce(dT)
        }
    }

    fun comupteFacesByEdge(faces: List<Face>): Map<EdgeKey, List<Face>> {
        val facesByEdge: MutableMap<EdgeKey, MutableList<Face>> = mutableMapOf()

        for (face in faces) {
            for (edge in face.edges()) {
                facesByEdge.computeIfAbsent(edge) { _ -> mutableListOf() }.add(face)
            }
        }

        return facesByEdge
    }

    fun computeVolumes(vertices: List<Vertex>, edges: List<Edge>, faces: List<Face>): MutableList<MutableList<Face>> {
        val volumesAssigned: MutableMap<Face, Int> = mutableMapOf()
        val unassignedFaces = faces.toMutableList()
        val volumes: MutableList<MutableList<Face>> = mutableListOf()

        while (unassignedFaces.isNotEmpty()) {
            val seedFace = unassignedFaces.first()

            // if this is true, start assigning
            val side = volumesAssigned[seedFace] == null

            val volume = mutableListOf(seedFace)

            // TODO: get all faces that should be assigned to that volume and add them.

            for (face in volume) {
                volumesAssigned.merge(face, if (side) 1 else 2) { a, b -> a + b }
            }
        }

        // TODO: figure out which volumes are contained inside others. raycast along the (1,0,0) direction. on encountering a face, check its orientation. If it is correctly oriented
        // TODO: contain things, its "internal" volume is the current volume's "external" volume. Otherwise, continue the raycast onwards, and ignore any collisions with faces that
        // TODO: contain the same volume as that collision.

        TODO("Not yet implemented.")
    }
}