package club.blocklounge.mc.vholos.protoTools

import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry.getChatComponentSerializer

class CompatabilityManager {
    companion object {
        private val stringSerialiser: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(String::class.java)
        private val byteSerialiser: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(Byte::class.javaObjectType)
        private val booleanSerialiser: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(Boolean::class.javaObjectType)
        private val chatSerialiser = getChatComponentSerializer(true)
        //val integerSerialiser: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(Int::class.java)
        fun getByteSerialiser(): WrappedDataWatcher.Serializer {
            return byteSerialiser
        }

        fun getstringSerialiser(): WrappedDataWatcher.Serializer {
            return stringSerialiser
        }

        fun getbooleanSerialiser(): WrappedDataWatcher.Serializer {
            return booleanSerialiser
        }

        fun getchatSerialiser(): WrappedDataWatcher.Serializer {
            return chatSerialiser
        }
    }
}