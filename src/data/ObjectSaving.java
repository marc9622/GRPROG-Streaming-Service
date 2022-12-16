package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

/** A class for saving and loading objects to and from files. */
public class ObjectSaving {
    
    /** The folder to save objects to. */
    public static final String FOLDER = "./Saved/";

    // Create the folder if it does not exist.
    static {
        try {
            Files.createDirectories(Path.of(FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Prevent instantiation.
    private ObjectSaving() {}

    /** Saves the given object to the file {@code FOLDER + fileName + getFileExtension(object.getClass())}.
     * @param object The object to save.
     * @param fileName The name of the folder to save the object to.
     * @throws IOException If the file could not be written to.
     */
    public static void saveToFile(Serializable object, String fileName) throws IOException {
        String filePath = FOLDER + fileName + getFileExtension(object.getClass());
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(object);
        }
    }

    /** Loads an object from a file.
     * The file path is {@code FOLDER + fileName + getFileExtension(clazz)}.
     * Also checks that the object is of the given class.
     * @param clazz The class of the object to load.
     * @return The object loaded from the file.
     * @throws IOException If the file could not be read.
     * @throws ClassNotFoundException If the file is not formatted correctly.
     * @throws ClassCastException If the object is not of the given class.
     */
    public static <T> T loadFromFile(Class<T> clazz, String fileName) throws IOException, ClassNotFoundException {
        String filePath = FOLDER + fileName + getFileExtension(clazz);
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            Object object = in.readObject();
            if(clazz.isInstance(object))
                return clazz.cast(object);
            throw new ClassCastException("The object loaded from the file is not of the given class.");
        }
    }

    /** Gets the file extension for the given class.
     * The file extension is the class name, with the first letter in lower case.
     * @param clazz The class to get the file extension for.
     * @return The file extension for the given class.
     */
    private static String getFileExtension(Class<?> clazz) {
        return '.' + clazz.getSimpleName().toLowerCase();
    }

    /** Implementing this indicates that instances of this class can be saved to a file.
     * This interface extends {@link Serializable}, so that the object can be saved to a file.
     * The default implementation of {@link #saveToFile(String)} uses {@link ObjectSaving#saveToFile(Serializable, String)}.
     * 
     * <p>Normally, classes that implement this should include a serialVersionUID field, as described in {@link Serializable}.
     * Though, because we aren't going to make changes to this project after it is finished, 
     * we can just use the default generated serialVersionUID.
     * 
     * <p><i>Note that this means the serialVersionUID is brittle, and might change if the class is changed.
     * Which means that the saved files might not be compatible with the new version of the class.</i>
     * 
     * <p>Classes that require special handling during the serialization and
     * deserialization process must implement special methods with these exact
     * signatures:
     *
     * <PRE>
     * import java.io.ObjectStreamException;
     * private void writeObject(ObjectOutputStream out) throws IOException;
     * private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException;
     * private void readObjectNoData() throws ObjectStreamException;
     * </PRE>
     * @see Serializable
     * @see ObjectSaving
    */
    public interface Saveable extends Serializable {

        /** Saves this object to a file.
         * The default implementation uses {@link ObjectSaving#saveToFile(Serializable, String)},
         * @param fileName The name of the file to save the object to.
         * @throws IOException If the file could not be written to.
         * @see ObjectSaving#saveToFile(Serializable, String)
         */
        default public void saveToFile(String fileName) throws IOException {
            ObjectSaving.saveToFile(this, fileName);
        }
    
    }

}
