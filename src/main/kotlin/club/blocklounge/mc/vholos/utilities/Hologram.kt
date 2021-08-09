package club.blocklounge.mc.vholos.utilities

import club.blocklounge.mc.vholos.protoTools.CompatabilityManager
import club.blocklounge.mc.vholos.protoTools.Records
import club.blocklounge.mc.vholos.protoTools.Runnable
import club.blocklounge.mc.vholos.protoTools.Runnable.Companion.hologramInAPIList
import club.blocklounge.mc.vholos.protoTools.Runnable.Companion.hologramInDatabaseFileList
import club.blocklounge.mc.vholos.protoTools.Runnable.Companion.hologramIndividualList
import club.blocklounge.mc.vholos.vholos
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedWatchableObject
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.gradle.internal.impldep.com.fasterxml.jackson.databind.exc.InvalidFormatException
import java.io.IOException
import java.util.*


class Hologram {
    fun reloadOrLoadHologramList() {

        hologramInDatabaseFileList.clear()
        hologramIndividualList.clear()

        addHologramsFromDatabase()

        //The id for the hologram
        //For each hologram
        var j = 1
        for (hologram in Runnable.database.readYamlFile()) {
            for ((i, line) in hologram.lines.withIndex()) {

                val uniqueEntityID = vholos.wrapper1_17.getUniqueEntityId()
                val uniqueUUID = UUID.randomUUID()

                val eidPair = Pair(uniqueEntityID, uniqueUUID)

                val hologramLocation = hologram.initLocation
                val newLoc = Location(hologramLocation.world, hologramLocation.x, (hologramLocation.y - (i*hologram.lineSpacing)), hologramLocation.z)

                hologramIndividualList.add(Records.IndividualHologramInformation(hologram.name, line, newLoc, hologram.viewDistance, uniqueEntityID, uniqueUUID, j))
                j++
            }
        }
    }

    fun addHologramsFromDatabase() {
        for (hologram in Runnable.database.readYamlFile()) {
            addHologramToDatabaseList(hologram)
        }
    }

    fun addHologramToDatabaseList(generalHologramInformation: Records.GeneralHologramInformation) {
        hologramInDatabaseFileList.add(generalHologramInformation)
    }

    fun removeHologramFromDatabaseList(generalHologramInformation: Records.GeneralHologramInformation) {
        hologramInDatabaseFileList.remove(generalHologramInformation)
    }

    fun addHologramToAPIList(generalHologramInformation: Records.GeneralHologramInformation) {
        hologramInAPIList.add(generalHologramInformation)
        reloadOrLoadHologramList()
    }

    fun removeHologramFromAPIList(generalHologramInformation: Records.GeneralHologramInformation) {
        hologramInAPIList.remove(generalHologramInformation)
        reloadOrLoadHologramList()
    }

    fun addHologramToInitialInformationList() {


    }

    fun removeHologramFromInitialInformationList(generalHologramInformation: Records.GeneralHologramInformation) {
        //No idea
    }

    fun fromInitialInformationListRemoveIndividualHologramInformation( internalID: Int?, generalHologramInformation: Records.GeneralHologramInformation?) {
        if (internalID != null) {
            for (hologram in Runnable.hologramIndividualList) {
                if (hologram.internalID == internalID) {
                    Runnable.hologramIndividualList.remove(hologram)
                }
            }
        }
        else if (generalHologramInformation != null) {
            for (hologram in Runnable.hologramIndividualList) {
                if (hologram.name == generalHologramInformation.name) {
                    Runnable.hologramIndividualList.remove(hologram)
                }
            }
        }
    }

    fun createDeletePacket(eid: Int): PacketContainer {
        return if (Runnable.cacheDelete.contains(eid)) {
            Runnable.cacheDelete[eid]!!
        } else {
            val packetContainer = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
            packetContainer.intLists.write(0, Collections.singletonList(eid))
            packetContainer
        }
    }

