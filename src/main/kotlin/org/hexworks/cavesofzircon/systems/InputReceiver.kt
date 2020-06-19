package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.commands.MoveTo
import org.hexworks.cavesofzircon.extensions.GameEntity
import org.hexworks.cavesofzircon.extensions.position
import org.hexworks.cavesofzircon.world.GameContext
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent

object InputReceiver : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context

        val currentPosition = player.position
        if (uiEvent is KeyboardEvent) {
            val newPosition = when (uiEvent.code) {
                KeyCode.KEY_W -> currentPosition.withRelativeY(-1)
                KeyCode.KEY_A -> currentPosition.withRelativeX(-1)
                KeyCode.KEY_S -> currentPosition.withRelativeY(1)
                KeyCode.KEY_D -> currentPosition.withRelativeX(1)
                else -> {
                    currentPosition
                }
            }

            player.executeCommand(MoveTo(context, player, newPosition))
        }

        return true
    }
}