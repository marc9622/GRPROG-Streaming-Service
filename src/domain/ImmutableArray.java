package domain;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

/** A simple wrapper class for an array that cannot be modified.
 * Keep in mind that the elements themselves can still be modified,
 * but they cannot be replaced.
*/
public class ImmutableArray<Type> implements Iterable<Type> {
    
    private final Type[] array;

    /** Creates a new imutable array with the given array as its contents.
     * @param array The array to copy.
     */
    public ImmutableArray(Type[] array) {
        this.array = array.clone();
    }

    /** Returns the element at the given index.
     * @param index The index of the element to return.
     * @return The element at the given index.
     */
    public Type get(int index) {
        return array[index];
    }

    /** Returns the length of the array.
     * @return The length of the array.
     */
    public int length() {
        return array.length;
    }

    /** Returns whether the given object is equal to this array.
     * @param obj The object to compare.
     * @return Whether the given object is equal to this array.
     */
    public boolean equals(Object obj) {
        if(obj instanceof Object[])
            return Arrays.equals(array, (Object[]) obj);
        if(obj instanceof ImmutableArray)
            return Arrays.equals(array, ((ImmutableArray<?>) obj).array);
        return false;
    }

    /** Returns an iterator for this array.
     * @return An iterator for this array.
     */
    public Iterator<Type> iterator() {
        return new Iterator<Type>() {
            private int index = 0;
            public boolean hasNext() {
                return index < array.length;
            }
            public Type next() {
                return array[index++];
            }
        };
    }

    /** Returns a stream for this array.
     * @return A stream for this array.
     */
    public Stream<Type> stream() {
        return Arrays.stream(array);
    }

    /** Returns a string representation of the array.
     * @return A string representation of the array.
     */
    public String toString() {
        return Arrays.toString(array);
    }

    /** Returns the hash code of the array.
     * @return The hash code of the array.
     */
    public int hashCode() {
        return Arrays.hashCode(array);
    }

}
