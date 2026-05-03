package dev.cdh.affiliate

import dev.cdh.ImageCache
import dev.cdh.constants.Behave
import dev.cdh.constants.BubbleState
import java.awt.image.BufferedImage
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

class ResourcesLoader {
    private val selectedCatType: String? = CAT_TYPES.random()
    private val localCache: MutableMap<String?, MutableList<BufferedImage?>?> = HashMap()

    fun loadFrames(behave: Behave): MutableList<BufferedImage?>? {
        val cacheKey = "$selectedCatType:${behave.name}"
        var frames = localCache[cacheKey]
        // Check local cache
        if (frames != null) {
            return frames
        }
        // then check global cache
        frames = ImageCache.getOrLoadFrames(
            cacheKey
        ) { loadFramesInternal(behave.name.lowercase(Locale.getDefault()), behave.frame) }
        // Store into local cache
        localCache[cacheKey] = frames
        return frames
    }

    fun loadBubbleFrames(state: BubbleState): MutableList<BufferedImage?>? {
        if (state == BubbleState.NONE) {
            return mutableListOf()
        }
        val cacheKey = "bubble:${state.name}"
        var frames = localCache[cacheKey]
        if (frames != null) {
            return frames
        }
        frames = ImageCache.getOrLoadFrames(cacheKey) {
            val actionName = state.name.lowercase(Locale.getDefault())
            loadFramesInternal(actionName, state.frame)
        }
        localCache[cacheKey] = frames
        return frames
    }

    private fun loadFramesInternal(actionName: String?, frameCount: Int): MutableList<BufferedImage?> {
        val frames = ArrayList<BufferedImage?>(frameCount)
        for (i in 1..frameCount) {
            var image = loadImage("$selectedCatType/$actionName/${actionName}_$i.png")
            if (image.type != BufferedImage.TYPE_INT_ARGB) {
                val converted = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
                val g2d = converted.createGraphics()
                g2d.drawImage(image, 0, 0, null)
                g2d.dispose()
                image = converted
            }
            frames.add(image)
        }
        return frames
    }

    private fun loadImage(path: String?): BufferedImage {
        try {
            javaClass.classLoader.getResourceAsStream(path).use { stream ->
                return ImageIO.read(Objects.requireNonNull(stream))
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to load: $path", e)
        }
    }

    companion object {
        private val CAT_TYPES = mutableListOf<String?>("calico_cat", "grey_tabby_cat", "orange_cat", "white_cat")
    }
}