    fun sendDeletePacket(player: Player, individualHologramInformation : Records.IndividualHologramInformation) {
        val toSendPacket = createDeletePacket(individualHologramInformation.eid)
        try {
            vholos.protocolManager.sendServerPacket(player, toSendPacket)
        }
        catch (e: IOException) {
            vholos.logger.error(e.toString())
        }
    }

    fun createUpdatePacket(individualHologramInformation: Records.IndividualHologramInformation, player: Player): PacketContainer {
        if (!individualHologramInformation.line.contains("%(.*?)%".toRegex())) {
            if (Runnable.cacheMeta.contains(individualHologramInformation.eid)) {
                return Runnable.cacheMeta[individualHologramInformation.eid]!!
            }
            else {
                val metaDataPacket = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)

                metaDataPacket.integers
                    .write(0, individualHologramInformation.eid)
                val newHoloName = PlaceholderAPI.setPlaceholders(player, individualHologramInformation.line)

                val wrappedChatComponent = WrappedChatComponent.fromJson(
                    GsonComponentSerializer.gson().serialize(
                        MiniMessage.get().parse(newHoloName)))

                val content: MutableList<WrappedWatchableObject> = ArrayList()
                content.add(
                    WrappedWatchableObject(
                        WrappedDataWatcher.WrappedDataWatcherObject(0, CompatabilityManager.getByteSerialiser()), 0x20.toByte()
                    )
                )
                content.add(
                    WrappedWatchableObject(
                        WrappedDataWatcher.WrappedDataWatcherObject(2, CompatabilityManager.getchatSerialiser()), Optional.of(wrappedChatComponent.handle)
                    )
                )
                content.add(
                    WrappedWatchableObject(
                        WrappedDataWatcher.WrappedDataWatcherObject(3, CompatabilityManager.getbooleanSerialiser()), true
                    )
                )
                content.add(
                    WrappedWatchableObject(
                        WrappedDataWatcher.WrappedDataWatcherObject(5, CompatabilityManager.getbooleanSerialiser()),true
                    )
                )

                metaDataPacket.watchableCollectionModifier
                    .write(0, content)

                return metaDataPacket
            }
        }
        else {
            val metaDataPacket = PacketContainer(PacketType.Play.Server.ENTITY_METADATA)
            metaDataPacket.integers
                .write(0, individualHologramInformation.eid)
            val newHoloName = PlaceholderAPI.setPlaceholders(player, individualHologramInformation.line)

            val wrappedChatComponent = WrappedChatComponent.fromJson(
                GsonComponentSerializer.gson().serialize(
                    MiniMessage.get().parse(newHoloName)))
            val content: MutableList<WrappedWatchableObject> = ArrayList()
            content.add(
                WrappedWatchableObject(
                    WrappedDataWatcher.WrappedDataWatcherObject(0, CompatabilityManager.getByteSerialiser()), 0x20.toByte()
                )
            )
            content.add(
                WrappedWatchableObject(
                    WrappedDataWatcher.WrappedDataWatcherObject(2, CompatabilityManager.getchatSerialiser()), Optional.of(wrappedChatComponent.handle)
                )
            )
            content.add(
                WrappedWatchableObject(
                    WrappedDataWatcher.WrappedDataWatcherObject(3, CompatabilityManager.getbooleanSerialiser()), true
                )
            )
            content.add(
                WrappedWatchableObject(
                    WrappedDataWatcher.WrappedDataWatcherObject(5, CompatabilityManager.getbooleanSerialiser()),true
                )
            )

            metaDataPacket.watchableCollectionModifier
                .write(0, content)

            return metaDataPacket
        }
    }

    fun sendUpdatePacket(player: Player, individualHologramInformation: Records.IndividualHologramInformation) {
        val toSendPacket = createUpdatePacket(individualHologramInformation, player)
        try {
            if (Runnable.prevMetaPacket.contains(player)) {
                if (Runnable.prevMetaPacket[player] != toSendPacket) {
                    vholos.protocolManager.sendServerPacket(player, toSendPacket)
                    Runnable.prevMetaPacket[player] = toSendPacket
                }
            }
            else {
                vholos.protocolManager.sendServerPacket(player, toSendPacket)
                Runnable.prevMetaPacket[player] = toSendPacket
            }
        }
        catch (e: IOException) {
            vholos.logger.error(e.toString())
        }
    }

    fun createCreatePacket(individualHologramInformation: Records.IndividualHologramInformation): PacketContainer {
        if (Runnable.cacheCreate.contains(individualHologramInformation.eid)) {
            return Runnable.cacheCreate[individualHologramInformation.eid]!!
        }
        else {
            val packetContainer = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING)

            packetContainer.integers
                .write(0, individualHologramInformation.eid) //EID?
                .write(1, 1) //Entity Type
            //.write(2, 69420) //Data for what?
            packetContainer.uuiDs
                .write(0, individualHologramInformation.uuid) //UUID
            packetContainer.doubles
                .write(0, individualHologramInformation.location.x) //X
                .write(1, individualHologramInformation.location.y) //Y
                .write(2, individualHologramInformation.location.z) //Z

            return packetContainer
        }
    }

    fun sendCreatePacket(player: Player, individualHologramInformation: Records.IndividualHologramInformation) {
        val toSendPacket1 = createCreatePacket(individualHologramInformation)
        val toSendPacket2 = createUpdatePacket(individualHologramInformation, player)
        try {
            vholos.protocolManager.sendServerPacket(player, toSendPacket1)
            vholos.protocolManager.sendServerPacket(player, toSendPacket2)
            //Runnable.prevMetaPacket[player] = toSendPackets.second
        }
        catch (e: IOException) {
            vholos.logger.error(e.toString())
        }
    }


    fun addPlayerFromHologramViewList(name: Int, player: Player) {
        val previousList: MutableList<Player> = Runnable.hologramViewList[name]!!.mutableAudience.toMutableList()
        previousList.add(player)
        Runnable.hologramViewList[name] = Records.HologramViewers(previousList)
    }

    fun removePlayerFromHologramViewList(name: Int, player: Player) {
        val previousList: MutableList<Player> = Runnable.hologramViewList[name]!!.mutableAudience.toMutableList()
        previousList.remove(player)
        Runnable.hologramViewList[name] = Records.HologramViewers(previousList)
    }

    fun isHologramCloseToPlayerLocation(locPlayer: Location, locHologram: Location, individualHologramInformation: Records.IndividualHologramInformation):Boolean {
        return if (locPlayer.world == locHologram.world) {
            val statmentX = (locHologram.x-locPlayer.x)*(locHologram.x-locPlayer.x)
            val statmentY = (locHologram.y-locPlayer.y)*(locHologram.y-locPlayer.y)
            val statmentZ = (locHologram.z-locPlayer.z)*(locHologram.z-locPlayer.z)

            val compare = statmentX+statmentY+statmentZ

            (compare < (individualHologramInformation.viewDistance*individualHologramInformation.viewDistance))
        } else {
            false
        }
    }

    fun clearTotalHologramView() {
        //Legacy unmarked code needs update!
        var j = 1
        var k = 0
        for (hologram in hologramInDatabaseFileList) {
            var i = 0
            //For each line iteration
            while (i < hologram.lines.size) {
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    if (Runnable.hologramViewList[j]!!.mutableAudience.contains(onlinePlayer)) {
                        try {
                            vholos.protocolManager.sendServerPacket(onlinePlayer, createDeletePacket(Runnable.hologramIndividualList[k].eid))
                        }

                        catch (e: IOException) {
                            vholos.logger.error(e.toString())
                        }
                    }
                }
                i++
                j++
                k++
            }
        }
    }

    @Throws(InvalidFormatException::class)
    fun getHologramLocationFromString(input: String): Location? {

        val parts = input.split(",").toTypedArray()
        if (parts.size != 4) {
            return null
        }
        return try {
            val x = parts[1].replace(" ", "").toDouble()
            val y = parts[2].replace(" ", "").toDouble()
            val z = parts[3].replace(" ", "").toDouble()
            val world = Bukkit.getWorld(parts[0].trim { it <= ' ' })
            Location(world, x, y, z)
        } catch (ex: NumberFormatException) {
            return null
        }
    }


}