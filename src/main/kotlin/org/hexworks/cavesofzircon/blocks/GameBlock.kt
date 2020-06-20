package org.hexworks.cavesofzircon.blocks

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.GameTileRepository
import org.hexworks.cavesofzircon.extensions.GameEntity
import org.hexworks.cavesofzircon.extensions.occupesBlock
import org.hexworks.cavesofzircon.extensions.tile
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(
    private var defaultTile: Tile = GameTileRepository.FLOOR,
    private val currentEntities: MutableList<GameEntity<EntityType>> = mutableListOf()
) : BlockBase<Tile>() {

    companion object {

        fun createWith(entity: GameEntity<EntityType>) = GameBlock(
            currentEntities = mutableListOf(entity)
        )
    }

    val isFloor: Boolean
        get() = defaultTile == GameTileRepository.FLOOR

    val isWall: Boolean
        get() = defaultTile == GameTileRepository.WALL

    val isEmptyFloor: Boolean
        get() = currentEntities.isEmpty()

    val occupier: Maybe<GameEntity<EntityType>>
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.occupesBlock })

    val isOccupied: Boolean
        get() = occupier.isPresent

    val entities: Iterable<GameEntity<EntityType>>
        get() = currentEntities.toList()

    override val layers: MutableList<Tile>
        get() {
            val tile: Tile
            currentEntities.map { it.tile }.apply {
                tile = when {
                    contains(GameTileRepository.PLAYER) -> GameTileRepository.PLAYER
                    isNotEmpty() -> first()
                    else -> defaultTile
                }
            }

            return mutableListOf(tile)
        }

    fun addEntity(entity: GameEntity<EntityType>) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: GameEntity<EntityType>) {
        currentEntities.remove(entity)
    }

    override fun fetchSide(side: BlockSide) = GameTileRepository.EMPTY
}