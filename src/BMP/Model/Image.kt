package BMP.Model

import java.awt.image.BufferedImage
import java.util.*

interface Image {

    var fileName: String
    var image: BufferedImage

    var width: Int
    var height: Int

    fun makeImage(byteArray: ByteArray)

    fun addObserver(obs: Observer)
}