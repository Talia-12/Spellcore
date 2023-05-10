package ram.talia.spellcore.common.entities

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import ram.talia.spellcore.api.softphysics.Edge
import ram.talia.spellcore.api.softphysics.Face
import ram.talia.spellcore.api.softphysics.IcosphereGenerator
import ram.talia.spellcore.api.softphysics.Vertex
import ram.talia.spellcore.common.lib.SpellcoreEntities

class SpellEntity(entityType: EntityType<out SpellEntity>, level: Level) : Entity(entityType, level) {

    val vertices: MutableList<Vertex> = mutableListOf()
    val edges: MutableList<Edge> = mutableListOf()
    val faces: MutableList<Face> = mutableListOf()

    constructor(level: Level): this(SpellcoreEntities.SPELL_ENTITY, level)

    init {
        val (newVertices, newEdges, newFaces) = IcosphereGenerator.icosphere(2)
        vertices.addAll(newVertices.map { Vertex(it, 0.1f) })
        faces.addAll(newFaces.map { Face(it.first, it.second, it.third) })
    }

    override fun tick() {
        super.tick()

        if (this.level.isClientSide)
            this.clientTick()
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
}