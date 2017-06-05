package retrain

import java.io.File

fun quantizeModel(quantizeModelScriptFile: File, optimizedGraphFile: File,
                  quantizedGraphFile: File) {
    downloadFile(QUANTIZE_MODEL_SCRIPT_URL, quantizeModelScriptFile, true)

    println("Quantizing Model...")

    val pb = ProcessBuilder("python", quantizeModelScriptFile.path,
            "--input", optimizedGraphFile.path,
            "--output", quantizedGraphFile.path,
            "--output_node_names", "final_result",
            "--mode", "weights_rounded")

    pb.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    pb.start().waitFor()

    println("Done.")
}
