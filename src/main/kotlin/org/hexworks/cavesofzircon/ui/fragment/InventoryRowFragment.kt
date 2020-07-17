package org.hexworks.cavesofzircon.ui.fragment

import org.hexworks.cavesofzircon.attributes.types.iconTile
import org.hexworks.cavesofzircon.extensions.GameItem
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.graphics.Symbols

class InventoryRowFragment(width: Int, item: GameItem) : Fragment {

    val dropButton = Components.button()
        .wrapSides(false)
        .withText("${Symbols.ARROW_DOWN}")
        .build()

    override val root: Component =
        Components.hbox()
            .withSpacing(1)
            .withSize(1)
            .build().apply {
                addComponent(
                    Components.icon().withIcon(item.iconTile)
                )

                addComponent(
                    Components.label()
                        .withSize(Inventory)
                )
            }
}