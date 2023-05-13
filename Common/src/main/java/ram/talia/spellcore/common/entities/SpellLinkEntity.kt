package ram.talia.spellcore.common.entities

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import ram.talia.spellcore.api.softphysics.Face
import ram.talia.spellcore.api.softphysics.Vertex

class SpellLinkEntity(entityType: EntityType<out SpellLinkEntity>, level: Level) : Entity(entityType, level) {

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