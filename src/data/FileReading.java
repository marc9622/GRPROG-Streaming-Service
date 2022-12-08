package data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Finds a file in the resources folder, and returns its contents as a String array.
 * The file is assumed to be encoded in ISO-8859-1.
 */
public class FileReading {
    
    /** Finds a file in the resources folder, and returns its contents as a String array.
     * The file is assumed to be encoded in {@code ISO-8859-1}.
     * @param filePath The name of the text file to be read.
     * @return An {@code ISO-8859-1} string array of the contents of the file separated by lines.
     * @throws IOException If an I/O error occurs trying to read from the file.
     */
    public static String[] readLinesFromFile(String filePath) throws IOException {
        String string = Files.readString(Path.of(filePath), StandardCharsets.ISO_8859_1);
        return string.split("\n");
    }

}
