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

    @JvmStatic
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

    /**
     * Takes the list of faces, and the map of edge keys to which faces are adjacent to that edge key
     */
    fun computeNextFaceByFaceAndEdge(faces: List<Face>, facesByEdge: Map<EdgeKey, List<Face>>): Map<Pair<Face, EdgeKey>, Face> {
        val out = mutableMapOf<Pair<Face, EdgeKey>, Face>()

        for (face in faces) {
            for (edge in face.edges()) {
                val others = facesByEdge[edge]?.toMutableList()?.apply { remove(face) } ?: throw IllegalArgumentException("facesByEdge must contain edge for face $face")

                val traverseTo = when (others.size) {
                    0 -> face.inverse() to face
                    1 -> {
                        val other = others.first()
                        other to other.inverse()
                    }
                    else -> {
                        val angles = others.map { it to face.angleTo(it, edge) }

                        var min = angles.first()
                        var max = angles.first()

                        for ((other, angle) in angles) {
                            if (angle < min.second)
                                min = other to angle
                            else if (angle > max.second)
                                max = other to angle
                        }

                        min.first to max.first.inverse()
                    }
                }

                out[face to edge] = traverseTo.first
                out[face.inverse() to edge.inverse()] = traverseTo.second
            }
        }

        return out
    }

    fun computeVolumes(vertices: List<Vertex>, edges: List<Edge>, faces: List<Face>, faceLinks: Map<Pair<Face, EdgeKey>, Face>): Pair<MutableList<Shape>, MutableList<Volume>> {
        // the list of all unassigned faces; stored as a pair for the face and the side of the face, since each side of a face
        // can be adjacent to a separate volume.
        val unassignedFaces = faces.flatMap { listOf(it, it.inverse()) }.toMutableList()
        val shapes: MutableList<Shape> = mutableListOf()
        val unmergedVolumes: MutableList<Volume> = mutableListOf()

        while (unassignedFaces.isNotEmpty()) {
            val seedFace = unassignedFaces.removeFirst()

            val shape = Shape.startShape(seedFace)
            val volume = Volume(mutableListOf(), 0.0, 0.0, 0.0)

            shapes.add(shape)
            volume.adjacentShapes.add(shape)
            unmergedVolumes.add(volume)

            val unexplored = seedFace.edges().map { seedFace to it }.toMutableList()

            while (unexplored.isNotEmpty()) {
                val (face, edge) = unexplored.removeFirst()
                val next = faceLinks[face to edge] ?: throw IllegalArgumentException("${face to edge} not in $faceLinks")

                // if next was in fact still unassigned, add it to the shape being built.
                if (unassignedFaces.remove(next)) {
                    unexplored.addAll(next.edges().mapNotNull { if (it != edge) next to it else null })
                    shape.add(next)
                    next.shape = shape
                    next.outerVolume = volume
                }
            }
        }

        // TODO: figure out which volumes are contained inside others. raycast along the (1,0,0) direction. on encountering a face, check its orientation. If it is correctly oriented
        // TODO: contain things, its "internal" volume is the current volume's "external" volume. Otherwise, continue the raycast onwards, and ignore any collisions with faces that
        // TODO: contain the same volume as that collision.

        TODO("Not yet implemented.")
    }
}