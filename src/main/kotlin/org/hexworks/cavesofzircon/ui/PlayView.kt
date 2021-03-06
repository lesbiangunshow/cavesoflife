package org.hexworks.cavesofzircon.ui

import org.hexworks.cavesofzircon.world.Game
import org.hexworks.cavesofzircon.blocks.GameBlock
import org.hexworks.cavesofzircon.GameConfig
import org.hexworks.cavesofzircon.systems.GameLogEvent
import org.hexworks.cavesofzircon.world.GameBuilder
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.onKeyboardEvent
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon

class PlayView(private val game: Game = GameBuilder.defaultGame()) : BaseView() {

    override val theme = ColorThemes.arc()

    override fun onDock() {

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
            .withGameArea(game.world)
            .withVisibleSize(game.world.visibleSize())
            .withProjectionMode(ProjectionMode.TOP_DOWN)
            .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
            .build()

        val sidebar = Components.panel()
            .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
            .wrapWithBox()
            .build()

        val logArea = Components.logArea()
            .withTitle("Log")
            .wrapWithBox()
            .withSize(GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH, GameConfig.LOG_AREA_HEIGHT)
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
            .build()

        with(screen) {
            addComponent(gameComponent)
            addComponent(sidebar)
            addComponent(logArea)

            onKeyboardEvent(KeyboardEventType.KEY_PRESSED) { event, _ ->
                game.world.update(this, event, game)
                Processed
            }
        }

        Zircon.eventBus.subscribe<GameLogEvent> {(text) ->
            logArea.addParagraph(
                paragraph = text,
                withNewLine = false,
                withTypingEffectSpeedInMs = 10
            )
        }
    }
}
