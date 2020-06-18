package org.hexworks.cavesofzircon.factories

import org.hexworks.cavesofzircon.GameBlock
import org.hexworks.cavesofzircon.GameTileRepository

object GameBlockFactory {

    fun floor() = GameBlock(GameTileRepository.FLOOR)

    fun wall() = GameBlock(GameTileRepository.WALL)

}