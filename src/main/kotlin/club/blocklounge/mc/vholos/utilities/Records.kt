package club.blocklounge.mc.vholos.protoTools

import club.blocklounge.mc.vholos.utilities.Hologram
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class Records {
    @JvmRecord
    data class GeneralHologramInformation(val name: String, val lines: List<String>, val initLocation: Location, val lineSpacing: Double, val viewDistance: Int)

    @JvmRecord
    data class IndividualHologramInformation(val name: Int, val line: String, val lineNumber: Int, val location: Location, val eid: Int, val uuid: UUID, val isTemp: Boolean, val originalHologram: GeneralHologramInformation)

    @JvmRecord
    data class HologramViewers(val mutableAudience: List<Player>)
}