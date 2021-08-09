package club.blocklounge.mc.vholos.protoTools

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class Records {
    @JvmRecord
    data class GeneralHologramInformation(val name: String, val lines: List<String>, val initLocation: Location, val lineSpacing: Double, val viewDistance: Int)

    @JvmRecord
    data class IndividualHologramInformation(val name: String, val line: String, val location: Location, val viewDistance: Int, val eid: Int, val uuid: UUID, val internalID: Int)

    @JvmRecord
    data class HologramViewers(val mutableAudience: List<Player>)
}