package org.hexworks.cavesofzircon.world

import com.sun.corba.se.spi.orbutil.fsm.StateEngine
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Engines
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.blocks.GameBlock
import org.hexworks.cavesofzircon.extensions.GameEntity
import org.hexworks.cavesofzircon.extensions.position
import org.hexworks.cavesofzircon.builders.GameBlockFactory
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.fold
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent

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

    private val engine: Engine<GameContext> = Engines.newEngine()

    init {
        startingBlocks.forEach { (position, block) ->
            setBlockAt(position, block)
            block.entities.forEach {
                engine.addEntity(it)
                it.position = position
            }
        }
    }

    fun update(screen: Screen, uiEvent: UIEvent, game: Game) {
        engine.update(
            GameContext(
                world = this,
                screen = screen,
                uiEvent = uiEvent,
                player = game.player
            )
        )
    }

    fun addEntity(
        entity: Entity<EntityType, GameContext>,
        position: Position3D
    ) {
        entity.position = position
        engine.addEntity(entity)
        fetchBlockAt(position).map {
            it.addEntity(entity)
        }
    }

    fun removeEntity(entity: Entity<EntityType, GameContext>) {
        fetchBlockAt(entity.position).map {
            it.removeEntity(entity)
        }

        engine.removeEntity(entity)
        entity.position = Position3D.unknown()
    }

    fun addAtEmptyPosition(
        entity: GameEntity<EntityType>,
        offset: Position3D = Positions.default3DPosition(),
        size: Size3D = actualSize()
    ) : Boolean =
        findEmptyLocationWithin(offset, size).fold(
            whenEmpty = { false },
            whenPresent = {
                addEntity(entity, it)
                true
            }
        )

    fun findEmptyLocationWithin(offset: Position3D, size: Size3D): Maybe<Position3D> {
        var position = Maybe.empty<Position3D>()
        val maxTries = 10
        var currentTry = 0
        while (position.isPresent.not() && currentTry < maxTries) {
            Positions.create3DPosition(
                x = (Math.random() * size.xLength).toInt() + offset.x,
                y = (Math.random() * size.yLength).toInt() + offset.y,
                z = (Math.random() * size.zLength).toInt() + offset.z
            ).let {
                fetchBlockAt(it).map { block ->
                    if (block.isEmptyFloor) position = Maybe.of(it)
                }
            }

            currentTry++
        }

        return position
    }

    fun moveEntity(entity: GameEntity<EntityType>, position: Position3D): Boolean {
        var success = false
        val oldBlock = fetchBlockAt(entity.position)
        val newBlock = fetchBlockAt(position)

        if (oldBlock.isPresent && newBlock.isPresent) {
            success = true
            oldBlock.get().removeEntity(entity)
            entity.position = position
            newBlock.get().addEntity(entity)
        }

        return success
    }
}