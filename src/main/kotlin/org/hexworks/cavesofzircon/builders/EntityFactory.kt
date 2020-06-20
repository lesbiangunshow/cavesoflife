package org.hexworks.cavesofzircon.builders

import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.GameTileRepository
import org.hexworks.cavesofzircon.attributes.EntityActions
import org.hexworks.cavesofzircon.attributes.EntityPosition
import org.hexworks.cavesofzircon.attributes.EntityTile
import org.hexworks.cavesofzircon.attributes.flags.BlockOccupier
import org.hexworks.cavesofzircon.attributes.types.Player
import org.hexworks.cavesofzircon.attributes.types.Wall
import org.hexworks.cavesofzircon.commands.Dig
import org.hexworks.cavesofzircon.systems.CameraMover
import org.hexworks.cavesofzircon.systems.Diggable
import org.hexworks.cavesofzircon.systems.InputReceiver
import org.hexworks.cavesofzircon.systems.Movable
import org.hexworks.cavesofzircon.world.GameContext

fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
    Entities.newEntityOfType(type, init)

object EntityFactory {

    fun newplayer() = newGameEntityOfType(Player) {
        attributes(
            EntityPosition(),
            EntityTile(GameTileRepository.PLAYER),
            EntityActions(
                Dig::class
            )
        )

        behaviors(
            InputReceiver
        )
        facets(
            Movable, CameraMover
        )
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.WALL)
        )

        facets(
            Diggable
        )
    }
}