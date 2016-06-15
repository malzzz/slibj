package co.quine.slibj;

import java.io.*;

public class LoadLib {

    private LoadLib() {}

    public static void fromJar(String path) throws IOException {

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Absolute path required");
        }

        String[] parts = path.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;

        String prefix = "";
        String suffix = null;
        if (filename != null) {
            parts = filename.split("\\.", 2);
            prefix = parts[0];
            suffix = (parts.length > 1) ? "."+parts[parts.length - 1] : null; // Thanks, davs! :-)
        }

        if (filename == null || prefix.length() < 3) {
            throw new IllegalArgumentException("(Filename.length > 3) must evaluate to true");
        }

        File temp = File.createTempFile(prefix, suffix);
        temp.deleteOnExit();

        if (!temp.exists()) {
            throw new FileNotFoundException("File " + temp.getAbsolutePath() + " does not exist");
        }

        byte[] buffer = new byte[1024];
        int readBytes;

        InputStream is = LoadLib.class.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR");
        }

        OutputStream os = new FileOutputStream(temp);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            os.close();
            is.close();
        }

        System.load(temp.getAbsolutePath());
    }
}