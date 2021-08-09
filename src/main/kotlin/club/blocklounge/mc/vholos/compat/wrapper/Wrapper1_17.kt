package club.blocklounge.mc.vholos.compat.wrapper

import java.lang.reflect.Field
import java.util.concurrent.atomic.AtomicInteger


class Wrapper1_17 {

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
}