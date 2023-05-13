package ram.talia.spellcore.common.entities

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.*
import ram.talia.spellcore.api.softphysics.*
import ram.talia.spellcore.common.lib.SpellcoreEntities
import java.util.UUID

class SpellEntity(entityType: EntityType<out SpellEntity>, level: Level) : Entity(entityType, level) {

    val vertices: MutableList<Vertex> = mutableListOf()
    val edges: MutableList<Edge> = mutableListOf()
    val faces: MutableList<Face> = mutableListOf()

    var facesByEdge: Map<EdgeKey, List<Face>> = mutableMapOf()
    var volumes: Map<List<Face>, Volume> = mutableMapOf()

    val links: MutableMap<Vertex, TypedUUID<SpellLinkEntity>> = mutableMapOf()

    constructor(level: Level): this(SpellcoreEntities.SPELL_ENTITY, level)

    init {
        val (newVertices, newEdges, newFaces) = IcosphereGenerator.icosphere(2)
        vertices.addAll(newVertices.map { Vertex(it, Vec3.ZERO, 0.1) })
        edges.addAll(newEdges.map { Edge(vertices[it.first], vertices[it.second], (vertices[it.first].pos - vertices[it.second].pos).length()) })
        faces.addAll(newFaces.map { Face.make(vertices[it.first], vertices[it.second], vertices[it.third]) })

        facesByEdge = Physics.comupteFacesByEdge(faces)
        recomputeVolumes()

    }

    override fun tick() {
        if (firstTick) {
            Physics.addSpellEntity(this.level, this)
            SpellcoreAPI.LOGGER.info("first!")
            SpellcoreAPI.LOGGER.info(uuid)
        }

        super.tick()

        Physics.runPhysicsTick(this.level)

        if (this.level.isClientSide)
            clientTick()
    }

    fun clientTick() {
//        val thisX = this.position().x
//        val thisY = this.position().y
//        val thisZ = this.position().z
//
//        for (vertex in vertices) {
//            level.addParticle(
//                ParticleTypes.BUBBLE,
//                (thisX + vertex.pos.x),
//                (thisY + vertex.pos.y),
//                (thisZ + vertex.pos.z),
//                0.0125, // * (random.nextDouble() - 0.5),
//                0.0125, // * (random.nextDouble() - 0.5),
//                0.0125, // * (random.nextDouble() - 0.5)
//            )
//        }
    }

    fun recenter(override: Boolean) {
        if (vertices.size == 0)
            return

        var centre = Vec3.ZERO

        for (vertex in vertices)
            centre += vertex.pos

        centre /= vertices.size.toDouble()

        for (vertex in vertices)
            vertex.pos -= centre

        if (override)
            setPos(centre)
        else
            setPos(position() + centre)
    }

    fun recomputeVolumes() {
        // TODO
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        // TODO
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        // TODO
    }

    override fun defineSynchedData() {
        // TODO
    }

    override fun getAddEntityPacket(): Packet<*> {
        return ClientboundAddEntityPacket(this)
    }

    data class SpellUUID private constructor(val uuid: UUID) {
        constructor(spell: SpellEntity) : this(spell.uuid)
    }
}