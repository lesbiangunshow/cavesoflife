package org.hexworks.cavesofzircon.world

import org.hexworks.cavesofzircon.blocks.GameBlock
import org.hexworks.cavesofzircon.extensions.sameLevelNeighborsShuffled
import org.hexworks.cavesofzircon.factories.GameBlockFactory
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class WorldBuilder(private val worldSize: Size3D) {

    private val width = worldSize.xLength
    private val height = worldSize.zLength
    private var blocks = mutableMapOf<Position3D, GameBlock>()

    fun makeCaves() = randomizeTiles().smooth(8)

    fun build(visibleSize: Size3D) =
        World(blocks, visibleSize, worldSize)

    private fun randomizeTiles(): WorldBuilder {
        forAllPositions {
            blocks[it] = if (Math.random() < 0.5) {
                GameBlockFactory.floor()
            } else GameBlockFactory.wall()
        }

        return this
    }

    private fun smooth(iterations: Int): WorldBuilder {
        val newBlocks = mutableMapOf<Position3D, GameBlock>()
        repeat(iterations) {
            forAllPositions { pos ->
                val (x, y, z) = pos
                var floors = 0
                var rocks = 0
                pos.sameLevelNeighborsShuffled().plus(pos).forEach { neighbor ->
                    blocks.whenPresent(neighbor) {
                        if (it.isFloor) {
                            floors++
                        } else rocks++
                    }
                }

                newBlocks[Positions.create3DPosition(x, y, z)] =
                    if (floors >= rocks) GameBlockFactory.floor() else GameBlockFactory.wall()
            }
            blocks = newBlocks
        }
        return this
    }

    private fun forAllPositions(function: (Position3D) -> Unit) = worldSize.fetchPositions().forEach(function)

    private fun MutableMap<Position3D, GameBlock>.whenPresent(position: Position3D, function: (GameBlock) -> Unit) {
        get(position)?.let(function)
    }
}