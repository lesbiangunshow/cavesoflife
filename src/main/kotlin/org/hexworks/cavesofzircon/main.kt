package org.hexworks.cavesofzircon

import org.hexworks.cavesofzircon.ui.StartView
import org.hexworks.zircon.api.SwingApplications

fun main() {

    val application = SwingApplications.startApplication(GameConfig.buildAppConfig())

    application.dock(StartView())

}
