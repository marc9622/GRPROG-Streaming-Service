package presentation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
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

            SearchField searchPanel = new SearchField(new Dimension(50, 50), new Dimension(100, 50), new Dimension(150, 100),
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

class Catalog extends JScrollPane {

    private final JPanel innerPanel;

    private final Consumer<Media> selectMediaListener;

    public Catalog(List<Media> media, Consumer<Media> selectMediaListener) {

        this.selectMediaListener = selectMediaListener;

        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getVerticalScrollBar().setUnitIncrement(14);

        { // Sets the layout of the this outer panel
            ScrollPaneLayout layout = new ScrollPaneLayout();
            this.setLayout(layout);
        }

        innerPanel = new JPanel();
        this.viewport.add(innerPanel);

        { // Sets the layout of the inner panel
            WrapLayout layout = new WrapLayout(FlowLayout.CENTER, -1, 0);
            innerPanel.setLayout(layout);
            innerPanel.setAlignmentX(CENTER_ALIGNMENT);
        }

        addMediaButtonsTo(media);
    }

    /** A layout manager that will lay out components in a wrap like fashion.
     * <p><i> Taken from https://gist.github.com/jirkapenzes/4560255 with very minor modifications.
     * <p> Class explained in https://tips4java.wordpress.com/2008/11/06/wrap-layout/ </i>
     */
    private static class WrapLayout extends FlowLayout {
        
        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;

                if (targetWidth == 0)
                    targetWidth = Integer.MAX_VALUE;

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = targetWidth - horizontalInsetsAndGap;

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;

                int nmembers = target.getComponentCount();

                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);

                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

                        if (rowWidth + d.width > maxWidth) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0;
                            rowHeight = 0;
                        }

                        if (rowWidth != 0)
                            rowWidth += hgap;

                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }

                addRow(dim, rowWidth, rowHeight);

                dim.width += horizontalInsetsAndGap;
                dim.height += insets.top + insets.bottom + vgap * 2;

                Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
                if (scrollPane != null)
                    dim.width -= (hgap + 1);

                return dim;
            }
        }

        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);

            if (dim.height > 0)
                dim.height += getVgap();

            dim.height += rowHeight;
        }
    }

    // Creates the media panels
    private void addMediaButtonsTo(List<Media> media) {
        this.invalidate();
        media.forEach(m -> innerPanel.add(new MediaPanel(m, selectMediaListener)));
        this.validate();
    }

    public void replaceMediaWith(List<Media> media) {
        innerPanel.removeAll();
        addMediaButtonsTo(media);
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
            JButton button = new JButton(new ImageIcon(media.imagePath)) {
                public void fireActionPerformed(ActionEvent event) {
                    selectMediaListener.accept(media);
                }
            };
            button.setAlignmentX(CENTER_ALIGNMENT);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setOpaque(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            this.add(button);

            JLabel label = new JLabel(media.title) {
                public Dimension getPreferredSize() {
                    return new Dimension(button.getPreferredSize().width, super.getPreferredSize().height);
                }
            };
            label.setAlignmentX(CENTER_ALIGNMENT);
            this.add(label);
        }

    }

}
