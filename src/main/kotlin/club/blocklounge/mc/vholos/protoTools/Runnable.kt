package club.blocklounge.mc.vholos.protoTools

import club.blocklounge.mc.vholos.Vholos
import club.blocklounge.mc.vholos.file.Database
import club.blocklounge.mc.vholos.utilities.Hologram
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class Runnable {
    companion object {
        //var hologramEIDList: HashMap<Int, Pair<Int, UUID>> = HashMap()
        //val hologramViewList: HashMap<Int, HologramViewers> = HashMap()

        val cacheCreate = HashMap<Int, PacketContainer>()
        val cacheMeta = HashMap<Int, PacketContainer>()
        val cacheDelete = HashMap<Int, PacketContainer>()
        var database = Database()
        var utilities = Hologram()
        var holoTaskId = 0

        //Instances of useful information


        //The cringe holograms in database yml
        var hologramInDatabaseFileList: MutableList<Records.GeneralHologramInformation> = mutableListOf()
        //The holograms to use in api
        var hologramInAPIList: MutableList<Records.GeneralHologramInformation> = mutableListOf()
        //The hologram individual information list for all holograms
        var hologramIndividualList: MutableList<Records.IndividualHologramInformation> = mutableListOf()
        //The previous hologram individual information list for all holograms
        var temporaryHologramIndividualList: MutableList<Records.IndividualHologramInformation> = mutableListOf()
        //Hologram view list of players who see the hologram
        var hologramViewList: HashMap<Int, Records.HologramViewers> = HashMap()
        //Hologram animation list for future
        var hologramPreviousFrame: HashMap<Int, PacketContainer> = HashMap()
    }
    
    fun runHologram() {
        //Load the file so that it can be read
        database.loadYamlFile(Bukkit.getPluginManager().getPlugin("Vholos")!!)

        //The task
        utilities.reloadOrLoadHologramList()
        val task = object : BukkitRunnable() {
            override fun run() {
                for (hologram in hologramIndividualList) {
                    holoIndividualTask(hologram)
                }
                for (hologram in temporaryHologramIndividualList) {
                    holoIndividualTask(hologram)
                }
            }
        }

        val theTask = Vholos.taskManager.asyncTimer(2L, task)
        holoTaskId = theTask.taskId
    }

    fun holoIndividualTask(individualHologramInformation: Records.IndividualHologramInformation) {

        if (!hologramViewList.contains(individualHologramInformation.eid)) {
            //Record if how many hologram viewers e.t.c if there is no list of viewers!
            hologramViewList[individualHologramInformation.eid] = Records.HologramViewers(listOf())
        }

        //For each player see if they are viable and then send them a hologram
        for (player in Bukkit.getOnlinePlayers()) {
            if (utilities.isHologramCloseToPlayerLocation(player.location, individualHologramInformation.location, individualHologramInformation)) {
                //If they just started to view it
                if (!hologramViewList[individualHologramInformation.eid]!!.mutableAudience.contains(player)) {
                    utilities.addPlayerFromHologramViewList(individualHologramInformation.eid, player)
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
                if (hologramViewList[individualHologramInformation.eid]!!.mutableAudience.contains(player)) {
                    utilities.removePlayerFromHologramViewList(individualHologramInformation.eid, player)
                    utilities.sendDeletePacket(player, individualHologramInformation)
                }
            }
        }
    }


}