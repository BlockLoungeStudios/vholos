package club.blocklounge.mc.vholos.compat.wrapper

import club.blocklounge.mc.vholos.protoTools.Records
import club.blocklounge.mc.vholos.protoTools.Runnable
import org.bukkit.Location
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class Wrapper1_17 {
    companion object {
        var mutableListOfIDs: HashMap<Int, Pair<Records.GeneralHologramInformation, Int>> = HashMap()
    }

    private var ENTITY_ID: AtomicInteger? = null

    fun getUniqueEntityId(): Int {
        if (ENTITY_ID == null) {
            //val packageName = Bukkit.getServer().javaClass.getPackage().name
            //val SERVER_VERSION = packageName.substring(packageName.lastIndexOf('.') + 1)
            try {
                val entityCount: Field = Class.forName("net.minecraft.world.entity.Entity").getDeclaredField("b")
                entityCount.isAccessible = true
                ENTITY_ID = entityCount.get(null) as AtomicInteger
            } catch (e: ReflectiveOperationException) {
                throw IllegalArgumentException(e)
            }
        }
        return ENTITY_ID!!.incrementAndGet()
    }

    fun getUniqueInternalId(individualHologramInformation: Records.GeneralHologramInformation, line: Int): Int {
        repeat(mutableListOfIDs.size+1) { int ->
            if (!mutableListOfIDs.contains(int)) {
                mutableListOfIDs[int] = Pair(individualHologramInformation, line)
                return int
            }
        }
        return 0
    }

    fun clearUniqueInternalIds() {
        mutableListOfIDs.clear()
    }

    fun getHologramsFromIds(): List<Pair<Records.GeneralHologramInformation, Int>> {
        return mutableListOfIDs.values.toList()
    }

    fun getHologramFromId(id: Int): Records.IndividualHologramInformation {
        return if (mutableListOfIDs.containsKey(id)) {
            getIndividualhologramFromGeneralHologramAndLine(mutableListOfIDs[id]!!)
        } else {
            Runnable.hologramIndividualList.first()
        }
    }

    fun getAllIndividualHologramsFromGeneralHologram(generalHologramInformation: Records.GeneralHologramInformation): List<Records.IndividualHologramInformation> {
        var mutableListOfIndividualHolograms = mutableListOf<Records.IndividualHologramInformation>()
        for (hologram in Runnable.hologramIndividualList) {
            if (hologram.originalHologram.equals(generalHologramInformation)) {
                mutableListOfIndividualHolograms.add(hologram)
            }
        }
        for (hologram in Runnable.temporaryHologramIndividualList) {
            if (hologram.originalHologram.equals(generalHologramInformation)) {
                mutableListOfIndividualHolograms.add(hologram)
            }
        }
        return mutableListOfIndividualHolograms
    }

    fun getIndividualhologramFromGeneralHologramAndLine(baseInformation: Pair<Records.GeneralHologramInformation, Int>): Records.IndividualHologramInformation {
        for (hologram in Runnable.hologramIndividualList) {
            if (hologram.lineNumber == baseInformation.second && baseInformation.first.lines.contains(hologram.line)) {
                return hologram
            }
        }
        return Records.IndividualHologramInformation(0, "null", 0, Location(null, 0.0, 0.0, 0.0), 0, UUID.randomUUID(), false, baseInformation.first)
    }
}