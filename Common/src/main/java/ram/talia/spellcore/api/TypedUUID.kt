package ram.talia.spellcore.api

import net.minecraft.world.entity.Entity
import java.util.*

data class TypedUUID<T : Entity> private constructor(val uuid: UUID) {
    constructor(entity: T) : this(entity.uuid)
}