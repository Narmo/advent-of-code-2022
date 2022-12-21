import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import javax.imageio.ImageIO

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

fun <T> Collection<T>.second() = this.elementAt(1)

// https://stackoverflow.com/a/55012758/318460
fun textToGraphics(text: String, fileName: String) {
	val helperImg = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
	var g2d = helperImg.createGraphics()
	val font = Font("Courier", Font.PLAIN, 48)

	g2d.font = font

	var fm = g2d.fontMetrics

	var longestText = ""

	val textRows = text.split("\n")

	for (row in textRows) {
		if (row.length > longestText.length) {
			longestText = row
		}
	}

	val width = fm.stringWidth(longestText)
	val height = fm.height * textRows.size
	g2d.dispose()

	val finalImg = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
	g2d = finalImg.createGraphics()
	g2d.color = Color.WHITE
	g2d.fillRect(0, 0, width, height)
	g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY)
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
	g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
	g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE)
	g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
	g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
	g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
	g2d.font = font
	fm = g2d.fontMetrics
	g2d.color = Color.BLACK

	var y = fm.ascent

	for (row in textRows) {
		g2d.drawString(row, 0, y)
		y += fm.height
	}

	g2d.dispose()

	try {
		ImageIO.write(finalImg, "png", File("$fileName.png"))
	}
	catch (e: IOException) {
		e.printStackTrace()
	}
}

fun benchmark(block: () -> Unit) {
	val start = System.currentTimeMillis()
	block()
	val end = System.currentTimeMillis()
	println("Took ${end - start}ms")
}
