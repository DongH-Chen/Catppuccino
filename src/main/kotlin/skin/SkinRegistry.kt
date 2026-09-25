package dev.cdh.skin

import java.io.IOException
import java.util.*
import java.util.random.RandomGenerator

class SkinRegistry private constructor(val skins: MutableList<CatSkin?>) {
    private val random: RandomGenerator = RandomGenerator.getDefault()

    fun all(): MutableList<CatSkin?> {
        return skins
    }

    fun randomSkin(): CatSkin? {
        return skins[random.nextInt(skins.size)]
    }

    companion object {
        const val CONFIG_PATH: String = "skins.properties"

        private const val SKINS_KEY = "skins"

        private const val NAME_KEY_PREFIX = "skin."

        fun load(): SkinRegistry {
            val props = Properties()
            try {
                SkinRegistry::class.java.classLoader.getResourceAsStream(CONFIG_PATH).use { stream ->
                    props.load(stream)
                }
            } catch (e: IOException) {
                throw RuntimeException("Failed to load: $CONFIG_PATH", e)
            }
            return fromProperties(props)
        }

        fun fromProperties(props: Properties): SkinRegistry {
            val list = props.getProperty(SKINS_KEY, "").trim { it <= ' ' }
            check(list.isNotEmpty()) { "No skins declared in $CONFIG_PATH" }
            val skins = ArrayList<CatSkin?>()
            for (id in list.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                var id = id
                id = id.trim { it <= ' ' }
                if (id.isEmpty()) {
                    continue
                }
                val displayName = props.getProperty("$NAME_KEY_PREFIX$id.name", id)
                skins.add(CatSkin(id, displayName!!))
            }
            check(skins.isNotEmpty()) { "No skins declared in $CONFIG_PATH" }
            return SkinRegistry(skins)
        }
    }
}
