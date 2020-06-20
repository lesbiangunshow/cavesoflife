package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.commands.Attack
import org.hexworks.cavesofzircon.extensions.GameCommand
import org.hexworks.cavesofzircon.world.GameContext


object Attackable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response =
        command.responseWhenCommandIs(Attack::class) { (context, _, target) ->
            context.world.removeEntity(target)

            Consumed
        }

}