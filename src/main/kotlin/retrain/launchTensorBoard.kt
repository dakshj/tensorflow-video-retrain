package retrain

import java.awt.Desktop
import java.io.File
import java.net.URI

fun launchTensorBoard(tensorBoardDirectory: File) {
    print("Launching TensorBoard...")
    ProcessBuilder("tensorboard",
            "--logdir", tensorBoardDirectory.path).start()
    println(" Done.")

    println("Opening TensorBoard in the browser...")

    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(TENSORBOARD_URL))
    }
}
