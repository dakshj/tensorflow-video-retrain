package retrain

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.GZIPOutputStream

fun gzipFile(file: File, gzippedFile: File) {
    print("GZipping ${file.name}...")

    val fileInputStream = FileInputStream(file)
    val gzipOutputStream = GZIPOutputStream(FileOutputStream(gzippedFile))
    fileInputStream.copyTo(gzipOutputStream)

    fileInputStream.close()
    gzipOutputStream.finish()
    gzipOutputStream.close()

    println(" Done.")
}
