package retrain

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

val DEFAULT_FPS = 5
val EXTENSION_VIDEO = "mp4"
val EXTENSION_IMAGE = "jpg"
val RETRAINING_SCRIPT_URL = "https://raw.githubusercontent.com/tensorflow/tensorflow/master/tensorflow/examples/image_retraining/retrain.py"
val RETRAINING_SCRIPT_FILE_NAME = "Retraining Script.py"

fun main(args: Array<String>) {
    if (args.isEmpty() || args[0].isEmpty()) {
        throw IllegalArgumentException("Please provide a folder path!")
    }

    val fps = if (args.size > 1) args[1].toInt() else DEFAULT_FPS

    val rootDirectory = File(args[0])

    if (!rootDirectory.isDirectory) {
        throw IllegalArgumentException("The provided path is not a folder!")
    }

    generateImagesFromVideos(rootDirectory, fps)

    downloadRetrainingScript(rootDirectory)
}

fun generateImagesFromVideos(rootDirectory: File, fps: Int) {
    val imageGenerator = ImageGenerator(fps)

    rootDirectory.listFiles()
            .filter { childDirectory -> childDirectory.isDirectory }
            .flatMap { childDirectory -> childDirectory.listFiles().asIterable() }
            .filter { file ->
                file != null && file.isFile && file.extension.toLowerCase()
                        .contentEquals(EXTENSION_VIDEO)
            }
            .forEach { file -> imageGenerator.generate(file) }
}

fun downloadRetrainingScript(rootDirectory: File) {
    print("Downloading $RETRAINING_SCRIPT_FILE_NAME...")
    val client = OkHttpClient()
    val request = Request.Builder().url(RETRAINING_SCRIPT_URL).build()
    val response = client.newCall(request).execute()
    if (!response.isSuccessful) {
        throw IOException("Failed to download $RETRAINING_SCRIPT_FILE_NAME!\n$response")
    }

    val retrainingScriptFile = File(rootDirectory, RETRAINING_SCRIPT_FILE_NAME)
    if (retrainingScriptFile.exists()) retrainingScriptFile.delete()

    val fos = FileOutputStream(retrainingScriptFile)
    fos.write(response.body()?.bytes())
    fos.close()
    println(" Done.")
}
