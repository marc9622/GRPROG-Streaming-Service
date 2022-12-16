package domain;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;

import data.ObjectSaving;
import domain.MediaParsing.InvalidStringFormatException;
import domain.User.InvalidImagePathException;
import domain.User.InvalidPasswordException;
import domain.User.InvalidUsernameException;
import domain.UserSet.UserAlreadyExistsException;
import domain.UserSet.UserDoesNotExistException;

@Nested
public class TestsDomain {
    
    private static final String MOVIE_IMAGES_PATH = "./Data/filmplakater/";
    private static final String SERIES_IMAGES_PATH = "./Data/serieforsider/";

    public static final Movie newTestMovie = new Movie("The Matrix", 1999, new String[] {"Action", "Sci-Fi"}, 8.7f, MOVIE_IMAGES_PATH);
    public static final Series newTestSeries = new Series("The Office", 2005, true, 2013, new String[] {"Comedy"}, 8.9f, new int[] {6, 22, 25, 19, 28, 26, 26, 24, 25}, SERIES_IMAGES_PATH);
    
    public static final Movie newTestMovie1 = new Movie("Inception", 2010, new String[] {"Action", "Sci-fi"}, 8.8f, MOVIE_IMAGES_PATH);
    public static final Movie newTestMovie2 = new Movie("The Dark Knight", 2008, new String[] {"Action", "Crime", "Drama"}, 9.0f, MOVIE_IMAGES_PATH);
    public static final Series newTestSeries1 = new Series("Breaking Bad", 2008, true, 2013, new String[] {"Crime", "Drama", "Thriller"}, 9.5f, new int[] {1, 2, 3, 4, 5}, SERIES_IMAGES_PATH);
    public static final Series newTestSeries2 = new Series("Game of Thrones", 2011, true, 2019, new String[] {"Action", "Adventure", "Drama"}, 9.3f, new int[] {1, 2, 3, 4, 5, 6, 7, 8}, SERIES_IMAGES_PATH);

    @Nested
    public class TestMediaParsing {

    
        public static final BiFunction<String, String, Media> parseStringToMedia = (string, imagePath) -> {
            Method method = ReflectionUtils.findMethod(MediaParsing.class, "parseStringToMedia", String.class, String.class).orElseThrow(() -> new RuntimeException("Could not find method"));
            return (Media) ReflectionUtils.invokeMethod(method, null, string, imagePath);
        };

        @Test
        void ignore() throws InvalidStringFormatException {
            Media expected = null;
            Media actual = parseStringToMedia.apply("// This is a comment", null);
    
            assertEquals(expected, actual);
        }
    
        // Movies
    
        @Test
        void movieGeneral() throws InvalidStringFormatException {
            Object expected = newTestMovie;
            Object actual = parseStringToMedia.apply("The Matrix; 1999; Action, Sci-fi; 8.7;", MOVIE_IMAGES_PATH);
    
            assertEquals(expected, actual);
        }
    
        // Series
    
        @Test
        void serieGeneral() throws InvalidStringFormatException {
            Object expected = newTestSeries;
            Object actual = parseStringToMedia.apply("The Office; 2005-2013; Comedy; 8.9; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25;", SERIES_IMAGES_PATH);
            
            assertEquals(expected, actual);
        }
    
        @Test
        void serieStillRunningWithDash() throws InvalidStringFormatException {
            Object expected = new Series("The Office", 2005, false, 0, new String[] {"Comedy"}, 8.9f, new int[] {6, 22, 25, 19, 28, 26, 26, 24, 25}, SERIES_IMAGES_PATH);
            Object actual = parseStringToMedia.apply("The Office; 2005-; Comedy; 8.9; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25;", SERIES_IMAGES_PATH);
            
            assertEquals(expected, actual);
        }
    
        @Test
        void serieStillRunningWithoutDash() throws InvalidStringFormatException {
            Object expected = new Series("The Office", 2005, false, 0, new String[] {"Comedy"}, 8.9f, new int[] {6, 22, 25, 19, 28, 26, 26, 24, 25}, SERIES_IMAGES_PATH);
            Object actual = parseStringToMedia.apply("The Office; 2005; Comedy; 8.9; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25;", SERIES_IMAGES_PATH);
            
            assertEquals(expected, actual);
        }

