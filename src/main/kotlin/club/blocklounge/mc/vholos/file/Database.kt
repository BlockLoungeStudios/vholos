package club.blocklounge.mc.vholos.file

import club.blocklounge.mc.vholos.Vholos
import club.blocklounge.mc.vholos.protoTools.Records
import club.blocklounge.mc.vholos.protoTools.Runnable
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

class Database {
    companion object {
        lateinit var file: File
        lateinit var config: FileConfiguration

    }

    fun loadYamlFile(plugin : Plugin) {

        file = File(plugin.dataFolder, "database.yml")

        if (!file.exists()) {
            plugin.dataFolder.mkdirs()
            plugin.saveResource("database.yml", true)
        }

        config = YamlConfiguration.loadConfiguration(file)
    }

    fun readYamlFile(): List<Records.GeneralHologramInformation> {

        val savedHologramNames = config.getKeys(false)

        val mutableHologramList = mutableListOf<Records.GeneralHologramInformation>()

        for (name in savedHologramNames) {
            try {
                mutableHologramList.add(createHologramFromNameInDatabase(name))
            }
            catch (e: Exception) {
                Vholos.logging.error(e.toString())
            }
        }
        //addToListIDs(mutableHologramList)
        //Create EID's and UUID's for them all

        return mutableHologramList
    }

    private fun loadHologram(name: String): Records.GeneralHologramInformation {
        val configSection = config.getConfigurationSection(name)

        if (configSection != null) {
            val lines: List<String> = configSection.getStringList("lines")
            val locationString: String? = configSection.getString("location")
            val lineSpacing: Double = configSection.getDouble("linespacing")
            val viewDistance: Int = configSection.getInt("viewdistance")

            if (locationString != null && lines.isNotEmpty()) {
                val loc: Location? = Runnable.utilities.getHologramLocationFromString(locationString)
                if (loc != null) {
                    return Records.GeneralHologramInformation(name, lines, loc, lineSpacing, viewDistance)
                }
            }
            else {
                Vholos.logging.error("The database file is incorrectly made!")
            }
        }
        return Records.GeneralHologramInformation("null", listOf("null", "null"), Location(null, 0.0, 0.0, 0.0), 0.0, 0)
    }

    fun createHologramFromNameInDatabase(name: String): Records.GeneralHologramInformation {
        return loadHologram(name)
    }

    fun addToListIDs(mutableList: List<Records.GeneralHologramInformation>) {

    }
}