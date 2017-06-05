package retrain

import java.io.File

fun main(args: Array<String>) {
    validateMainArgs(args)

    val tensorFlowDir = File(args[0])
    val fps = if (args.size > 1) args[1].toInt() else DEFAULT_FPS
    val deleteVideo = if (args.size > 2) args[2].toBoolean() else false

    val trainingDataDir = File(tensorFlowDir, TRAINING_DATA_DIR_NAME)
    val retrainingScriptFile = File(tensorFlowDir, RETRAIN_MODEL_SCRIPT_FILE_NAME)

    val trainingSummariesRootDir = File(tensorFlowDir, TRAINING_SUMMARIES_ROOT_DIR_NAME)
    trainingSummariesRootDir.mkdirs()
    val trainingSummariesChildDir = File(trainingSummariesRootDir, TRAINING_SUMMARIES_CHILD_DIR_NAME)
    val retrainedGraphFile = File(tensorFlowDir, RETRAINED_GRAPH_FILE_NAME)

    generateImagesFromVideos(trainingDataDir, fps, deleteVideo = deleteVideo)

    retrainModel(retrainingScriptFile,
            File(tensorFlowDir, BOTTLENECKS_DIR_NAME),
            File(tensorFlowDir, MODEL_DIR_NAME),
            trainingSummariesChildDir,
            retrainedGraphFile,
            File(tensorFlowDir, RETRAINED_LABELS_FILE_NAME),
            trainingDataDir)

    val optimizedGraphFile = File(tensorFlowDir, OPTIMIZED_GRAPH_FILE_NAME)

    optimizeForInference(File(tensorFlowDir, OPTIMIZE_FOR_INFERENCE_SCRIPT_FILE_NAME),
            retrainedGraphFile, optimizedGraphFile)

    quantizeModel(File(tensorFlowDir, QUANTIZE_MODEL_SCRIPT_FILE_NAME), optimizedGraphFile,
            File(tensorFlowDir, QUANTIZED_GRAPH_FILE_NAME))

    launchTensorBoard(trainingSummariesChildDir)
}
