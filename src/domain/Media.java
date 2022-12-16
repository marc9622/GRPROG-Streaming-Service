package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.ObjectSaving.Saveable;

/** Super class for both movies and series.
 * Uses only immutable fields, to ensure that no media
 * can be changed after it has been added to the library.
*/
public abstract class Media implements Saveable {

    public final String title;
    public final int releaseYear;
    public final float rating;

    /** The categories this media belongs to */
    public final CategoryList categories;

    /** A class that represents a list of categories.
     * The list is stored as a bit field, which is an int,
     * where each bit acts as a boolean flag for a category.
     * <p>Example: If the first bit (from the right) is Action,
     * the next is Adventure, and the third is Biography,
     * then a bit field of {@code 00...00101} would represent
     * the categories Action and Biography, but not Adventure.
     */
    public static final class CategoryList implements Saveable {

        // #region Category constants
        /* Explanation of how the bit field and bitwise operators work:

        x '<<' y means shift the bits of x to the left by y places.
        
        '1 << n' is equal to 2^n.

        Examples:
           1 << 0 = ..0001 = 1
           1 << 1 = ..0010 = 2
           1 << 2 = ..0100 = 4

        To check if a bit is set (i.e. is a '1'),
            use the bitwise AND operator '&'.
        
        If the result is 0, then the bit is not set.
        If the result is not 0, then the bit is set.

        Examples:
            0010 & 0001 = 0000 = 0 (not set)
            0010 & 0010 = 0010 = 2 (set)
            0010 & 0100 = 0000 = 0 (not set)

        To set a bit, use the bitwise OR operator '|'.

        Examples:
            0010 | 0001 = 0011 = 3
            0010 | 0010 = 0010 = 2
            0010 | 0100 = 0110 = 6
        */
        private static final int NULL = 0;

        private static final int COUNT = 23;

        public static final ImmutableArray<String> names = new ImmutableArray<>(new String[] {
            "action", "adventure", "biography", "comedy", "crime", "drama",
            "family", "fantasy", "history", "horror", "mystery", "romance",
            "sci-fi", "sport", "thriller", "war", "western",

            "film-noir", "music", "musical",        // Unique to movies
            "animation", "documentary", "talk-show" // Unique to series
        });
        
        private static final Map<String, Integer> map = new HashMap<>() {{
            // Loop to create the map
            for (int i = 0; i < COUNT; i++)
                put(names.get(i), 1 << i);
        }};
        // #endregion

        private final int bitField;

        public CategoryList(String... names) {
                                // Create a stream of the names
            this.bitField = Stream.of(names)
                                // Use the map to convert the names to bit fields
                                  .map(name -> map.get(name.toLowerCase()))
                                // Put the bit fields together using bitwise OR
                                  .reduce((a, b) -> a | b)
                                // If the stream is empty, return NULL
                                  .orElse(NULL);
        }

        /** Returns whether the given name is a valid category name.
         * @param name The name to check.
         * @return Whether the name is valid.
         */
        public static boolean doesNameExist(String name) {
            return map.containsKey(name.toLowerCase());
        }

        /** Returns whether this category list contains the given category.
         * @param other The category to check.
         * @return Whether this category list contains the given category.
         */
        public boolean contains(CategoryList other) {
            return (this.bitField & other.bitField) != NULL;
        }

        /** Returns the indices of the categories in this list.
         * The indices are the same as the indices in the {@link #names} array.
         * @return The indices of the categories in this list.
         */
        public int[] getIndices() {
            // Save a temporary array of the indices.
            int[] indices = new int[COUNT];

            int index = 0;
            for(int i = 0; i < COUNT; i++)
                if((this.bitField & (1 << i)) != NULL)
                    indices[index++] = i;

            int[] result = new int[index];
            System.arraycopy(indices, 0, result, 0, index);
            return result;
        }

        /** Returns the names of the categories in this list.
         * @return The names of the categories in this list.
         */
        public String[] getNames() {
            // Save a temporary array of the names.
            String[] names = new String[COUNT];

            int index = 0;
            for(int i = 0; i < COUNT; i++)
                if((this.bitField & (1 << i)) != NULL)
                    names[index++] = CategoryList.names.get(i);

            String[] result = new String[index];
            System.arraycopy(names, 0, result, 0, index);
            return result;
        }

        public boolean equals(Object obj) {
            if(obj == null)
                return false;

            if(obj == this)
                return true;

            if(!(obj instanceof CategoryList))
                return false;

            CategoryList other = (CategoryList) obj;
            return this.bitField == other.bitField;
        }

        public String toString() {
            String[] names = getNames();

            if(names.length == 0)
                return "None";

            UnaryOperator<String> capitalize = s -> Character.toUpperCase(s.charAt(0)) + s.substring(1);
            
            if(names.length == 1)
                return capitalize.apply(names[0]);
            return Stream.of(names)
                         .map(capitalize)
                         .limit(names.length - 1)
                         .collect(Collectors.collectingAndThen(
                                                Collectors.joining(", "),
                                                s -> s + " and "))
                   + capitalize.apply(names[names.length - 1]);
        }
    
        public int hashCode() {
            return this.bitField;
        }
    }

    public final String imagePath;

    protected Media(String title, int releaseYear, String[] categories, float rating, String imagePath) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(categories);
        Objects.requireNonNull(imagePath);

        this.title = title;
        this.releaseYear = releaseYear;
        this.categories = new CategoryList(categories);
        this.rating = rating;

        if(imagePath.endsWith("/") || imagePath.endsWith("\\"))
            this.imagePath = imagePath + title + ".jpg";
        else
            this.imagePath = imagePath + "/" + title + ".jpg";
    }

    protected String getCategoriesString() {
        return Stream.of(categories.getNames()).collect(Collectors.joining(", "));
    }

    public abstract String toString();

    public abstract int hashCode();

    public abstract boolean equals(Object obj);
}
