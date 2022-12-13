package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.FileReading;

/** Effectively just functions as a namespace for functions that parse files.
 * <p> Use {@link #parseFiles(String, String)} to read parse movie and series files.
 * <p> Contains {@link #InvalidStringFormatException} for when a string is formatted incorrectly.
 */
public class MediaParsing {

    /** Prevents instantiation of this class.
     * This class only contains static methods,
     * and exists only because Java forces you to use classes,
     * even when it is unnecessary.
     */
    private MediaParsing () {}

    /** Reads and parses the given files.
     * <p> Lines must be written in the format specified at {@link #parseStringToMedia}.
     * @param filePathMovies The path to the file containing movies. Fx. {@code ".Data/film.txt"}.
     * @param filePathSeries The path to the file containing series. Fx. {@code ".Data/serier.txt"}.
     * @return All the movies and series as a set of media.
     * @throws IOException If an I/O error occurs trying to read from the file.
     * @throws InvalidStringFormatException If a line in the file is not written in the correct format.
     */
    public static Media[] parseFiles(String filePathMovies, String filePathSeries, String filePathMovieImages, String filePathSeriesImages)
    throws IOException, InvalidStringFormatException {
        
        Media[] linesMovies = null;
        Media[] linesSeries = null;

        InvalidStringFormatException exception = null;
        
        // Try parse the lines to media
        try {
            linesMovies = parseLinesToMedia(FileReading.readLinesFromFile(filePathMovies), filePathMovieImages);
        }
        catch (InvalidStringFormatException e) {
            exception = e;
            linesMovies = e.successfullyParsed;
        }

        try {
            linesSeries = parseLinesToMedia(FileReading.readLinesFromFile(filePathSeries), filePathSeriesImages);
        }
        catch (InvalidStringFormatException e) {
            exception = e;
            linesSeries = e.successfullyParsed;
        }

        // Combine the two arrays
        Media[] lines = new Media[linesMovies.length + linesSeries.length];
        System.arraycopy(linesMovies, 0, lines, 0, linesMovies.length);
        System.arraycopy(linesSeries, 0, lines, linesMovies.length, linesSeries.length);
        
        // If an exception was thrown, throw it now.
        if(exception != null) throw new InvalidStringFormatException(exception.errorDescription, exception.invalidStrings, lines);

        return lines;
    }

    private static Media[] parseLinesToMedia(String[] lines, String imagePath) throws InvalidStringFormatException {
        return parseLinesToMedia(new String[][] {lines}, imagePath);
    }

    /** Takes a string array of movies and one of series and parses them into an array of movies and series.
     * @param linesMovies The string array of movies.
     * @param linesSeries The string array of series.
     * @return An array of movies and series.
     * @throws InvalidStringFormatException If a string could not be parsed.
     */
    private static Media[] parseLinesToMedia(String[][] lines, String imagesPath) throws InvalidStringFormatException {
        if(lines.length == 0) return new Media[0];

        ArrayList<Media> media = new ArrayList<Media>(lines[0].length);

        // In case of an exception, we want to throw it after we have attempted to parse all lines.
        InvalidStringFormatException exception = null;
        ArrayList<String> invalidStrings = null;

        // Parse all lines to media
        for(String[] lineArray : lines)
            for(String line : lineArray)
                try {
                    media.add(parseStringToMedia(line, imagesPath));
                }
                // If an exception is thrown, we want to save it and continue parsing.
                catch (InvalidStringFormatException e) {
                    if(exception == null) exception = e;
                    if(invalidStrings == null) invalidStrings = new ArrayList<String>();
                    invalidStrings.add(line);
                }

        Media[] mediaArray = media.toArray(new Media[media.size()]);

        // If an exception was thrown, we want to throw it after we have attempted to parse all lines.
        if(exception != null) {
            String errorDesc = exception.errorDescription;
            String[] invalidStringsArray = invalidStrings.toArray(new String[invalidStrings.size()]);
            throw new InvalidStringFormatException(errorDesc, invalidStringsArray, mediaArray);
        }

        return mediaArray;
    }

