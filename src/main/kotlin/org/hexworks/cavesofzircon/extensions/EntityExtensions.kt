package org.hexworks.cavesofzircon.extensions

import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.EntityActions
import org.hexworks.cavesofzircon.attributes.EntityPosition
import org.hexworks.cavesofzircon.attributes.EntityTile
import org.hexworks.cavesofzircon.attributes.flags.BlockOccupier
import org.hexworks.cavesofzircon.attributes.flags.VisionBlocker
import org.hexworks.cavesofzircon.attributes.types.Combatant
import org.hexworks.cavesofzircon.attributes.types.combatStats
import org.hexworks.cavesofzircon.attributes.types.Player
import org.hexworks.cavesofzircon.world.GameContext
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

var AnyGameEntity.position
    get() = tryToFindAttribute(EntityPosition::class).position
    set(value) {
        findAttribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyGameEntity.tile: Tile
    get() = tryToFindAttribute(EntityTile::class).tile

val AnyGameEntity.occupesBlock: Boolean
    get() = findAttribute(BlockOccupier::class).isPresent

val AnyGameEntity.blocksVision: Boolean
    get() = findAttribute(VisionBlocker::class).isPresent

fun AnyGameEntity.tryActionsOn(context: GameContext, target: AnyGameEntity): Response {
    var result: Response = Pass
    findAttribute(EntityActions::class).map {
        it.createActionsfFor(context, this, target).forEach { action ->
            if (target.executeCommand(action) is Consumed) {
                result = Consumed
                return@forEach
            }
        }
    }

    return result
}

fun <T : Attribute> AnyGameEntity.tryToFindAttribute(klass: KClass<T>): T = findAttribute(klass).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${klass.simpleName}'.")
}

val AnyGameEntity.isPlayer: Boolean
    get() = type == Player

fun GameEntity<Combatant>.ifHasNoHealthLeftThen(function: () -> Unit) {
    if (combatStats.hp <= 0) function()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : EntityType> Iterable<AnyGameEntity>.filterType(): List<Entity<T, GameContext>> =
    filter {
        T::class.isSuperclassOf(it.type::class)
    }.toList() as List<Entity<T, GameContext>>


