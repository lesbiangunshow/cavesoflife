package org.hexworks.cavesofzircon.extensions

import org.hexworks.zircon.api.data.impl.Position3D

fun Position3D.sameLevelNeighborsShuffled(): List<Position3D> =
        (-1..1).flatMap { x ->
            (-1..1).map {y ->
                withRelativeX(x).withRelativeY(y)
            }

        }.minus(this).shuffled()