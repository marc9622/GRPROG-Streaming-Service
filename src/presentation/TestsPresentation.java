package presentation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode;

import domain.ApplicationData;
import domain.Media;
import domain.User;

@Nested
public class TestsPresentation {
    
    // The Unit tests are run in a different directory, which means that they can't read the media files.
    // This means that we have to add our own test media.
    @Nested
    public class TestUINavigation {
        
        public static ApplicationData getAppData(Application app) throws Exception {
            ApplicationData data = (ApplicationData) ReflectionUtils.tryToReadFieldValue(Application.class, "data", app).get();
            return data;
        }

        @BeforeAll
        static void turnOffErrorDialogs() throws Exception {
            Field showErrorDialog = ReflectionUtils.findFields(ErrorHandling.class, method -> true, HierarchyTraversalMode.TOP_DOWN).get(0);
            showErrorDialog.setAccessible(true);
            showErrorDialog.set(null, false);
        }

        @Test
        void pageNavigation() throws Exception {
            Application app = new Application();
            ApplicationData data = getAppData(app);

            assertTrue(data.getUsers().size() == 0);
            app.addUser("Test1", "abc123", "abc123", null);
            assertTrue(data.getUsers().size() == 1);

            app.loginUser("Test1", "abc123");

            app.logoutUser();

            assertTrue(data.getUsers().size() == 1);
            app.addUser("Test2", "abc123", "abc123", null);
            assertTrue(data.getUsers().size() == 2);

            app.deleteUser("Test1");
            assertTrue(data.getUsers().size() == 1);

            app.loginUser("Test2", "abc123");

            User user = data.getUser("Test2");
            assertNotNull(user);

            // We have to use our own test media.
            Media media1 = domain.TestsDomain.newTestMovie;
            assertNotNull(media1);

            Media media2 = domain.TestsDomain.newTestSeries;
            assertNotNull(media2);

            app.selectMedia(media1, user);

            assertTrue(user.getFavorites().size() == 0);
            user.addFavorite(media1);
            assertTrue(user.getFavorites().size() == 1);

            app.selectMedia(media2, user);

            assertTrue(user.getFavorites().size() == 1);
            user.addFavorite(media2);
            assertTrue(user.getFavorites().size() == 2);

            app.logoutUser();

            app.loginUser("Test2", "abc123");
            assertTrue(data.getUsers().size() == 1);

            app.selectMedia(media1, user);

            assertTrue(user.getFavorites().size() == 2);
            user.removeFavorite(media1);
            assertTrue(user.getFavorites().size() == 1);

            assertTrue(user.isFavorite(media2));

            assertEquals(user.getFavorites().get(0), media2);
        }
    
        @Test
        void addUser() throws Exception {
            Application app = new Application();
            ApplicationData data = getAppData(app);

            assertTrue(data.getUsers().size() == 0);

            app.addUser("Test1", "abc123", "123abc", null);
            assertTrue(data.getUsers().size() == 0);

            app.addUser("Test1", "", "", null);
            assertTrue(data.getUsers().size() == 0);

            app.addUser("", "abc123", "abc123", null);
            assertTrue(data.getUsers().size() == 0);

            app.addUser("Test1", "abc123", "abc123", null);
            assertTrue(data.getUsers().size() == 1);

            app.addUser("Test1", "abc123", "abc123", null);
            assertTrue(data.getUsers().size() == 1);
        }

        @Test
        void deleteUser() throws Exception {
            Application app = new Application();
            ApplicationData data = getAppData(app);

            assertTrue(data.getUsers().size() == 0);

            app.addUser("Test1", "abc123", "abc123", null);
            assertTrue(data.getUsers().size() == 1);

            app.addUser("Test2", "abc123", "abc123", null);
            assertTrue(data.getUsers().size() == 2);

            app.deleteUser("Test3");
            assertTrue(data.getUsers().size() == 2);

            app.deleteUser("Test2");
            assertTrue(data.getUsers().size() == 1);

            app.deleteUser("Test2");
            assertTrue(data.getUsers().size() == 1);

            app.deleteUser("Test1");
            assertTrue(data.getUsers().size() == 0);
        }

        @Test
        void favorites() throws Exception {
            Application app = new Application();
            ApplicationData data = getAppData(app);

            app.addUser("Test1", "abc123", "abc123", null);

            app.loginUser("Test1", "abc123");

            User user = data.getUser("Test1");
            assertNotNull(user);

            Media media1 = domain.TestsDomain.newTestMovie;
            assertNotNull(media1);

            Media media2 = domain.TestsDomain.newTestSeries;
            assertNotNull(media2);

            assertTrue(user.getFavorites().size() == 0);
            user.addFavorite(media1);
            assertTrue(user.getFavorites().size() == 1);

            user.addFavorite(media2);
            assertTrue(user.getFavorites().size() == 2);

            user.addFavorite(media1);
            assertTrue(user.getFavorites().size() == 2);

            user.addFavorite(media2);
            assertTrue(user.getFavorites().size() == 2);

            app.logoutUser();

            app.loginUser("Test1", "abc123");

            assertTrue(user.getFavorites().size() == 2);
            user.removeFavorite(media2);
            assertTrue(user.getFavorites().size() == 1);

            user.removeFavorite(media2);
            assertTrue(user.getFavorites().size() == 1);

            user.removeFavorite(media1);
            assertTrue(user.getFavorites().size() == 0);

            user.removeFavorite(media1);
            assertTrue(user.getFavorites().size() == 0);
        }
    }

}
