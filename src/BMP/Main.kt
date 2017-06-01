import BMP.Viewer.Viewer

fun main(args: Array<String> ) {
    val viewer = Viewer()

    if (!args.isEmpty()) viewer.drawImage(args[0])

}