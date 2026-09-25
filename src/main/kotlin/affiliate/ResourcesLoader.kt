package dev.cdh.affiliate

import dev.cdh.ImageCache
import dev.cdh.Behave
import dev.cdh.BubbleState
import dev.cdh.skin.CatSkin
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO

class ResourcesLoader(skin: CatSkin?) {
    private val selectedCatType = Objects.requireNonNull<CatSkin?>(skin)!!.resourceRoot()

    fun catType(): String {
        return selectedCatType
    }

    fun loadFrames(behave: Behave): MutableList<BufferedImage?>? {
        val cacheKey = selectedCatType + ":" + behave.name
        return ImageCache.getOrLoadFrames(cacheKey) {
            loadFramesInternal(
                behave.name.lowercase(Locale.getDefault()),
                behave.frame,
                CatWindow.WINDOW_SIZE
            )
        }
    }

    fun loadBubbleFrames(state: BubbleState?): MutableList<BufferedImage?>? {
        if (state == BubbleState.NONE) {
            return mutableListOf()
        }
        val cacheKey = "bubble:" + state!!.name
        return ImageCache.getOrLoadFrames(
            cacheKey
        ) {
            loadFramesInternal(
                state.name.lowercase(Locale.getDefault()),
                state.frame,
                CatWindow.BUBBLE_SIZE
            )
        }
    }

    private fun loadFramesInternal(actionName: String?, frameCount: Int, targetSize: Int): MutableList<BufferedImage?> {
        val frames: MutableList<BufferedImage?> = ArrayList<BufferedImage?>(frameCount)
        val basePath = selectedCatType + "/" + actionName
        for (i in 1..frameCount) {
            val path = String.format("%s/%s_%d.png", basePath, actionName, i)
            frames.add(prepareImage(loadImage(path)!!, targetSize))
        }
        return frames
    }

    private fun prepareImage(image: BufferedImage, targetSize: Int): BufferedImage {
        if (image.getType() == BufferedImage.TYPE_INT_ARGB && image.getWidth() == targetSize && image.getHeight() == targetSize) {
            return image
        }
        val prepared = BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB)
        val g2d = prepared.createGraphics()
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
        g2d.drawImage(image, 0, 0, targetSize, targetSize, null)
        g2d.dispose()
        return prepared
    }

    private fun loadImage(path: String?): BufferedImage? {
        try {
            javaClass.classLoader.getResourceAsStream(path).use { stream ->
                return ImageIO.read(Objects.requireNonNull<InputStream?>(stream, "Missing resource: " + path))
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to load: $path", e)
        }
    }
}
