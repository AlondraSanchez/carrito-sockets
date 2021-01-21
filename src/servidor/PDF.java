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

public class PDF {

    public String generarPDF(Carrito c) throws FileNotFoundException, DocumentException {
        String ruta = "docs/recibo.pdf";
        double total = 0;
        Date date = new Date();
        ArrayList<Producto> carro = c.getCarrito();
        Document documento = new Document();
        FileOutputStream ficheroPdf = new FileOutputStream("docs/recibo.pdf");
        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
        documento.open();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        documento.add(new Paragraph("\t\t\tAbarrotes doña lupe\n\n",
                FontFactory.getFont("arial", 22)));
        documento.add(new Paragraph("Hora y fecha de la compra: " + hourdateFormat.format(date) + "\n\n"));
        PdfPTable tabla = new PdfPTable(4);
        tabla.addCell(new Paragraph("Producto", FontFactory.getFont("helvetica", 12, Font.BOLD)));
        tabla.addCell(new Paragraph("Precio unitario", FontFactory.getFont("helvetica", 12, Font.BOLD)));
        tabla.addCell(new Paragraph("Cantidad", FontFactory.getFont("helvetica", 12, Font.BOLD)));
        tabla.addCell(new Paragraph("Subtotal", FontFactory.getFont("helvetica", 12, Font.BOLD)));

        total = carro.stream().map((prod) -> {
            tabla.addCell(prod.getNombre());
            return prod;
        }).map((prod) -> {
            tabla.addCell("" + prod.getPrecio());
            return prod;
        }).map((prod) -> {
            tabla.addCell("" + prod.getCantidad());
            return prod;
        }).map((prod) -> {
            tabla.addCell("" + (prod.getCantidad() * prod.getPrecio()));
            return prod;
        }).map((prod) -> (prod.getCantidad() * prod.getPrecio())).reduce(total, (accumulator, _item) -> accumulator + _item);
        documento.add(tabla);
        documento.add(new Paragraph("\n\t\t\t\t\t\t\tTotal: " + total));
        documento.close();
        return ruta;
    }
}
