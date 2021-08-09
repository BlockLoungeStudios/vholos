package club.blocklounge.mc.vholos.utilities

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Utils {
    val onlinePlayers: Collection<Player> get() {
            val onlinePlayers: MutableCollection<Player> = ArrayList()
            Bukkit.getOnlinePlayers().forEach{ player ->
                onlinePlayers.add(player)
            }
            return onlinePlayers
        }
}