        @Test
        void imagePath() {
            String expected = MOVIE_IMAGES_PATH + "The Matrix.jpg";
            String actual = newTestMovie.imagePath;
    
            assertEquals(expected, actual);

            expected = SERIES_IMAGES_PATH + "The Office.jpg";
            actual = newTestSeries.imagePath;

            assertEquals(expected, actual);
        }
    
        // Exception testing
    
        @Test
        void invalidYear() {
            // Movie
            Exception exceptionMovie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Matrix; 19a99; Action, Sci-fi; 8.7;", MOVIE_IMAGES_PATH);
            });
    
            // Serie
            Exception exceptionSerie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Office; 20a05-20a13; Comedy; 8.9; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25;", SERIES_IMAGES_PATH);
            });
    
            String expectedMessage = "Tried to parse Media, but could not parse year (int) from ";
    
            assertTrue(exceptionMovie.getMessage().startsWith(expectedMessage));
            assertTrue(exceptionSerie.getMessage().startsWith(expectedMessage));
        }
    
        @Test
        void invalidCategory() {
            // Movie
            Exception exceptionMovie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Matrix; 1999; aAction; 8.7;", MOVIE_IMAGES_PATH);
            });
    
            // Serie
            Exception exceptionSerie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Office; 2005-2013; ComedyA; 8.9; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25;", SERIES_IMAGES_PATH);
            });
    
            String expectedMessage = "Tried to parse Media, but could not parse category from ";
    
            assertTrue(exceptionMovie.getMessage().startsWith(expectedMessage));
            assertTrue(exceptionSerie.getMessage().startsWith(expectedMessage));
        }
    
        @Test
        void invalidRating() {
            // Movie
            Exception exceptionMovie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Matrix; 1999; Action, Sci-fi; 8a.7;", MOVIE_IMAGES_PATH);
            });
    
            // Serie
            Exception exceptionSerie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Office; 2005-2013; Comedy; 8.a9; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25;", SERIES_IMAGES_PATH);
            });
    
            String expectedMessage = "Could not parse rating (float) from ";
    
            assertTrue(exceptionMovie.getMessage().startsWith(expectedMessage));
            assertTrue(exceptionSerie.getMessage().startsWith(expectedMessage));
        }
    
        @Test
        void missingData() {
            // Movie
            Exception exceptionMovie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Matrix; 1999; Action, Sci-fi;", MOVIE_IMAGES_PATH);
            });
    
            // Serie
            Exception exceptionSerie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Office; 2005-2013; Comedy; 8.9;", SERIES_IMAGES_PATH);
            });
    
            String expectedMessageMovie = "Tried to parse Media, but string ended prematurely.";
            String expectedMessageSerie = "Tried to parse Serie, but string ended prematurely.";
    
            assertTrue(exceptionMovie.getMessage().startsWith(expectedMessageMovie));
            assertTrue(exceptionSerie.getMessage().startsWith(expectedMessageSerie));
        }
    
        @Test
        void invalidSeasons() {
            // Serie
            Exception exceptionSerie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Office; 2005-2013; Comedy; 8.9; 1a-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25, 10-25;", SERIES_IMAGES_PATH);
            });
    
            String expectedMessage = "Tried to parse Serie, but could not parse season from ";
    
            assertTrue(exceptionSerie.getMessage().startsWith(expectedMessage));
        }
    
        @Test
        void tooLong() {
            // Movie
            Exception exceptionMovie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Matrix; 1999; Action, Sci-fi; 8.7; 8.7;", MOVIE_IMAGES_PATH);
            });
    
            // Serie
            Exception exceptionSerie = assertThrows(InvalidStringFormatException.class, () -> {
                parseStringToMedia.apply("The Office; 2005-2013; Comedy; 8.9; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25; 1-6, 2-22, 3-25, 4-19, 5-28, 6-26, 7-26, 8-24, 9-25;", SERIES_IMAGES_PATH);
            });
    
            String expectedMessageMovie = "Tried to parse Serie, but could not parse season and length from ";
            String expectedMessageSerie = "Tried to parse Serie, but string contained more characters than expected.";
    
            assertTrue(exceptionMovie.getMessage().startsWith(expectedMessageMovie));
            assertTrue(exceptionSerie.getMessage().startsWith(expectedMessageSerie));
        }
    
    }

    @Nested
    public class TestMediaSorting {

        public static MediaLibrary newTestMediaLibrary() {
            MediaLibrary mediaLibrary = new MediaLibrary();
            mediaLibrary.add(newTestMovie);
            mediaLibrary.add(newTestSeries);
            mediaLibrary.add(newTestMovie1);
            mediaLibrary.add(newTestSeries1);
            mediaLibrary.add(newTestMovie2);
            mediaLibrary.add(newTestSeries2);
            return mediaLibrary;
        }

        public static Stream<Arguments> provideBooleanArguments() {
            return Stream.of(Arguments.of(false, false), Arguments.of(false, true), Arguments.of(true, false), Arguments.of(true, true));
        }

        @ParameterizedTest
        @MethodSource("provideBooleanArguments")
        void searchByTitle(boolean useCache, boolean parallel) {
            MediaLibrary mediaLibrary = newTestMediaLibrary();
            List<Media> searchResult = mediaLibrary.getSortedBySearch("The Matrix", useCache, parallel);
            assertEquals(mediaLibrary.size(), searchResult.size());
            assertEquals(newTestMovie, searchResult.get(0));
        }

        @ParameterizedTest
        @MethodSource("provideBooleanArguments")
        void searchByCategory(boolean useCache, boolean parallel) {
            MediaLibrary mediaLibrary = newTestMediaLibrary();
            List<Media> searchResult = mediaLibrary.getSortedBySearch("Action", useCache, parallel);
            assertEquals(mediaLibrary.size(), searchResult.size());
            assertEquals(newTestMovie1, searchResult.get(0));
            assertEquals(newTestMovie2, searchResult.get(1));
            assertEquals(newTestMovie, searchResult.get(2));
        }

    }

    @Nested
    public class TestSerialization {

        @Test
        void movieSerialization() throws IOException, ClassNotFoundException {
            Movie movie = newTestMovie;
    
            ObjectSaving.saveToFile(movie, "test");
    
            Object parsedMovie = ObjectSaving.loadFromFile(Movie.class, "test");
    
            assertEquals(movie, parsedMovie);
        }

        @Test
        void seriesSerialization() throws IOException, ClassNotFoundException {
            Series series = newTestSeries;
    
            ObjectSaving.saveToFile(series, "test");
    
            Object parsedSeries = ObjectSaving.loadFromFile(Series.class, "test");
    
            assertEquals(series, parsedSeries);
        }

        @Test
        void mediaLibrarySerialization() throws IOException, ClassNotFoundException {
            MediaLibrary mediaLibrary = new MediaLibrary();
            mediaLibrary.add(newTestMovie);
            mediaLibrary.add(newTestSeries);
    
            ObjectSaving.saveToFile(mediaLibrary, "test");
    
            Object parsedMediaLibrary = ObjectSaving.loadFromFile(MediaLibrary.class, "test");
    
            assertEquals(mediaLibrary, parsedMediaLibrary);
        }

        @Test
        void userSerialization() throws IOException, ClassNotFoundException, InvalidUsernameException, InvalidPasswordException, InvalidImagePathException {
            User user = new User("Test1", "abc123", null);
            user.addFavorite(newTestMovie);
            user.addFavorite(newTestSeries);
    
            ObjectSaving.saveToFile(user, "test");
    
            Object parsedUser = ObjectSaving.loadFromFile(User.class, "test");
    
            assertEquals(user, parsedUser);
        }

        @Test
        void userSetSerialization() throws IOException, ClassNotFoundException, UserAlreadyExistsException, InvalidUsernameException, InvalidPasswordException, InvalidImagePathException, UserDoesNotExistException {
            UserSet userSet = new UserSet();
            userSet.addUser(new User("Test1", "abc123", null));
            userSet.addUser(new User("Test2", "abc123", null));
            userSet.getUser("Test1").addFavorite(newTestMovie);
            userSet.getUser("Test2").addFavorite(newTestSeries);

            ObjectSaving.saveToFile(userSet, "test");

            Object parsedUserSet = ObjectSaving.loadFromFile(UserSet.class, "test");

            assertEquals(userSet, parsedUserSet);
        }

    }

}
