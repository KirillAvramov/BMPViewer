package BMP.Viewer

import BMP.Model.Image
import javax.swing.JFrame

class Drawer(private val frame: JFrame) {
    fun draw(model: Image) {
        val panel = ImagePanel(model.image)
        panel.setSize(model.width, model.height)

        frame.setBounds(200, 200, model.width, model.height)
        frame.contentPane = panel
    }

}