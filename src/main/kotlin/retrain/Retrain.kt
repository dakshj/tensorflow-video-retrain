package retrain

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty() || args[0].isEmpty()) {
        throw IllegalArgumentException("Please provide a folder path!")
    }

    val fps = if (args.size > 1) args[1].toInt() else 5

    val rootDirectory = File(args[0])

    if (!rootDirectory.isDirectory) {
        throw IllegalArgumentException("The provided path is not a folder!")
    }
}
