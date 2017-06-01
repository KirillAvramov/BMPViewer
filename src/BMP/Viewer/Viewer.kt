package BMP.Viewer

import BMP.Controller.Controller
import BMP.Model.Image
import java.util.*
import javax.swing.JFrame

class Viewer : Observer {

    private val controller = Controller(this)
    private val frame = JFrame()
    init {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }
    private val drawer = Drawer(frame)

    override fun update(obs: Observable?, arg: Any?) {
        obs as Image
        frame.title = obs.fileName
        drawer.draw(obs)
        frame.isVisible = true
    }

    fun drawImage(filePath: String) {
        controller.openFile(filePath)
    }
}
