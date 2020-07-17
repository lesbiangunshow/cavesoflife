package org.hexworks.cavesofzircon.builders

import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.*
import org.hexworks.cavesofzircon.attributes.flags.BlockOccupier
import org.hexworks.cavesofzircon.attributes.flags.VisionBlocker
import org.hexworks.cavesofzircon.attributes.types.*
import org.hexworks.cavesofzircon.commands.Attack
import org.hexworks.cavesofzircon.commands.Dig
import org.hexworks.cavesofzircon.entities.FogOfWar
import org.hexworks.cavesofzircon.systems.*
import org.hexworks.cavesofzircon.world.Game
import org.hexworks.cavesofzircon.world.GameContext
import org.hexworks.cavesofzircon.world.Wanderer
import org.hexworks.zircon.api.GraphicTilesetResources
import org.hexworks.zircon.api.Tiles

fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
    Entities.newEntityOfType(type, init)

object EntityFactory {

    fun newplayer() = newGameEntityOfType(Player) {
        attributes(
            BlockOccupier,
            EntityPosition(),
            EntityTile(GameTileRepository.PLAYER),
            EntityActions(
                Dig::class,
                Attack::class
            ),
            CombatStats.create(
                maxHp = 100,
                attackValue = 10,
                defenseValue = 5
            ),
            Vision(9),
            Inventory(10)
        )

        behaviors(
            InputReceiver
        )
        facets(
            Movable, CameraMover, StairClimber, StairDescender, Attackable, Destructible, ItemPicker
        )
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.WALL),
            VisionBlocker
        )

        facets(
            Diggable
        )
    }

    fun newFogOfWar(game: Game) = FogOfWar(game)

    fun newFungus(fungusSpread: FungusSpread = FungusSpread()) = newGameEntityOfType(Fungus) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.FUNGUS),
            fungusSpread,
            CombatStats.create(
                maxHp = 10,
                attackValue = 0,
                defenseValue = 0
            )
        )

        facets(
            Attackable,
            Destructible
        )

        behaviors(
            FungusGrowth
        )
    }

    fun newStairsDown() = newGameEntityOfType(StairsDown) {
        attributes(
            EntityTile(GameTileRepository.STAIRS_DOWN),
            EntityPosition()
        )
    }

    fun newStairsUp() = newGameEntityOfType(StairsUp) {
        attributes(
            EntityTile(GameTileRepository.STAIRS_UP),
            EntityPosition()
        )
    }

    fun newZircon() = newGameEntityOfType(Zircon) {
        attributes(
            ItemIcon(
                Tiles.newBuilder()
                    .withName("white gem")
                    .withTileset(GraphicTilesetResources.nethack16x16())
                    .buildGraphicTile()
            ),
            EntityPosition(),
            EntityTile(GameTileRepository.ZIRCON)
        )

    }

    fun newBat() = newGameEntityOfType(Bat) {
        attributes(
            BlockOccupier,
            EntityPosition(),
            EntityTile(GameTileRepository.BAT),
            CombatStats.create(
                maxHp = 5,
                attackValue = 2,
                defenseValue = 1
            ),
            EntityActions(
                Attack::class
            )
        )

        facets(
            Movable,
            Attackable,
            Destructible
        )

        behaviors(
            Wanderer
        )
    }
}