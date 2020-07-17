package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.types.Item
import org.hexworks.cavesofzircon.attributes.types.addItem
import org.hexworks.cavesofzircon.commands.PickItemUp
import org.hexworks.cavesofzircon.extensions.GameCommand
import org.hexworks.cavesofzircon.extensions.filterType
import org.hexworks.cavesofzircon.extensions.isPlayer
import org.hexworks.cavesofzircon.functions.logGameEvent
import org.hexworks.cavesofzircon.world.GameContext
import org.hexworks.cavesofzircon.world.World
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.flatMap
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.data.impl.Position3D

object ItemPicker : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response =
        command.responseWhenCommandIs(PickItemUp::class) { (context, itemHolder, position) ->
            with(context.world) {
                findTopItem(position).map { item ->
                    if (itemHolder.addItem(item)) {
                        removeEntity(item)
                        val subject: String
                        val verb: String
                        if (itemHolder.isPlayer) {
                            subject = "You"
                            verb = "pick up"
                        } else {
                            subject = "The $itemHolder"
                            verb = "picks up"
                        }

                        logGameEvent("$subject $verb the $item.")
                    }
                }
            }

            Consumed
        }

    private fun World.findTopItem(position: Position3D) =
        fetchBlockAt(position).flatMap { block ->
            Maybe.ofNullable(block.entities.filterType<Item>().firstOrNull())
        }

}