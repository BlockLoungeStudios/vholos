package club.blocklounge.mc.vholos.command.executor

import club.blocklounge.mc.vholos.protoTools.Runnable
import club.blocklounge.mc.vholos.vholos
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
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
            vholos.hologramUtils.clearTotalHologramView()
            vholos.taskManager.cancelTask(Runnable.holoTaskId)
            vholos.logger.info(" ~ Cancelled task")

            Runnable.cacheCreate.clear()
            Runnable.cacheMeta.clear()
            Runnable.cacheDelete.clear()

            Runnable.utilities.reloadOrLoadHologramList()

            Runnable().runHologram()
            vholos.logger.info(" ~ Refreshed cache")

            vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] Reloaded!"))
            vholos.logger.info("Reloading complete")
        }
        else {
            vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] You do not have permission"))
        }
    }

    private fun create(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        if (sender.hasPermission("vholos.create")) {
            //TO BE MADE!!! THIS COMMAND DOESN'T WORK
//            vholos.hologramUtils.clearTotalHologramView()
//            vholos.taskManager.cancelTask(Runnable.holoTaskId)
//            Runnable().runHologram()
//            vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] Reloaded!"))
        }
        else {
            vholos.messageManager.sendMessage(sender, vholos.messageManager.createMessage("<red>[vholos] You do not have permission"))
        }
    }

}