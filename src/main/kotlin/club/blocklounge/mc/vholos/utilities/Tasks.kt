package club.blocklounge.mc.vholos.utilities

import club.blocklounge.mc.vholos.Vholos
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class Tasks {

    fun task(task: Runnable):BukkitTask {
        return Bukkit.getScheduler().runTask(Vholos.instance, task)
    }

    fun asyncTask(task: Runnable):BukkitTask {
        return Bukkit.getScheduler().runTaskAsynchronously(Vholos.instance, task)
    }

    fun laterAsyncTask(delay: Long, task: Runnable):BukkitTask {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Vholos.instance, task, delay)
    }

    fun asyncTimer(period: Long, delay: Long?, task: Runnable?): BukkitTask {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Vholos.instance, task!!, delay!!, period)
    }

    fun asyncTimer(period: Long, task: Runnable?): BukkitTask {
        return asyncTimer(period, 0L, task)
    }

    fun cancelTask(taskID: Int) {
        Bukkit.getScheduler().cancelTask(taskID)
    }
}