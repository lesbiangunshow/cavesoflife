package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.types.Player
import org.hexworks.cavesofzircon.commands.MoveDown
import org.hexworks.cavesofzircon.commands.MoveTo
import org.hexworks.cavesofzircon.commands.MoveUp
import org.hexworks.cavesofzircon.commands.PickItemUp
import org.hexworks.cavesofzircon.extensions.GameEntity
import org.hexworks.cavesofzircon.extensions.position
import org.hexworks.cavesofzircon.world.GameContext
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent

object InputReceiver : BaseBehavior<GameContext>() {
    private val logger = LoggerFactory.getLogger(this::class)

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context

        val currentPosition = player.position
        if (uiEvent is KeyboardEvent) {
            with(player) {
                when (uiEvent.code) {
                    KeyCode.KEY_W -> moveTo(currentPosition.withRelativeY(-1), context)
                    KeyCode.KEY_A -> moveTo(currentPosition.withRelativeX(-1), context)
                    KeyCode.KEY_S -> moveTo(currentPosition.withRelativeY(1), context)
                    KeyCode.KEY_D -> moveTo(currentPosition.withRelativeX(1), context)
                    KeyCode.KEY_R -> moveUp(context)
                    KeyCode.KEY_F -> moveDown(context)
                    KeyCode.KEY_P -> pickItemUp(currentPosition, context)
                    else -> {
                        logger.debug("UI Event ($uiEvent) does not have a corresponding command, it is ignored.")
                    }
                }
            }
        }
        return true
    }

    private fun GameEntity<Player>.moveTo(position: Position3D, context: GameContext) {
        executeCommand(MoveTo(context, this, position))
    }

    private fun GameEntity<Player>.moveUp(context: GameContext) {
        executeCommand(MoveUp(context, this))
    }

    private fun GameEntity<Player>.moveDown(context: GameContext) {
        executeCommand(MoveDown(context, this))
    }

    private fun GameEntity<Player>.pickItemUp(position: Position3D, context: GameContext) {
        executeCommand(PickItemUp(context, this, position))
    }
}
