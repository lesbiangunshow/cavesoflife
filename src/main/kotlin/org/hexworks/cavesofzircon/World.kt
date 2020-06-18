package org.hexworks.cavesofzircon

import org.hexworks.cavesofzircon.factories.GameBlockFactory
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea

class World(
    startingBlocks: Map<Position3D, GameBlock>,
    visibleSize: Size3D,
    actualSize: Size3D
) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
    .withVisibleSize(visibleSize)
    .withActualSize(actualSize)
    .withDefaultBlock(DEFAULT_BLOCK)
    .withLayersPerBlock(1)
    .build() {

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }

    init {
        startingBlocks.forEach { (pos, block) ->
            setBlockAt(pos, block)
        }
    }
}