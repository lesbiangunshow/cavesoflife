package org.hexworks.cavesofzircon.world

import org.hexworks.cavesofzircon.GameConfig
import org.hexworks.cavesofzircon.GameConfig.WORLD_SIZE
import org.hexworks.cavesofzircon.builders.EntityFactory
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
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

        return Game.create(
            player = player,
            world = world
        )
    }

    private fun prepareWorld() = also {
        world.scrollUpBy(world.actualSize().zLength)
    }

    private fun addPlayer() = EntityFactory.newplayer().also {
        world.addAtEmptyPosition(
            it,
            offset = Positions.default3DPosition().withZ(GameConfig.DUNGEON_LEVELS - 1),
            size = world.visibleSize().copy(zLength = 0)
        )
    }
}