package org.hexworks.cavesofzircon.world

import org.hexworks.cavesofzircon.blocks.GameBlock
import org.hexworks.cavesofzircon.extensions.sameLevelNeighborsShuffled
import org.hexworks.cavesofzircon.builders.GameBlockFactory
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import kotlin.random.Random

class WorldBuilder(private val worldSize: Size3D) {

    private val width = worldSize.xLength
    private val height = worldSize.yLength
    private val depth = worldSize.zLength
    private var blocks = mutableMapOf<Position3D, GameBlock>()

    fun makeCaves() = randomizeTiles().smooth(8).connectLevels()

    fun build(visibleSize: Size3D) =
        World(blocks, visibleSize, worldSize)

    private fun generateRandomFloorPositionsOn(level: Int) = sequence {
        while (true) {
            var position = Position3D.unknown()
            while (position.isUnknown()) {
                Positions.create3DPosition(
                    x = Random.nextInt(width - 1),
                    y = Random.nextInt(height - 1),
                    z = level
                ).let {
                    if (blocks[it].isEmptyFloor()) position = it
                }
            }

            yield(position)
        }
    }

    private fun GameBlock?.isEmptyFloor() = this?.isEmptyFloor ?: false

    private fun Position3D.below() = copy(z = z - 1)

    private fun connectRegionDown(currentLevel: Int) {
        generateRandomFloorPositionsOn(currentLevel)
            .first {
                blocks[it].isEmptyFloor() && blocks[it.below()].isEmptyFloor()
            }.let {
                blocks[it] = GameBlockFactory.stairsDown()
                blocks[it.below()] = GameBlockFactory.stairsUp()
            }
    }

    private fun connectLevels() = also {
        (depth - 1).downTo(1).forEach(::connectRegionDown)
    }

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
                        if (it.isEmptyFloor) {
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