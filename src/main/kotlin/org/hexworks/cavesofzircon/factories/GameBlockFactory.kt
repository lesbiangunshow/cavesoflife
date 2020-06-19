package org.hexworks.cavesofzircon.factories

import org.hexworks.cavesofzircon.blocks.GameBlock
import org.hexworks.cavesofzircon.GameTileRepository

object GameBlockFactory {

    fun floor() = GameBlock(GameTileRepository.FLOOR)

    fun wall() = GameBlock(GameTileRepository.WALL)

}