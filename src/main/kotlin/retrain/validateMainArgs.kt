package retrain

import java.io.File
import java.io.IOException

fun validateMainArgs(args: Array<String>) {
    if (args.isEmpty() || args[0].isEmpty()) {
        throw IllegalArgumentException("Please provide a directory path!")
    }

    val tensorFlowDir = File(args[0])

    if (!tensorFlowDir.isDirectory) {
        throw IllegalArgumentException("The provided path is not a directory!")
    }

    if (!tensorFlowDir.canWrite()) {
        throw IOException("Cannot write to ${tensorFlowDir.path}!")
    }
}
