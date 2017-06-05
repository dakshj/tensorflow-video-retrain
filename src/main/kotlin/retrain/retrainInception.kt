package retrain

import java.io.File

fun retrainInception(retrainingScriptFile: File, bottlenecksDir: File,
                     modelDir: File, trainingSummariesDir: File,
                     retrainedGraphFile: File, retrainedLabelsFile: File,
                     imagesDirectory: File) {
    downloadFile(RETRAINING_SCRIPT_URL, retrainingScriptFile, true)

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
