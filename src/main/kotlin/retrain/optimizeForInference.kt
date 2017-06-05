package retrain

import java.io.File

fun optimizeForInference(optimizationScriptFile: File,
                         retrainedGraphFile: File, optimizedGraphFile: File) {
    downloadFile(OPTIMIZE_FOR_INFERENCE_SCRIPT_URL, optimizationScriptFile, true)

    println("Optimizing Model...")

    val pb = ProcessBuilder("python", optimizationScriptFile.path,
            "--input", retrainedGraphFile.path,
            "--output", optimizedGraphFile.path,
            "--input_names", "Cast",
            "--output_names", "final_result")

    pb.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    pb.start().waitFor()

    println("Done.")
}
