package club.blocklounge.mc.vholos.command.executor

import club.blocklounge.mc.vholos.Vholos
import club.blocklounge.mc.vholos.protoTools.Records
import club.blocklounge.mc.vholos.protoTools.Runnable
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.jetbrains.annotations.Nullable

class vholos : @Nullable CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.isEmpty() -> {
                Vholos.messageManager.sendMessage(sender, Vholos.messageManager.createMessage("<red>[Vholos] Select a sub option!"))
            }
            args.isNotEmpty() -> {
                when {
                    args[0] == "reload" -> {
                        reload(sender)
                    }
                    args[0] == "create" -> {
                        create(sender, command, label, args)
                    }
                    else -> {
                        Vholos.messageManager.sendMessage(sender, Vholos.messageManager.createMessage("<red>[Vholos] Select a valid sub option!"))
                    }
                }
            }
        }
        return true
    }

    private fun reload(sender: CommandSender) {
        if (sender.hasPermission("Vholos.reload")) {
            Vholos.logging.info("Reloading started")
            Vholos.taskManager.cancelTask(Runnable.holoTaskId)
            Vholos.logging.info(" ~ Cancelled task")

            Runnable.cacheCreate.clear()
            Runnable.cacheMeta.clear()
            Runnable.cacheDelete.clear()
            Runnable.utilities.clearTotalHologramView()
            Vholos.logging.info(" ~ Refreshed cache ~")

            Runnable.utilities.reloadOrLoadHologramList()
            Vholos.logging.info(" ~ Reloaded holograms from lists")

            Vholos.runnable = Runnable()
            Vholos.runnable.runHologram()
            Vholos.logging.info(" ~ Created new instance and ran it")

            Vholos.messageManager.sendMessage(sender, Vholos.messageManager.createMessage("<red>[Vholos] Reloaded!"))
            Vholos.logging.info("Reloading complete")
        }
        else {
            Vholos.messageManager.sendMessage(sender, Vholos.messageManager.createMessage("<red>[Vholos] You do not have permission"))
        }
    }

    private fun create(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        if (sender.hasPermission("Vholos.create") && sender is Player) {
            Runnable.utilities.addHologramToAPIList(Records.GeneralHologramInformation(sender.name, listOf("Test"), sender.location, 2.24, 35))
        }
        else {
            Vholos.messageManager.sendMessage(sender, Vholos.messageManager.createMessage("<red>[Vholos] You do not have permission"))
        }
    }

}