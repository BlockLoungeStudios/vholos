package club.blocklounge.mc.vholos.command.executor

import club.blocklounge.mc.vholos.protoTools.Records
import club.blocklounge.mc.vholos.protoTools.Runnable
import club.blocklounge.mc.vholos.vholos
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.jetbrains.annotations.Nullable

class vholos : @Nullable CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.isEmpty() -> {
                vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] Select a sub option!"))
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
                        vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] Select a valid sub option!"))
                    }
                }
            }
        }
        return true
    }

    private fun reload(sender: CommandSender) {
        if (sender.hasPermission("vholos.reload")) {
            vholos.logger.info("Reloading started")
            vholos.taskManager.cancelTask(Runnable.holoTaskId)
            vholos.logger.info(" ~ Cancelled task")

            Runnable.cacheCreate.clear()
            Runnable.cacheMeta.clear()
            Runnable.cacheDelete.clear()
            Runnable.utilities.clearTotalHologramView()
            vholos.logger.info(" ~ Refreshed cache")

            Runnable.utilities.reloadOrLoadHologramList()
            vholos.logger.info(" ~ Reloaded holograms from lists")

            vholos.runnable = Runnable()
            vholos.runnable.runHologram()
            vholos.logger.info(" ~ Created new instance and ran it")

            vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] Reloaded!"))
            vholos.logger.info("Reloading complete")
        }
        else {
            vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] You do not have permission"))
        }
    }

    private fun create(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        if (sender.hasPermission("vholos.create") && sender is Player) {
            Runnable.utilities.addHologramToAPIList(Records.GeneralHologramInformation(sender.name, listOf("%tab_placeholder_animation:scoreboardheader2%"), sender.location, 2.24, 35))
        }
        else {
            vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] You do not have permission"))
        }
    }

}