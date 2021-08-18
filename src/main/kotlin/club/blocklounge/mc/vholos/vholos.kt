package club.blocklounge.mc.vholos

import club.blocklounge.mc.vholos.api.VholosAPI
import club.blocklounge.mc.vholos.protoTools.Runnable
import club.blocklounge.mc.vholos.utilities.Hologram
import club.blocklounge.mc.vholos.utilities.Message
import club.blocklounge.mc.vholos.utilities.Tasks
import club.blocklounge.mc.vholos.utilities.Utils
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class vholos : JavaPlugin() {
    companion object {
        lateinit var logger: Logger
        lateinit var protocolManager: ProtocolManager
        lateinit var instance: Plugin
        lateinit var audience: BukkitAudiences
        lateinit var hologramUtils: Hologram
        lateinit var runnable: Runnable


        fun getAPI():VholosAPI {
            return VholosAPI()
        }

        //TODO(API I HAVE NO IDEA!!!)
        val wrapper1_17 = club.blocklounge.mc.vholos.compat.wrapper.Wrapper1_17()
        val messageManager = Message()
        val taskManager = Tasks()
        val utilManager = Utils()
    }


    private val logger: Logger = LoggerFactory.getLogger("vholos")

    override fun onEnable() {
        registerCommands()
        defineLateInits()

        runnable.runHologram()

        Bukkit.getConsoleSender().sendMessage("Welcome ~ vholos")
        Bukkit.getConsoleSender().sendMessage("Starting up!")
    }
    
    override fun onDisable() {
        Bukkit.getScheduler().cancelTask(Runnable.holoTaskId)
        Bukkit.getConsoleSender().sendMessage("Goodbye ~ vholos")
    }

    fun defineLateInits() {
        Companion.runnable = Runnable()
        Companion.audience = BukkitAudiences.create(Bukkit.getPluginManager().getPlugin("vholos")!!)
        Companion.logger = LoggerFactory.getLogger("vholos")
        Companion.protocolManager = ProtocolLibrary.getProtocolManager()
        Companion.instance = Bukkit.getPluginManager().getPlugin("vholos")!!
        Companion.hologramUtils = Hologram()
    }

    fun registerCommands() {
        getCommand("vholos")!!.setExecutor(club.blocklounge.mc.vholos.command.executor.vholos())
        getCommand("vholos")!!.tabCompleter = club.blocklounge.mc.vholos.command.completers.vholos()
    }




}