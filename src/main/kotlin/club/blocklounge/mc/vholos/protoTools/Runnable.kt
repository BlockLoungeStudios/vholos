package club.blocklounge.mc.vholos.protoTools

import club.blocklounge.mc.vholos.file.Database
import club.blocklounge.mc.vholos.utilities.Hologram
import club.blocklounge.mc.vholos.vholos
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class Runnable {
    companion object {
        //var hologramEIDList: HashMap<Int, Pair<Int, UUID>> = HashMap()
        //val hologramViewList: HashMap<Int, HologramViewers> = HashMap()

        val cacheCreate = HashMap<Int, PacketContainer>()
        val cacheMeta = HashMap<Int, PacketContainer>()
        val cacheDelete = HashMap<Int, PacketContainer>()
        var prevMetaPacket = HashMap<Player, PacketContainer>()
        var database = Database()
        var utilities = Hologram()
        var holoTaskId = 0

        //Instances of useful information


        //The cringe holograms in databaseyml
        lateinit var hologramInDatabaseFileList: MutableList<Records.GeneralHologramInformation>
        //The holograms to use in api
        lateinit var hologramInAPIList: MutableList<Records.GeneralHologramInformation>
        //The hologram individual information list
        lateinit var hologramIndividualList: MutableList<Records.IndividualHologramInformation>
        //Hologram view list
        lateinit var hologramViewList: HashMap<Int, Records.HologramViewers>
        //Hologram animation list
        lateinit var hologramAnimation: HashMap<Int, Records.HologramAnimationFrames>
    }
    
    fun runHologram() {
        //Create new instances so that when reloaded new instances are made of all
        hologramViewList = HashMap()

        hologramInDatabaseFileList = mutableListOf()
        hologramInAPIList = mutableListOf()
        hologramIndividualList = mutableListOf()

        //Load the file so that it can be read
        database.loadYamlFile(Bukkit.getPluginManager().getPlugin("vholos")!!)

        //The task
        utilities.reloadOrLoadHologramList()
        val task = object : BukkitRunnable() {
            override fun run() {
                for (hologram in hologramIndividualList) {
                    holoIndividualTask(hologram)
                }
            }
        }

        val theTask = vholos.taskManager.asyncTimer(2L, task)
        holoTaskId = theTask.taskId
    }

    fun holoIndividualTask(individualHologramInformation: Records.IndividualHologramInformation) {

        if (!hologramViewList.contains(individualHologramInformation.internalID)) {
            hologramViewList[individualHologramInformation.internalID] = Records.HologramViewers(listOf())
        }

        //For each player see if they are viable and then send them a hologram
        for (player in Bukkit.getOnlinePlayers()) {
            if (utilities.isHologramCloseToPlayerLocation(player.location, individualHologramInformation.location, individualHologramInformation)) {

                //If they just started to view it
                if (!hologramViewList[individualHologramInformation.internalID]!!.mutableAudience.contains(player)) {
                    utilities.addPlayerFromHologramViewList(individualHologramInformation.internalID, player)
                    utilities.sendCreatePacket(player, individualHologramInformation)
                    utilities.sendUpdatePacket(player, individualHologramInformation)
                }
                //If they are now going to view it
                else {
                    utilities.sendUpdatePacket(player, individualHologramInformation)
                }
            }
            else {
                //If they were viewing it but no longer
                if (hologramViewList[individualHologramInformation.internalID]!!.mutableAudience.contains(player)) {
                    utilities.removePlayerFromHologramViewList(individualHologramInformation.internalID, player)
                    utilities.sendDeletePacket(player, individualHologramInformation)
                }
            }
        }
    }


}