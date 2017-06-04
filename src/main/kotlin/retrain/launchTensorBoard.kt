package retrain

import java.awt.Desktop
import java.io.File
import java.net.URI

fun launchTensorBoard(tensorBoardDirectory: File) {
    // TODO TensorBoard seems to not be working as of now. Shows no data on its web interface.

    print("Launching TensorBoard...")
    ProcessBuilder("tensorboard",
            "--logdir", tensorBoardDirectory.path)
    println(" Done.")

    println("Opening TensorBoard in the browser...")

    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(TENSORBOARD_URL))
    }
}
