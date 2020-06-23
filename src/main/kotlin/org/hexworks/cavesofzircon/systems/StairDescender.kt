package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.types.StairsDown
import org.hexworks.cavesofzircon.blocks.GameBlock
import org.hexworks.cavesofzircon.commands.MoveDown
import org.hexworks.cavesofzircon.extensions.GameCommand
import org.hexworks.cavesofzircon.extensions.position
import org.hexworks.cavesofzircon.functions.logGameEvent
import org.hexworks.cavesofzircon.world.GameContext
import org.hexworks.cobalt.datatypes.extensions.map

object StairDescender : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response =
        command.responseWhenCommandIs(MoveDown::class) { (context, source) ->
            val position = source.position

            with(context.world) {
                fetchBlockAt(position).map { block ->
                    if (block.hasStairsDown) {
                        logGameEvent("You move down one level...")

                        moveEntity(source, position.withRelativeZ(-1))
                        scrollOneDown()
                    } else logGameEvent("You try to dig a hole in the ground with your bare hands. Nope.")
                }
            }

            Consumed
        }

    private val GameBlock.hasStairsDown: Boolean  get() = entities.any { it.type == StairsDown }
}