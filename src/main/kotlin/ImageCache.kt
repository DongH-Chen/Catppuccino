package dev.cdh

import java.awt.image.BufferedImage

object ImageCache {
    val frameCache: MutableMap<String, MutableList<BufferedImage>> =
        HashMap()
    private val flipCache: MutableMap<String, BufferedImage> = HashMap()

    inline fun getOrLoadFrames(key: String, loader: () -> MutableList<BufferedImage>): MutableList<BufferedImage> =
        frameCache.getOrPut(key, loader)

    fun getOrFlip(original: BufferedImage, key: String): BufferedImage =
        flipCache.getOrPut(key) { original.flipImage() }

    private fun BufferedImage.flipImage(): BufferedImage {
        val flipped = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = flipped.createGraphics()
        g2d.drawImage(this, width, 0, -width, height, null)
        g2d.dispose()
        return flipped
    }
}