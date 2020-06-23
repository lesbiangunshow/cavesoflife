package org.hexworks.cavesofzircon.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.types.combatStats
import org.hexworks.cavesofzircon.commands.Attack
import org.hexworks.cavesofzircon.commands.Destroy
import org.hexworks.cavesofzircon.extensions.GameCommand
import org.hexworks.cavesofzircon.extensions.isPlayer
import org.hexworks.cavesofzircon.extensions.ifHasNoHealthLeftThen
import org.hexworks.cavesofzircon.functions.logGameEvent
import org.hexworks.cavesofzircon.world.GameContext


object Attackable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>): Response =
        command.responseWhenCommandIs(Attack::class) { (context, attacker, target) ->

            if (attacker.isPlayer || target.isPlayer) {

                val damage = Math.max(0, attacker.combatStats.attackValue - target.combatStats.defenseValue)
                val finalDamage = (Math.random() * damage).toInt() + 1
                target.combatStats.hp -= finalDamage

                logGameEvent("The $attacker hits the $target for $finalDamage!")

                target.ifHasNoHealthLeftThen {
                    target.executeCommand(
                        Destroy(
                            context, attacker, target,
                            "a blow to the head."
                        )
                    )
                }

                Consumed
            } else Pass
        }
}