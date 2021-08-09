package club.blocklounge.mc.vholos.command.completers

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.jetbrains.annotations.Nullable

class vholos : @Nullable TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        val results: MutableList<String> = ArrayList()
        if (args.size == 1) {
            results.add("reload")
            results.add("create")
        }
        return results
    }
}