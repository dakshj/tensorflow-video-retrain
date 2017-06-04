package retrain

import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI

val DEFAULT_FPS = 5
val EXTENSION_VIDEO = "mp4"
val EXTENSION_IMAGE = "jpg"
val RETRAINING_SCRIPT_URL = "https://raw.githubusercontent.com/tensorflow/tensorflow/master/tensorflow/examples/image_retraining/retrain.py"

val TRAINING_DATA_DIR_NAME = "Training Data"
val RETRAINING_SCRIPT_FILE_NAME = "Retraining Script.py"
val BOTTLENECKS_DIR_NAME = "Bottlenecks"
val INCEPTION_MODEL_DIR_NAME = "Inception Model"
val TRAINING_SUMMARIES_DIR_NAME = "TensorBoard Summaries"
val RETRAINED_GRAPH_FILE_NAME = "Retrained Graph.pb"
val RETRAINED_LABELS_FILE_NAME = "Retrained Labels.txt"

val TENSORBOARD_URL = "http://localhost:6006"

fun main(args: Array<String>) {
    if (args.isEmpty() || args[0].isEmpty()) {
        throw IllegalArgumentException("Please provide a directory path!")
    }

    val fps = if (args.size > 1) args[1].toInt() else DEFAULT_FPS

    val tensorFlowDir = File(args[0])

    if (!tensorFlowDir.isDirectory) {
        throw IllegalArgumentException("The provided path is not a directory!")
    }

    if (!tensorFlowDir.canWrite()) {
        throw IOException("Cannot write to ${tensorFlowDir.path}!")
    }

    val trainingDataDir = File(tensorFlowDir, TRAINING_DATA_DIR_NAME)
    val retrainingScriptFile = File(tensorFlowDir, RETRAINING_SCRIPT_FILE_NAME)

    val trainingSummariesDir = File(tensorFlowDir, TRAINING_SUMMARIES_DIR_NAME)

    generateImagesFromVideos(trainingDataDir, fps)

    downloadRetrainingScript(retrainingScriptFile)

    retrainInception(retrainingScriptFile,
            File(tensorFlowDir, BOTTLENECKS_DIR_NAME),
            File(tensorFlowDir, INCEPTION_MODEL_DIR_NAME),
            trainingSummariesDir,
            File(tensorFlowDir, RETRAINED_GRAPH_FILE_NAME),
            File(tensorFlowDir, RETRAINED_LABELS_FILE_NAME),
            trainingDataDir)

    launchTensorBoard(trainingSummariesDir)
}

fun generateImagesFromVideos(trainingDataDir: File, fps: Int) {
    val imageGenerator = ImageGenerator(fps)

    trainingDataDir.listFiles()
            .filter { childDirectory -> childDirectory.isDirectory }
            .flatMap { childDirectory -> childDirectory.listFiles().asIterable() }
            .filter { file ->
                file != null && file.isFile && file.extension.toLowerCase()
                        .contentEquals(EXTENSION_VIDEO)
            }
            .forEach { file -> imageGenerator.generate(file) }
}

fun downloadRetrainingScript(retrainingScriptFile: File) {
    print("Downloading $RETRAINING_SCRIPT_FILE_NAME...")
    val client = OkHttpClient()
    val request = Request.Builder().url(RETRAINING_SCRIPT_URL).build()
    val response = client.newCall(request).execute()
    if (!response.isSuccessful) {
        throw IOException("Failed to download $RETRAINING_SCRIPT_FILE_NAME!\n$response")
    }

    val fos = FileOutputStream(retrainingScriptFile, false)
    fos.write(response.body()?.bytes())
    fos.close()
    println(" Done.")
}

fun retrainInception(retrainingScriptFile: File, bottlenecksDir: File,
                     modelDir: File, trainingSummariesDir: File,
                     retrainedGraphFile: File, retrainedLabelsFile: File,
                     imagesDirectory: File) {
    println("Retraining Inception...")

    val pb = ProcessBuilder("python", retrainingScriptFile.path,
            "--bottleneck_dir", bottlenecksDir.path,
            "--model_dir", modelDir.path,
            "--summaries_dir", trainingSummariesDir.path,
            "--output_graph", retrainedGraphFile.path,
            "--output_labels", retrainedLabelsFile.path,
            "--image_dir", imagesDirectory.path)

    pb.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    pb.start().waitFor()

    println("Done.")
}

fun launchTensorBoard(tensorBoardDirectory: File) {
    print("Launching TensorBoard...")
    ProcessBuilder("tensorboard",
            "--logdir", tensorBoardDirectory.path)
    println(" Done.")

    println("Opening TensorBoard in the browser...")

    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(TENSORBOARD_URL))
    }
}
