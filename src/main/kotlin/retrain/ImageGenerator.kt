package retrain

import java.io.File

class ImageGenerator(val fps: Int) {

    fun generate(file: File) {
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

        println("Done.")
    }
}
