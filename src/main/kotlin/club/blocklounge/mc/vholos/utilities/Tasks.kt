package club.blocklounge.mc.vholos.utilities

import club.blocklounge.mc.vholos.vholos
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class Tasks {
    fun asyncTask(task: Runnable):BukkitTask {
        return Bukkit.getScheduler().runTaskAsynchronously(vholos.instance, task)
    }

    fun laterAsyncTask(delay: Long, task: Runnable):BukkitTask {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(vholos.instance, task, delay)
    }

    fun asyncTimer(period: Long, delay: Long?, task: Runnable?): BukkitTask {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(vholos.instance, task!!, delay!!, period)
    }

    fun asyncTimer(period: Long, task: Runnable?): BukkitTask {
        return asyncTimer(period, 0L, task)
    }

    fun cancelTask(taskID: Int) {
        Bukkit.getScheduler().cancelTask(taskID)
    }
}