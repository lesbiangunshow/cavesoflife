package org.hexworks.cavesofzircon.extensions

import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cavesofzircon.attributes.types.Item
import org.hexworks.cavesofzircon.attributes.types.ItemHolder
import org.hexworks.cavesofzircon.world.GameContext

typealias AnyGameEntity = Entity<EntityType, GameContext>

typealias GameEntity<T> = Entity<T, GameContext>

typealias GameCommand<T> = Command<T, GameContext>

typealias GameItem = GameEntity<Item>

typealias GameItemHolder = GameEntity<ItemHolder>