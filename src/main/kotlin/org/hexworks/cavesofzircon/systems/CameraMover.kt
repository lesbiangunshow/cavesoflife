package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.commands.MoveCamera
import org.hexworks.cavesofzircon.extensions.GameCommand
import org.hexworks.cavesofzircon.extensions.position
import org.hexworks.cavesofzircon.world.GameContext

object CameraMover : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(MoveCamera::class) { (context, source, previousPosition) ->
            val world = context.world
            val screenPos = source.position - world.visibleOffset()
            val halfHeight = world.visibleSize().yLength / 2
            val halfWidth = world.visibleSize().xLength / 2
            val currentPosition = source.position
            when {
                previousPosition.y > currentPosition.y && screenPos.y < halfHeight -> {
                    world.scrollOneBackward()
                }
                previousPosition.y < currentPosition.y && screenPos.y > halfHeight -> {
                    world.scrollOneForward()
                }
                previousPosition.x > currentPosition.x && screenPos.x < halfWidth -> {
                    world.scrollOneLeft()
                }
                previousPosition.x < currentPosition.x && screenPos.x > halfWidth -> {
                    world.scrollOneRight()
                }
            }
            Consumed
        }
}
