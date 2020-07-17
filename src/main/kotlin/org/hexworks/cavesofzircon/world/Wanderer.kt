package org.hexworks.cavesofzircon.world

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.commands.MoveTo
import org.hexworks.cavesofzircon.extensions.GameEntity
import org.hexworks.cavesofzircon.extensions.position
import org.hexworks.cavesofzircon.extensions.sameLevelNeighborsShuffled

object Wanderer : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean =
        with(entity.position) {
            if (isUnknown().not()) {
                entity.executeCommand(
                    MoveTo(
                        context = context,
                        source = entity,
                        position = sameLevelNeighborsShuffled().first()
                    )
                )
                return@with true
            }
            false
        }

}