    /** Takes a single string line and parses it into either a Movie or Serie.
     * <p> The line format for movies is:
     * <p> <code>title; releaseYear; category1, category2 ...; rating;</code>
     * <p> The line format for series is:
     * <p> <code>title; releaseYear-endYear; category1, category2 ...; rating; 1-season1Length, 2-season2Length ...;</code>
     * <p> Some of the given data from the assignment is actually formatted incorrectly.
     * In those cases, <code>//</code> at the start of a line is used to indicate that the line should be ignored.
     * @param string The string to be parsed.
     * @param isMovie Whether the string is a movie or a serie.
     * @return Either a Movie or Serie object. (Or null if the string is ignored.)
     * @throws InvalidStringFormatException If the string is not formatted correctly.
     */
    private static Media parseStringToMedia(String string, String imagePath) throws InvalidStringFormatException {
        // If the line starts with "//", it is ignored.
        if(string.startsWith("//")) return null;

        // The string is parsed in one pass because it is faster.
        // This enum is used to keep track of the current parsing state.
        enum ParsingState { TITLE, RELEASE_YEAR, END_YEAR, CATEGORIES, RATING, SEASONS, DONE }
        ParsingState isParsing = ParsingState.TITLE;

        // These variables are used to store the parsed data.
        boolean knowItIsSerie = false; // This is unknown at this point, but it is set later.
        String title = null;
        int releaseYear = 0;
        boolean isEnded = false;
        int endYear = 0;
        List<String> categories = new ArrayList<>(4);
        float rating = 0;
        List<Integer> seasonLengths = null;

        // Simply a index variable used to keep track of what the last parsed character was.
        int lastParsed = 0;

        // This loop parses the string.
        for(int i = 0; i < string.length() && isParsing != ParsingState.DONE; i++) {
            char c = string.charAt(i);
            boolean isSemicolon = c == ';';

            // Parse the title
            if(isParsing == ParsingState.TITLE) {
                if(!isSemicolon) continue;
                title = string.substring(0, i).strip();

                lastParsed = i + 1;
                isParsing = ParsingState.RELEASE_YEAR;
                continue;
            }

            // Parse the release year
            if(isParsing == ParsingState.RELEASE_YEAR) {

                // If we haven't reached a semicolon or hyphen, continue.
                if(!(isSemicolon || c == '-')) continue;

                // If we have reached a hyphen, we know it is a serie.
                if(!isSemicolon) knowItIsSerie = true;

                // Try to parse the release year.
                try {
                    releaseYear = Integer.parseInt(string.substring(lastParsed, i).strip());
                } catch (NumberFormatException e) {
                    throw new InvalidStringFormatException("Tried to parse Media, but could not parse year (int) from '" + string.substring(lastParsed, i).strip() + "'.", string);
                }

                // Update the last parsed index.
                lastParsed = i + 1;

                // If we reached a semicolon, then we parse the categories.
                if(isSemicolon) isParsing = ParsingState.CATEGORIES;
                
                // If we reached a hyphen, then we parse the end year.
                else isParsing = ParsingState.END_YEAR;
                
                continue;
            }

            // Parse the end year
            if(isParsing == ParsingState.END_YEAR) {
                if(!isSemicolon) continue;

                String endYearString = string.substring(lastParsed, i).strip();

                // Only parse the end year if it is not empty.
                if(!endYearString.isBlank()) {
                    isEnded = true;
                    try {
                        endYear = Integer.parseInt(endYearString);
                    } catch (NumberFormatException e) {
                        throw new InvalidStringFormatException("Tried to parse Media, but could not parse end year (int) from '" + endYearString + "'.", string);
                    }
                }

                lastParsed = i + 1;
                isParsing = ParsingState.CATEGORIES;
                continue;
            }
        
            // Parse the categories
            if(isParsing == ParsingState.CATEGORIES) {

                // If we haven't reached a semicolon or comma, continue.
                if(!isSemicolon && c != ',') continue;

                // Parse the category and add it to the list.
                String categoryString = string.substring(lastParsed, i).strip();
                
                // If the name is valid, add it to the list.
                if(Media.CategoryList.doesNameExist(categoryString))
                    categories.add(categoryString);

                // If not, throw an exception.
                else throw new InvalidStringFormatException("Tried to parse Media, but could not parse category from '" + categoryString + "'.", string);

                // Update the last parsed index.
                lastParsed = i + 1;

                // If we reached a semicolon, then we go parse the rating.
                if(isSemicolon)
                    isParsing = ParsingState.RATING;
                
                continue;
            }

            // Parse the rating
            if(isParsing == ParsingState.RATING) {
                if(c != ';') continue;

                try {
                    rating = Float.parseFloat(string.substring(lastParsed, i).strip().replace(",", "."));
                } catch (NumberFormatException e) {
                    throw new InvalidStringFormatException("Could not parse rating (float) from '" + string.substring(lastParsed, i).strip() + "'.", string);
                }

                // If the media didn't contain a hyphen after the release year,
                // then we still don't know if it is a movie or a series.
                // Therefore, we just continue to parsing the seasons.
                lastParsed = i + 1;
                isParsing = ParsingState.SEASONS;
                seasonLengths = new ArrayList<>(6);
                continue;
            }

            // Parse the season
            if(isParsing == ParsingState.SEASONS) {

                // If we haven't reached a semicolon or comma, continue.
                if(!isSemicolon && c != ',') continue;
                
                // Find the index of the hyphen.
                int hyphen = string.indexOf('-', lastParsed);
                if(hyphen == -1)
                    throw new InvalidStringFormatException("Tried to parse Serie, but could not parse season and length from '" + string.substring(lastParsed, i).strip() + "'.", string);
                
                // Parse the season and length and add it to the list.
                try {
                    // The season is the first part of the string. From lastParsed to hyphen.
                    int season = Integer.parseInt(string.substring(lastParsed, hyphen).strip());
                    if(season != seasonLengths.size() + 1)
                        throw new InvalidStringFormatException("Tried to parse Serie, but season numbers are not in order.", string);

                    // The length is the second part of the string. From hyphen+1 to i.
                    int seasonLength = Integer.parseInt(string.substring(hyphen + 1, i).strip());
                    seasonLengths.add(seasonLength);
                } catch (NumberFormatException numberFormat) {
                    throw new InvalidStringFormatException("Tried to parse Serie, but could not parse season from '" + string.substring(lastParsed, i).strip() + "'.", string);
                }

                lastParsed = i + 1;
                if(isSemicolon) {
                    isParsing = ParsingState.DONE;
                    break;
                }
                
                continue;
            }
        }

        // If we knew the media was a serie, and we didn't end in the DONE state,
        // then we know that the string was invalid.
        if(knowItIsSerie && isParsing != ParsingState.DONE)
            throw new InvalidStringFormatException("Tried to parse Serie, but string ended prematurely.", string);

        // If we didn't know the whether it was a movie or serie...
        if(!knowItIsSerie) {
            // and we didn't end in the DONE state...
            if(isParsing != ParsingState.DONE) {
                // and we didn't end in the SEASONS state either, then we know that the string was invalid.
                if(isParsing != ParsingState.SEASONS)
                    throw new InvalidStringFormatException("Tried to parse Media, but string ended prematurely.", string);
                
                // If we did end in the SEASONS state, but the seasons list is not empty, then it was an invalid serie.
                else if(!seasonLengths.isEmpty())
                    throw new InvalidStringFormatException("Tried to parse Serie, but string ended prematurely.", string);
                
                // Otherwise, we know it is a movie.
            }

            // If we ended in the DONE state, and the seasons list is not empty, then we know it is a series.
            else if(!seasonLengths.isEmpty()) {
                knowItIsSerie = true;
            }

            // Otherwise, we know it is a movie.
        }

        // *At this point, we are sure whether it is a movie or series.*

        // If the string is longer than expected, throw an exception.
        if(!string.substring(lastParsed).isBlank())
            throw new InvalidStringFormatException("Tried to parse " + (knowItIsSerie ? "Serie" : "Movie") + ", " +
                                                   "but string contained more characters than expected.", string);

        // Create the media object and return it.
        if(!knowItIsSerie) return new Movie(title, releaseYear, categories.toArray(String[]::new), rating, imagePath);
        else               return new Series(title, releaseYear, isEnded, endYear, categories.toArray(String[]::new), rating,
                                            seasonLengths.stream().mapToInt(i -> i).toArray(), imagePath);
    }

    /** Thrown when a string cannot be parsed to a movie or a serie.
     * <p> Contains: <ul>
     * <li>{@link #errorDescription}: A description of the format error (for the first invalid string).</li>
     * <li>{@link #invalidStrings}: The invalid string(s) that caused the error.</li>
     * <li>{@link #successfullyParsed}: The successfully parsed media.</li>
     */
    public static class InvalidStringFormatException extends Exception {

        public final String errorDescription;
        public final String[] invalidStrings;
        public final Media[] successfullyParsed;

        public InvalidStringFormatException(String errorDescription, String invalidString) {
            this(errorDescription, new String[] {invalidString}, new Media[0]);
        }

        public InvalidStringFormatException(String errorDescription, String[] invalidStrings, Media[] successfullyParsed) {
            super(errorDescription + " String: '" + invalidStrings[0].strip() +
                (invalidStrings.length == 1 ? "'" : "' and " + (invalidStrings.length - 1) + " more.") +
                " Successfully parsed: " + successfullyParsed.length + " media."
            );
            this.errorDescription = errorDescription;
            this.invalidStrings = invalidStrings;
            this.successfullyParsed = successfullyParsed;
        }

    }
    
}