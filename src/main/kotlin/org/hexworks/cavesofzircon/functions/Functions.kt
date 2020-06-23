package org.hexworks.cavesofzircon.functions

import org.hexworks.cavesofzircon.systems.GameLogEvent
import org.hexworks.zircon.internal.Zircon

fun logGameEvent(text: String): Unit =
    Zircon.eventBus.publish(GameLogEvent(text))
