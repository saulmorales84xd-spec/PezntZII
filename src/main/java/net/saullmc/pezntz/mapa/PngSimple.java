package net.saullmc.pezntz.mapa;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PngSimple {

    private static final byte[] FIRMA = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};

    public static byte[] codificar(int[] rgb, int ancho, int alto) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream(Math.max(1024, ancho * alto));
        DataOutputStream datos = new DataOutputStream(salida);

        datos.write(FIRMA);

        ByteArrayOutputStream cabecera = new ByteArrayOutputStream();
        DataOutputStream c = new DataOutputStream(cabecera);
        c.writeInt(ancho);
        c.writeInt(alto);
        c.writeByte(8);
        c.writeByte(2);
        c.writeByte(0);
        c.writeByte(0);
        c.writeByte(0);
        trozo(datos, "IHDR", cabecera.toByteArray());

        ByteArrayOutputStream comprimido = new ByteArrayOutputStream(Math.max(1024, ancho * alto));
        Deflater deflater = new Deflater(6);

        try (DeflaterOutputStream z = new DeflaterOutputStream(comprimido, deflater, 1 << 16)) {
            byte[] fila = new byte[1 + ancho * 3];

            for (int y = 0; y < alto; y++) {
                fila[0] = 1;

                int base = y * ancho;
                int pr = 0;
                int pg = 0;
                int pb = 0;

                for (int x = 0; x < ancho; x++) {
                    int color = rgb[base + x];

                    int r = (color >> 16) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;

                    int i = 1 + x * 3;
                    fila[i] = (byte) (r - pr);
                    fila[i + 1] = (byte) (g - pg);
                    fila[i + 2] = (byte) (b - pb);

                    pr = r;
                    pg = g;
                    pb = b;
                }

                z.write(fila);
            }
        } finally {
            deflater.end();
        }

        trozo(datos, "IDAT", comprimido.toByteArray());
        trozo(datos, "IEND", new byte[0]);

        datos.flush();
        return salida.toByteArray();
    }

    private static void trozo(DataOutputStream datos, String tipo, byte[] contenido) throws IOException {
        byte[] nombre = tipo.getBytes(StandardCharsets.US_ASCII);

        datos.writeInt(contenido.length);
        datos.write(nombre);
        datos.write(contenido);

        CRC32 crc = new CRC32();
        crc.update(nombre);
        crc.update(contenido);
        datos.writeInt((int) crc.getValue());
    }

    public static long crc(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }
}