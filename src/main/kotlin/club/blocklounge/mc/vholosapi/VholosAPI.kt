package club.blocklounge.mc.vholosapi

import club.blocklounge.mc.vholos.protoTools.Records
import club.blocklounge.mc.vholos.protoTools.Runnable
import org.bukkit.Location
import org.bukkit.plugin.Plugin

class VholosAPI {

    fun createNonPermanentHologram(uniqueName: String, lines: List<String>, initLocation: Location, lineSpacing: Double, viewDistance: Int): Records.GeneralHologramInformation {
        return Records.GeneralHologramInformation(uniqueName, lines, initLocation, lineSpacing, viewDistance)
    }

    fun createNonPermanentHologram(uniqueName: String, lines: List<String>, initLocation: Location, lineSpacing: Double): Records.GeneralHologramInformation {
        return Records.GeneralHologramInformation(uniqueName, lines, initLocation, lineSpacing, 35)
    }

    fun createNonPermanentHologram(uniqueName: String, lines: List<String>, initLocation: Location): Records.GeneralHologramInformation {
        return Records.GeneralHologramInformation(uniqueName, lines, initLocation, 2.24, 35)
    }

    fun getNonTemporaryIndividualHolograms(): List<Records.IndividualHologramInformation> {
        return Runnable.hologramIndividualList
    }

    fun getTemporaryIndividualHolograms(): List<Records.IndividualHologramInformation> {
        return Runnable.temporaryHologramIndividualList
    }

    fun getGeneralHologramsInDatabase(): List<Records.GeneralHologramInformation> {
        return Runnable.hologramInDatabaseFileList
    }

    fun getGeneralHologramsInAPI(): List<Records.GeneralHologramInformation> {
        return Runnable.hologramInAPIList
    }

    fun addNonPermanentHologram(generalHologramInformation: Records.GeneralHologramInformation) {
        Runnable.hologramInAPIList.add(generalHologramInformation)
    }

}