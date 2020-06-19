package org.hexworks.cavesofzircon.world

import org.hexworks.cavesofzircon.GameConfig
import org.hexworks.cavesofzircon.attributes.types.Player
import org.hexworks.cavesofzircon.extensions.GameEntity
import org.hexworks.zircon.api.data.impl.Size3D

class Game(
    val world: World,
    val player: GameEntity<Player>
) {

    companion object {

        fun create(
            world: World,
            player: GameEntity<Player>
        ) = Game(
            world,
            player
        )
    }
}