package com.example.sparica.reporting

import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.ByteArrayOutputStream

object ReportUtils {
    private lateinit var createFileLauncherCSV: ActivityResultLauncher<String>
    private lateinit var createFileLauncherPDF: ActivityResultLauncher<String>
    private var currentFileContent: ByteArray? = null
    private lateinit var activity:ComponentActivity

    fun registerCreateFileLauncher(activity: ComponentActivity) {
        this.activity = activity
        createFileLauncherCSV = activity.registerForActivityResult(
            ActivityResultContracts.CreateDocument("text/csv")
        ) { uri: Uri? ->
            uri?.let { currentUri ->
                currentFileContent?.let { content ->
                    activity.contentResolver.openOutputStream(currentUri)?.use { outputStream ->
                        outputStream.write(content)
                        outputStream.flush()
                        Toast.makeText(activity, "CSV file saved successfully", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        createFileLauncherPDF = activity.registerForActivityResult(
            ActivityResultContracts.CreateDocument("application/pdf")
        ) { uri: Uri? ->
            uri?.let { currentUri ->
                currentFileContent?.let { content ->
                    activity.contentResolver.openOutputStream(currentUri)?.use { outputStream ->
                        outputStream.write(content)
                        outputStream.flush()
                        Toast.makeText(activity, "PDF file saved successfully", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // This function will handle PDF content generation
    fun generatePdfContent(content: String): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        // Add content to the PDF
        document.add(Paragraph(content))
        // You can add more complex content here

        document.close()
        return outputStream.toByteArray()
    }

    // This function will handle CSV content generation
    fun generateCsvContent(content: String): ByteArray {
        return content.toByteArray() // Simply convert the string content to a byte array
    }

    fun createFile(fileName: String, stringContent:String) {
        val extension = fileName.split(".").lastOrNull() ?: "csv"
        //currentFileContent = contentGenerator()
        when (extension) {
            "pdf" -> {
                currentFileContent = generatePdfContent(stringContent)
                createFileLauncherPDF.launch(fileName)
            }
            "csv" -> {
                currentFileContent = generateCsvContent(stringContent)
                createFileLauncherCSV.launch(fileName)
            }
            else -> Toast.makeText(this.activity, "Unsupported file type", Toast.LENGTH_LONG).show()
        }
    }
}