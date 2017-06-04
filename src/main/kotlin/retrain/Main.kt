package retrain

import java.io.File

fun main(args: Array<String>) {
    validateMainArgs(args)

    val tensorFlowDir = File(args[0])
    val fps = if (args.size > 1) args[1].toInt() else DEFAULT_FPS
    val deleteVideo = if (args.size > 2) args[2].toBoolean() else false

    val trainingDataDir = File(tensorFlowDir, TRAINING_DATA_DIR_NAME)
    val retrainingScriptFile = File(tensorFlowDir, RETRAINING_SCRIPT_FILE_NAME)

    val trainingSummariesDir = File(tensorFlowDir, TRAINING_SUMMARIES_DIR_NAME)

    generateImagesFromVideos(trainingDataDir, fps, deleteVideo = deleteVideo)

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

fun downloadRetrainingScript(retrainingScriptFile: File) {
    print("Downloading $RETRAINING_SCRIPT_FILE_NAME...")
    downloadFile(RETRAINING_SCRIPT_URL, retrainingScriptFile)
    println(" Done.")
}
