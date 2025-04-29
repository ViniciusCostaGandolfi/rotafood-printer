package br.com.rotafood.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import utils.printText
import javax.print.DocFlavor
import javax.print.PrintServiceLookup
import javax.print.SimpleDoc

@Composable
fun PrinterTestButton(
    selectedPrinter: String?,
    pageWidth: String,
    pageHeight: String,
    fontSize: String,
    pageMargin: String,
    spacingBetweenPrints: String
) {
    val sampleText = """
        ---------------------
        
        --- COMANDA TESTE ---
        
        Pedido: 1234
        
        Data: 27/04/2025
        
        Canal: APP
        
        Tipo: Entrega
        
        Status: Confirmado
        
        ---------------------
        
        Entrega: Rua Exemplo, 123 - Centro
        
        ---------------------
        
        (2) Hambúrguer R$ 25,00
        
           - Bacon R$ 5,00
           
           - Queijo Extra R$ 3,00
           
        (1) Refrigerante R$ 8,00
        
        ---------------------
        
        Total: R$ 123,00
        
        Info extra: Sem dlfkjasdl dlfkjasdl flsdkjfalnl dlisfjslmnl sdlkfj   
        """

    Button(
        onClick = {
            printText(
                printerName = selectedPrinter,
                text        = sampleText,
                widthMm     = pageWidth,
                heightMm    = pageHeight,
                fontSizePt  = fontSize,
                marginMm    = pageMargin,
                spacingPt   = spacingBetweenPrints
            )
        },
        enabled = selectedPrinter != null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF1565C0),
            contentColor    = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    ) { Text("Imprimir Página de Teste") }
}
