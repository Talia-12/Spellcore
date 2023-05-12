package ram.talia.spellcore.common.entities

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.SpellcoreAPI
import ram.talia.spellcore.api.div
import ram.talia.spellcore.api.minus
import ram.talia.spellcore.api.plus
import ram.talia.spellcore.api.softphysics.*
import ram.talia.spellcore.common.lib.SpellcoreEntities
import java.util.UUID

class SpellEntity(entityType: EntityType<out SpellEntity>, level: Level) : Entity(entityType, level) {

    val vertices: MutableList<Vertex> = mutableListOf()
    val internalEdges: MutableList<Edge> = mutableListOf()
    val internalFaces: MutableList<Face> = mutableListOf()
//    val interSpellEdges: MutableMap<SpellUUID, MutableList<Edge>> = mutableMapOf()
//    val interSpellFaces: MutableMap<SpellUUID, MutableList<Face>> = mutableMapOf()

    constructor(level: Level): this(SpellcoreEntities.SPELL_ENTITY, level)

    init {
        val (newVertices, newEdges, newFaces) = IcosphereGenerator.icosphere(2)
        vertices.addAll(newVertices.map { Vertex(it, Vec3.ZERO, 0.1) })
        internalEdges.addAll(newEdges.map { Edge(vertices[it.first], vertices[it.second], (vertices[it.first].pos - vertices[it.second].pos).length()) })
        internalFaces.addAll(newFaces.map { Face(vertices[it.first], vertices[it.second], vertices[it.third]) })

        SpellcoreAPI.LOGGER.debug(this.level)
        Physics.addSpellEntity(this.level, this)
    }

    override fun tick() {
        super.tick()

        if (this.level.isClientSide)
            clientTick()
    }

    fun clientTick() {
        val thisX = this.position().x
        val thisY = this.position().y
        val thisZ = this.position().z

        for (vertex in vertices) {
            level.addParticle(
                ParticleTypes.BUBBLE,
                (thisX + vertex.pos.x),
                (thisY + vertex.pos.y),
                (thisZ + vertex.pos.z),
                0.0125, // * (random.nextDouble() - 0.5),
                0.0125, // * (random.nextDouble() - 0.5),
                0.0125, // * (random.nextDouble() - 0.5)
            )
        }
    }

    fun recenter() {
        if (vertices.size == 0)
            return

        var centre = Vec3.ZERO

        for (vertex in vertices)
            centre += vertex.pos

        centre /= vertices.size.toDouble()

        for (vertex in vertices)
            vertex.pos -= centre

        setPos(position() + centre)
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