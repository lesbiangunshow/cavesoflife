package org.hexworks.cavesofzircon.world

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.GameConfig
import org.hexworks.cavesofzircon.GameConfig.FUNGI_PER_LEVEL
import org.hexworks.cavesofzircon.GameConfig.WORLD_SIZE
import org.hexworks.cavesofzircon.builders.EntityFactory
import org.hexworks.cavesofzircon.extensions.GameEntity
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.impl.Size3D

class GameBuilder(val worldSize: Size3D) {

    companion object {

        fun defaultGame() = GameBuilder(worldSize = WORLD_SIZE)
            .buildGame()
    }

    private val visibleSize = Sizes.create3DSize(
        xLength = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH,
        yLength = GameConfig.WINDOW_HEIGHT - GameConfig.LOG_AREA_HEIGHT,
        zLength = 1
    )

    val world = WorldBuilder(worldSize)
        .makeCaves()
        .build(visibleSize = visibleSize)

    fun buildGame(): Game {

        prepareWorld()

        val player = addPlayer()

        addFungi()

        return Game.create(
            player = player,
            world = world
        )
    }

    private fun prepareWorld() = also {
        world.scrollUpBy(world.actualSize().zLength)
    }

    private fun <T: EntityType> GameEntity<T>.addToWorld(
        atLevel: Int,
        atArea: Size = world.actualSize().to2DSize()
    ) = this.also {
        world.addAtEmptyPosition(
            this,
            offset = Positions.default3DPosition().withZ(atLevel),
            size = Size3D.from2DSize(atArea)
        )
    }

    private fun addPlayer() = EntityFactory.newplayer().addToWorld(
            atLevel = GameConfig.DUNGEON_LEVELS - 1,
            atArea = world.visibleSize().to2DSize()
        )

    private fun addFungi() = also {
        repeat(world.actualSize().zLength) {level ->
            repeat(FUNGI_PER_LEVEL) {
                EntityFactory.newFungus().addToWorld(level)
            }
        }
    }
}