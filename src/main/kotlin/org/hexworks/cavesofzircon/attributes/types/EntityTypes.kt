package org.hexworks.cavesofzircon.attributes.types

import org.hexworks.amethyst.api.base.BaseEntityType

object FogOfWarType: BaseEntityType()

object Wall : BaseEntityType(
    name = "wall"
)

object StairsDown : BaseEntityType(
    name = "stairs down"
)

object StairsUp : BaseEntityType(
    name = "stairs up"
)

object Player : BaseEntityType(
    name = "player"
), Combatant

object Fungus : BaseEntityType(
    name = "fungus"
), Combatant