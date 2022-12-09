package domain;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import domain.MediaParsing.InvalidStringFormatException;
import domain.MediaSorting.SearchCache;

/** A class that represents a library of media. <ul>
 * <p> Use {@link #readFiles(String, String)} to read the media from the given files.
 * <p> Use {@link #search(String)} to search for media.
 * <p> Use {@link #add(Media)} to add media to the library.
 * <p> Use {@link #remove(Media)} to remove media from the library. </ul>
 */
public class MediaLibrary {

    /** <i>Should be non-null.</i> */
    private final Set<Media> mediaSet;
    private transient SearchCache searchCache = new SearchCache();

    /** Creates an empty media library.*/
    public MediaLibrary() {
        mediaSet = new HashSet<>();
    }

    /** Creates a new media library that contains all media in the given files.
     * @param filePathMovies The path to the file containing movies.
     * @param filePathSeries The path to the file containing series.
     * @throws IOException If the files could not be read.
     * @throws MediaParsing.InvalidStringFormatException If the files are not formatted correctly.
     */
    public static MediaLibrary parseMediaLibrary(String filePathMovies, String filePathSeries) throws IOException, InvalidStringFormatException {
        MediaLibrary library = new MediaLibrary();
        library.readFiles(filePathMovies, filePathSeries);
        return library;
    }

    /** Re-reads the media files and updates the media library, and clears the search cache.
     * @param filePathMovies The path to the file containing movies.
     * @param filePathSeries The path to the file containing series.
     * @throws IOException If the files could not be read.
     * @throws MediaParsing.InvalidStringFormatException If the files are not formatted correctly.
     */
    private void readFiles(String filePathMovies, String filePathSeries) throws IOException, InvalidStringFormatException {
        Media[] mediaArray = MediaParsing.parseFiles(filePathMovies, filePathSeries);
        mediaSet.clear();
        Stream.of(mediaArray).forEach(media -> mediaSet.add(media));
        searchCache.clear();
    }

    /** Returns the media library sorted by the given search string.
     * Searches by title and category, <i>case insensitive</i>.
     * @param query The query to search for.
     * @param useCache Whether to use the search cache.
     * @param parallel Whether to use concurrent search.
     * @return A set of media that matches the given query.
     */
    public List<Media> sortBySearch(String query, boolean useCache, boolean parallel) {
        return sortBySearch(query, mediaSet.size(), useCache, parallel);
    }

    /** Returns the media library sorted by the given search string.
     * Searches by title and category, <i>case insensitive</i>.
     * @param query The query to search for.
     * @param count The maximum number of results to return. Best results are returned first.
     * @param useCache Whether to use the search cache.
     * @param parallel Whether to use concurrent search.
     * @return A set of media that matches the given query.
     */
    public List<Media> sortBySearch(String query, int count, boolean useCache, boolean parallel) {
        return MediaSorting.sortBySearchQueries(mediaSet, query.split(" "), searchCache, count, useCache, parallel);
    }

    /** Returns a sorted list of the library,
     * using a specified sorting method.
     * @param sortBy The property to sort by.
     * @param sortOrder The order to sort in.
     * @return The sorted list of media.
     */
    public List<Media> sortBy(MediaSorting.SortBy sortBy, MediaSorting.SortOrder sortOrder) {
        return MediaSorting.sortMedia(mediaSet, sortBy, sortOrder);
    }

    /** Adds the given media to the library, and clears the search cache.
     * @param media The media to add.
     */
    public void add(Media media) {
        mediaSet.add(media);
        searchCache.clear();
    }

    /** Adds all media in the given library to this library, and clears the search cache.
     * @param media The set of media to add.
     */
    public void addAll(MediaLibrary media) {
        mediaSet.addAll(media.mediaSet);
        searchCache.clear();
    }

    /** Removes the given media from the library, and clears the search cache.
     * @param media The media to remove.
     */
    public void remove(Media media) {
        mediaSet.remove(media);
        searchCache.clear();
    }

    /** Clears the library, and clears the search cache. */
    public void removeAll() {
        mediaSet.clear();
        searchCache.clear();
    }

    /** Returns whether the library contains the given media.
     * @param media The media to check for.
     * @return Whether the library contains the given media.
     */
    public boolean contains(Media media) {
        return mediaSet.contains(media);
    }

    /** Deep-clones the media library */
    public MediaLibrary clone() {
        MediaLibrary newLibrary = new MediaLibrary();
        mediaSet.forEach(media -> newLibrary.add(media)); // Media is immutable, so no need to clone
        return newLibrary;
    }
    
    public int hashCode() {
        return 37 * mediaSet.hashCode();
    }

    /** Returns whether the given object is equal to this media library.
     * @param obj The object to compare to.
     * @return Whether the given object is equal to this media library.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MediaLibrary)) return false;
        MediaLibrary other = (MediaLibrary) obj;
        return mediaSet.equals(other.mediaSet);
    }

}