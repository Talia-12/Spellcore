package ram.talia.spellcore.api.softphysics

import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.plus
import ram.talia.spellcore.api.times
import ram.talia.spellcore.common.entities.SpellEntity

object Physics {

    val spellEntitiesByLevel: MutableMap<Level, MutableList<SpellEntity.SpellUUID>> = mutableMapOf()

    fun addSpellEntity(level: Level, spell: SpellEntity) {
        spellEntitiesByLevel.computeIfAbsent(level) { _ -> mutableListOf() }.add(SpellEntity.SpellUUID(spell))
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
                allEdges.addAll(spell.internalEdges)
                allFaces.addAll(spell.internalFaces)

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
}