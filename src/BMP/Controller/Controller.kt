package BMP.Controller

import BMP.Model.BMPMaker
import BMP.Model.Image
import BMP.Viewer.Viewer
import java.nio.file.*

class Controller(private val viewer: Viewer) {

    private var model: Image = BMPMaker()

    fun openFile(filePath: String) {
        val fileName = filePath.split("/").last()
        when (filePath.split(".").last()) {
            "bmp" -> {
                if (model !is BMPMaker) {
                    model = BMPMaker()
                }
            }
            else -> {
                println("Illegal file extension")
                return
            }
        }

        model.fileName = fileName
        val path: Path
        val byteArray: ByteArray
        try {
            path = Paths.get(filePath)
            byteArray = Files.readAllBytes(path)
        }
        catch (exept: NoSuchFileException) {
            println("No such file")
            return
        }

        model.addObserver(viewer)
        try {
            model.makeImage(byteArray)
        }
        catch (exept: IllegalArgumentException) {
            println(exept.message)
            return
        }
    }
}