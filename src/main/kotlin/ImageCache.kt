package dev.cdh

import java.awt.image.BufferedImage

internal object ImageCache {
    private val FRAME_CACHE: MutableMap<String?, MutableList<BufferedImage?>?> =
        HashMap()
    private val FLIP_CACHE: MutableMap<String?, BufferedImage> = HashMap()

    fun getOrLoadFrames(key: String?, loader: ()-> MutableList<BufferedImage?>): MutableList<BufferedImage?>? {
        return FRAME_CACHE.computeIfAbsent(key) { loader() }
    }

    fun getOrFlip(original: BufferedImage, key: String?): BufferedImage {
        return FLIP_CACHE.computeIfAbsent(key) { flipImage(original) }
    }

    private fun flipImage(source: BufferedImage): BufferedImage {
        val flipped = BufferedImage(source.width, source.height, BufferedImage.TYPE_INT_ARGB)
        val g2d = flipped.createGraphics()
        g2d.drawImage(source, source.width, 0, -source.width, source.height, null)
        g2d.dispose()
        return flipped
    }
}