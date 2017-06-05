package retrain

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun downloadFile(url: String, destinationFile: File, printToConsole: Boolean) {
    if (printToConsole) print("Downloading ${destinationFile.name}...")

    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    val response = client.newCall(request).execute()
    if (!response.isSuccessful) {
        throw IOException("Failed to download ${destinationFile.name}!\n$response")
    }

    val fos = FileOutputStream(destinationFile, false)
    fos.write(response.body()?.bytes())
    fos.close()

    if (printToConsole) println(" Done.")
}
