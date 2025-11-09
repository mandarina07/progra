package com.example.proyecto.servicios

import com.example.proyecto.dominio.Boleta
import com.example.proyecto.dominio.Cliente
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.PDPageContentStream
import java.io.ByteArrayOutputStream

/**
 * Pdf service
 *
 * @constructor Create empty Pdf service
 */// Servicio que genera un PDF con las boletas
class PdfService {

    /**
     * Generar boletas p d f
     *
     * @param boletas
     * @param clientes
     * @return
     */// Genera un PDF con las boletas dadas
    fun generarBoletasPDF(
        boletas: List<Boleta>,
        clientes: Map<String, Cliente>
    ): ByteArray {

        if (boletas.isEmpty()) return ByteArray(0)

        val table = buildBoletasPdfTable(boletas, clientes)

        PDDocument().use { document ->
            val page = PDPage(PDRectangle.LETTER)
            document.addPage(page)

            val margin = 40f
            val pageWidth = page.mediaBox.width
            val pageHeight = page.mediaBox.height

            val tableWidth = pageWidth - 2 * margin
            val rowHeight = 18f
            val colCount = table.headers.size
            val colWidth = tableWidth / colCount

            val titleY = pageHeight - margin

            PDPageContentStream(document, page).use { cs ->

                // Título del documento
                cs.beginText()
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16f)
                cs.newLineAtOffset(margin, titleY)
                cs.showText("CGE - Boletas Mensuales")
                cs.endText()

                var currentY = titleY - 30f

                // Encabezados de la tabla
                cs.setNonStrokingColor(220, 230, 250)
                cs.addRect(margin, currentY - rowHeight, tableWidth, rowHeight)
                cs.fill()

                cs.setStrokingColor(0, 0, 0)
                cs.addRect(margin, currentY - rowHeight, tableWidth, rowHeight)
                cs.stroke()

                cs.setNonStrokingColor(0, 0, 0)
                cs.setFont(PDType1Font.HELVETICA_BOLD, 9f)

                for ((i, header) in table.headers.withIndex()) {
                    val textX = margin + i * colWidth + 2f
                    val textY = currentY - rowHeight + 5f
                    cs.beginText()
                    cs.newLineAtOffset(textX, textY)
                    cs.showText(header)
                    cs.endText()
                }

                currentY -= rowHeight

                // Filas de la tabla
                cs.setFont(PDType1Font.HELVETICA, 9f)

                for ((rowIndex, row) in table.rows.withIndex()) {

                    if (rowIndex % 2 == 0) {
                        cs.setNonStrokingColor(245, 245, 245)
                        cs.addRect(margin, currentY - rowHeight, tableWidth, rowHeight)
                        cs.fill()
                    }

                    cs.setStrokingColor(0, 0, 0)
                    cs.addRect(margin, currentY - rowHeight, tableWidth, rowHeight)
                    cs.stroke()

                    cs.setNonStrokingColor(0, 0, 0)
                    for ((i, cell) in row.withIndex()) {
                        val textX = margin + i * colWidth + 2f
                        val textY = currentY - rowHeight + 5f
                        cs.beginText()
                        cs.newLineAtOffset(textX, textY)
                        cs.showText(cell.take(20))
                        cs.endText()
                    }

                    currentY -= rowHeight
                }

                // Total general al final del documento
                val totalGeneral = boletas.sumOf { it.detalle.total }

                currentY -= 15f
                cs.beginText()
                cs.setFont(PDType1Font.HELVETICA_BOLD, 11f)
                cs.newLineAtOffset(margin, currentY)
                cs.showText("Total general: %.0f".format(totalGeneral))
                cs.endText()
            }

            val out = ByteArrayOutputStream()
            document.save(out)
            return out.toByteArray()
        }
    }
}
