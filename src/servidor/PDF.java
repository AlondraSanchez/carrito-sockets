/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.util.ArrayList;
import productos.Carrito;
import productos.Producto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author hector
 */
public class PDF {

    public void generarPDF(Carrito c) throws FileNotFoundException, DocumentException {

        double total = 0;
        int i = 0;
        Date date = new Date();
        ArrayList<Producto> carro = c.getCarrito();
        Document documento = new Document();
        FileOutputStream ficheroPdf = new FileOutputStream("recibo.pdf");
        // Se asocia el documento al OutputStream y se indica que el espaciado entre
        // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
        // Se abre el documento.
        documento.open();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        documento.add(new Paragraph("\t\t\tAbarrotes doña lupe\n\n", 
                FontFactory.getFont("arial",   // fuente
		22)));
        documento.add(new Paragraph("Hora y fecha de la compra: "+hourdateFormat.format(date) + "\n\n"));
        PdfPTable tabla = new PdfPTable(4);
        tabla.addCell(new Paragraph("Producto", FontFactory.getFont("helvetica", 12, Font.BOLD)));
        tabla.addCell(new Paragraph("Precio unitario", FontFactory.getFont("helvetica", 12, Font.BOLD)));
        tabla.addCell(new Paragraph("Cantidad", FontFactory.getFont("helvetica", 12, Font.BOLD)));
        tabla.addCell(new Paragraph("Subtotal", FontFactory.getFont("helvetica", 12, Font.BOLD)));
        
        for(Producto prod : carro) {
            tabla.addCell(prod.getNombre());
            tabla.addCell("" + prod.getPrecio());
            tabla.addCell("" + prod.getCantidad());
            tabla.addCell("" + (prod.getCantidad()* prod.getPrecio()));
            total += (prod.getCantidad()* prod.getPrecio());
        }
        documento.add(tabla);
        documento.add(new Paragraph("\n\t\t\t\t\t\t\tTotal: "+ total));
        documento.close();
    }
}
