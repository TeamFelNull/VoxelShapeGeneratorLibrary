package dev.felnull.vsgl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {
    public static byte[] streamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = stream.read(buffer);
            if (len < 0) {
                break;
            }
            bout.write(buffer, 0, len);
        }
        return bout.toByteArray();
    }
}
