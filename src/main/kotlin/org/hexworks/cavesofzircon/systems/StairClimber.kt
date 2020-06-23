package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.types.StairsUp
import org.hexworks.cavesofzircon.blocks.GameBlock
import org.hexworks.cavesofzircon.commands.MoveUp
import org.hexworks.cavesofzircon.extensions.GameCommand
import org.hexworks.cavesofzircon.extensions.position
import org.hexworks.cavesofzircon.functions.logGameEvent
import org.hexworks.cavesofzircon.world.GameContext
import org.hexworks.cobalt.datatypes.extensions.map

object StairClimber : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response =
        command.responseWhenCommandIs(MoveUp::class) { (context, source) ->
            val position = source.position

            with(context.world) {
                fetchBlockAt(position).map { block ->
                    if (block.hasStairsUp) {
                        logGameEvent("You move up one level...")

                        moveEntity(source, position.withRelativeZ(1))
                        scrollOneUp()
                    } else logGameEvent("You jump up and try to reach the ceiling. You fail.")
                }
            }

            Consumed
        }

    private val GameBlock.hasStairsUp: Boolean  get() = entities.any { it.type == StairsUp }
}