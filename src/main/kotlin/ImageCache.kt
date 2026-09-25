package dev.cdh

import java.awt.image.BufferedImage

object ImageCache {
    private val FRAME_CACHE: MutableMap<String, MutableList<BufferedImage>> =
        HashMap()
    private val FLIP_CACHE: MutableMap<String, BufferedImage> = HashMap()

    fun getOrLoadFrames(key: String, loader: () -> MutableList<BufferedImage>): MutableList<BufferedImage> = FRAME_CACHE.getOrPut(key, loader)

    fun getOrFlip(original: BufferedImage, key: String): BufferedImage = FLIP_CACHE.getOrPut(key) { original.flipImage() }

    private fun BufferedImage.flipImage(): BufferedImage {
        val flipped = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = flipped.createGraphics()
        g2d.drawImage(this, width, 0, -width, height, null)
        g2d.dispose()
        return flipped
    }
}