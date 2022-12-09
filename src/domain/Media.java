package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Super class for both movies and series.
 * Uses only immutable fields, to ensure that no media
 * can be changed after it has been added to the library.
*/
public abstract class Media {

    public final String title;
    public final int releaseYear;
    public final float rating;

    // /** An enum of the different categories that movies and series can belong to
    //  * <p> TODO: Maybe make this consist of flags in a bit field?
    //  * An enum value is stored as an int, which is 4 bytes,
    //  * and there are 23 categories, so 4 bytes * 23 = 92 bytes.
    //  * But if each category is stored as a flag in a bit field,
    //  * then only one int is needed, as 4 bytes * 8 = 32 bits.
    // */
    // public static enum Category {
    //     Action, Adventure, Biography, Comedy , Crime, Drama, // Ignoring uppercase convention ¯\_(ツ)_/¯
    //     Family, Fantasy, History, Horror,  Mystery, Romance,
    //     SciFi("Sci-fi"), Sport, Thriller, War, Western,

    //     FilmNoir("Film-Noir"), Music, Musical, // Unique to movies
    //     Animation, Documentary, TalkShow("Talk-show"); // Unique to series

    //     private String realName = null;

    //     private Category() {}
    //     private Category(String realName) {
    //         this.realName = realName;
    //     }
        
    //     private static Map<String, Category> map =
    //         Stream.of(Category.values()).collect(
    //             Collectors.toMap(
    //                 c -> c.toString().toLowerCase(), // Map key
    //                 c -> c                           // Map value
    //             )
    //         );

    //     /** Returns the category that corresponds to the given string, <i>case insensitive</i>.
    //      * @param string The string to parse.
    //      * @return The category that corresponds to the given string.
    //      */
    //     public static Optional<Category> fromString(String string) {
    //         return Optional.ofNullable(map.get(string.toLowerCase()));
    //     }

    //     /** Returns a set of string representing the categories, <i>in lowercase</i>.
    //      * @return The set of strings.
    //      */
    //     public static Set<String> getStringsLowerCase() {
    //         return map.keySet();
    //     }
    
    //     /** Returns the string representation of this category.
    //      * Some categories have a different string representation than their name.
    //      * For example, the category <code>SciFi</code> has the string representation <code>"Sci-fi"</code>.
    //      * @return The string representation of this category.
    //      */
    //     public String toString() {
    //         return realName == null ? name() : realName;
    //     }
    // }

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
    public static final class CategoryList {

        // #region Category constants
        /* Explanation of how the bit field and bitwise operators work:

        x '<<' y means shift the bits of x to the left by y places.
        
        1 << n is equal to 2^n.

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

        private static final int count = 23;
        private static final int last = count - 1;

        public static final ImmutableArray<String> names = new ImmutableArray<>(new String[] {
            "action", "adventure", "biography", "comedy", "crime", "drama",
            "family", "fantasy", "history", "horror", "mystery", "romance",
            "scifi", "sport", "thriller", "war", "western",

            "filmnoir", "music", "musical",        // Unique to movies
            "animation", "documentary", "talkshow" // Unique to series
        });
        
        private static final Map<String, Integer> map = new HashMap<>() {{
            // Loop to create the map
            for (int i = 0; i < count; i++)
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

        /** Returns a string representation of the category
         * with the given bit.
         * @param bitField The bit field, with only one bit set.
         * @return The string representation of the category.
         */
        private static String getNameForSingleBit(int bitField) {
            for(int i = 0; i < count; i++)
                if((1 << i) == bitField)
                    return names.get(i);
            return null;
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

        public String[] getNames() {
            // Save a temporary array of the names.
            String[] names = new String[count];

            int index = 0;
            for(int i = 0; i < last; i <<= 1) {
                String name = getNameForSingleBit(i);
                if(name != null)
                    names[index++] = name;
            }

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
    }

    protected Media(String title, int releaseYear, String[] categories, float rating) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.categories = new CategoryList(categories);
        this.rating = rating;
    }

    protected String getCategoriesString() {
        return Stream.of(categories.getNames()).collect(Collectors.joining(", "));
    }

    public abstract String toString();

    public abstract int hashCode();

    public abstract boolean equals(Object obj);    
}