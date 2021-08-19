package club.blocklounge.mc.vholos.utilities

import club.blocklounge.mc.vholos.Vholos
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender

class Message {
    fun createMessage(message: String): Component {
        return MiniMessage.get().parse(message)
    }

    fun sendMessage(player: CommandSender, message: Component) {
        val audience: Audience = Vholos.audience.sender(player)
        audience.sendMessage(message)
    }

    fun sendEmptyMessage(player: CommandSender) {
        val audience: Audience = Vholos.audience.sender(player)
        audience.sendMessage(Component.text().content(" "))
    }
}