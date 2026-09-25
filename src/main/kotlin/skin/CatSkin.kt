package dev.cdh.skin

@JvmRecord
data class CatSkin(val id: String, val displayName: String) {
    fun resourceRoot(): String = id
}