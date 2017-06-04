package retrain

import java.io.File

fun generateImagesFromVideos(trainingDataDir: File, fps: Int, deleteVideo: Boolean) {
    trainingDataDir.listFiles()
            .filter { it.isDirectory }
            .flatMap { it.listFiles().asIterable() }
            .filter {
                it != null && it.isFile && it.extension.toLowerCase()
                        .contentEquals(EXTENSION_VIDEO)
            }
            .forEach { generateImageFromVideo(it, fps, deleteVideo) }
}

private fun generateImageFromVideo(file: File, fps: Int, deleteVideo: Boolean) {
    println("Generating images for " + file.name + "...")

    val pb = ProcessBuilder("ffmpeg",

            // Image
            "-i",

            // Input file path
            file.path,

            // Overwrite files flag
            "-y",

            // Disable audio flag
            "-an",

            // No quality loss flag
            "-q", "0",

            // FPS flag
            "-vf", "fps=" + fps,

            //Output file path
            File(file.parentFile.path,
                    file.nameWithoutExtension + "_%06d.$EXTENSION_IMAGE").path)

    pb.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    pb.start().waitFor()

    if (deleteVideo && file.delete()) {
        println("Deleted ${file.name}.")
    }

    println("Done.")
}
