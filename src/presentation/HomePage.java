package presentation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.Box.Filler;

import domain.Media;

public class HomePage {
    
    public final JPanel panel;

    private final Header header;
    private final Catalog catalog;

    public HomePage(List<Media> allMedia, Supplier<List<Media>> favoritesGetter, Function<String, List<Media>> searcher, Consumer<Media> selectMediaListener) {
        this.panel = new JPanel();

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
        }

        catalog = new Catalog(allMedia, selectMediaListener);
        panel.add(catalog);

        header = new Header(() -> gotoOverview(allMedia),
                            () -> gotoFavorites(favoritesGetter.get()),
                            () -> gotoSearch(),
                             s -> updateSearch(searcher.apply(s)));
        panel.add(header, 0);

    }

    private void gotoOverview(List<Media> allMedia) {
        // TODO: Also update the header to show that the overview is selected
        catalog.replaceMediaWith(allMedia);
    }
        
    private void gotoFavorites(List<Media> favorites) {
        // TODO: Also update the header to show that the favorites are selected
        catalog.replaceMediaWith(favorites);
    }

    private void gotoSearch() {
        // TODO: Also update the header to show that the search is selected
        catalog.replaceMediaWith(new ArrayList<>());
    }

    private void updateSearch(List<Media> searchResults) {
        catalog.replaceMediaWith(searchResults);
    }

}

class Header extends JPanel {

    public Header(Runnable gotoOverview, Runnable gotoFavorites, Runnable gotoSearch, Consumer<String> searchUpdater) {
        super();

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
            this.setLayout(layout);
            this.setAlignmentX(CENTER_ALIGNMENT);
        }

        { // Creates and adds the components to the panel
            Filler leftLogoFiller = new Filler(new Dimension(0, 0), new Dimension(25, 0), new Dimension(75, 0));
            this.add(leftLogoFiller);

            JLabel logo = new JLabel("1234 Movies"); // Set the logo image here
            this.add(logo);

            Filler logoOverviewFiller = new Filler(new Dimension(0, 0), new Dimension(100, 0), new Dimension(250, 0));
            this.add(logoOverviewFiller);

            JButton overview = new JButton("Overview");
            overview.addActionListener(e -> gotoOverview.run());
            this.add(overview);

            Filler overviewFavoritesFiller = new Filler(new Dimension(0, 0), new Dimension(100, 0), new Dimension(250, 0));
            this.add(overviewFavoritesFiller);

            JButton favorites = new JButton("Favorites");
            favorites.addActionListener(e -> gotoFavorites.run());
            this.add(favorites);

            Filler favoritesSearchFiller = new Filler(new Dimension(0, 0), new Dimension(100, 0), new Dimension(250, 0));
            this.add(favoritesSearchFiller);

            SearchField searchPanel = new SearchField(new Dimension(100, 50), new Dimension(200, 50), new Dimension(300, 100),
                                                      gotoSearch, searchUpdater);
            this.add(searchPanel);

            Filler searchRightFiller = new Filler(new Dimension(0, 0), new Dimension(25, 0), new Dimension(75, 0));
            this.add(searchRightFiller);
        }
    }

}

class SearchField extends JTextField {
    
    private final Runnable gotoSearch;

    public SearchField(Dimension minSize, Dimension prefSize, Dimension maxSize, Runnable gotoSearch, Consumer<String> searchUpdater) {
        super();

        this.setMinimumSize(minSize);
        this.setPreferredSize(prefSize);
        this.setMaximumSize(maxSize);

        this.gotoSearch = gotoSearch;
        
        this.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateSeach(searchUpdater);
            }
            public void removeUpdate(DocumentEvent e) {
                updateSeach(searchUpdater);
            }
            public void insertUpdate(DocumentEvent e) {
                updateSeach(searchUpdater);
            }
        });

    }

    public void focusGained() {
        gotoSearch.run();
    }

    private void updateSeach(Consumer<String> searchUpdater) {
        searchUpdater.accept(this.getText());
    }
    
}

class Catalog extends JPanel {

    private final Consumer<Media> selectMediaListener;

    public Catalog(List<Media> media, Consumer<Media> selectMediaListener) {
        
        this.selectMediaListener = selectMediaListener;

        { // Sets the layout of the panel
            FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 50, 50);
            this.setLayout(layout);
            this.setAlignmentX(CENTER_ALIGNMENT);
        }

        addMediaButtons(media);
    }

    // Creates the media panels
    private void addMediaButtons(List<Media> media) {
        media.forEach(m -> this.add(new MediaPanel(m, selectMediaListener)));
    }

    public void replaceMediaWith(List<Media> media) {
        this.removeAll();
        addMediaButtons(media);
        this.revalidate();
        this.repaint();
    }

}

class MediaPanel extends JPanel {

    public MediaPanel(Media media, Consumer<Media> selectMediaListener) {

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            this.setLayout(layout);
            this.setAlignmentX(CENTER_ALIGNMENT);
        }

        { // Creates and adds the components to the panel
            JButton button = new JButton(media.title) { // Set the image here
                public void fireActionPerformed(ActionEvent event) {
                    selectMediaListener.accept(media);
                }
            };
            this.add(button);
        }

    }

}
