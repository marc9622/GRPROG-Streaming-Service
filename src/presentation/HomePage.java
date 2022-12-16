package presentation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import presentation.UIUtils.*;

import domain.Media;
import domain.MediaSorting.SortBy;

public class HomePage {
    
    public final JPanel panel;

    private final Header header;
    private final Catalog catalog;

    public HomePage(List<Media> allMedia, Supplier<List<Media>> favoritesGetter, Function<SortBy, List<Media>> sorter,
                    Function<String, List<Media>> searcher, Consumer<Media> selectMediaListener, Runnable logoff) {
        this.panel = new BackgroundPanel(Images.BACKGROUND());

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
        }

        catalog = new Catalog(allMedia, selectMediaListener);
        panel.add(catalog);

        header = new Header(() -> gotoOverview(allMedia),
                            () -> gotoFavorites(favoritesGetter.get()),
                             s -> updateSort(sorter.apply(s)),
                            () -> gotoSearch(),
                             s -> updateSearch(searcher.apply(s)),
                            logoff);
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

    private void updateSort(List<Media> sortResults) {
        catalog.replaceMediaWith(sortResults);
    }

    private void gotoSearch() {
        // TODO: Also update the header to show that the search is selected
        catalog.replaceMediaWith(new ArrayList<>());
    }

    private void updateSearch(List<Media> searchResults) {
        catalog.replaceMediaWith(searchResults);
    }

    private static class Header extends JPanel {

        public Header(Runnable gotoOverview, Runnable gotoFavorites, Consumer<SortBy> sortUpdater, Runnable gotoSearch, Consumer<String> searchUpdater, Runnable logoutListener) {
            super();
    
            this.setOpaque(false);
    
            { // Sets the layout of the panel
                BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
                this.setLayout(layout);
                this.setAlignmentX(CENTER_ALIGNMENT);
            }
    
            { // Creates and adds the components to the panel
                this.add(Fillers.HORIZONTAL_SMALL());
    
                JLabel logo = new JLabel("1234 Movies"); // TODO: Add logo
                logo.setFont(logo.getFont().deriveFont(Fonts.SIZE_LARGE));
                this.add(logo);
    
                this.add(Fillers.HORIZONTAL_LARGE());
    
                JButton overview = new JButton("Overview");
                overview.addActionListener(e -> gotoOverview.run());
                this.add(overview);
    
                this.add(Fillers.HORIZONTAL_LARGE());
    
                JButton favorites = new JButton("Favorites");
                favorites.addActionListener(e -> gotoFavorites.run());
                this.add(favorites);
    
                this.add(Fillers.HORIZONTAL_LARGE());

                SortingField sortingField = new SortingField();
                sortingField.addActionListener(e -> sortUpdater.accept((SortBy) sortingField.getSelectedItem()));
                this.add(sortingField);
    
                this.add(Fillers.HORIZONTAL_LARGE());

                SearchField searchField = new SearchField(new Dimension(75, 50), new Dimension(150, 50), new Dimension(250, 100),
                                                          gotoSearch, searchUpdater);
                this.add(searchField);
    
                this.add(Fillers.HORIZONTAL_LARGE());
    
                JButton logOut = new JButton("Log Out");
                logOut.addActionListener(e -> logoutListener.run());
                this.add(logOut);
    
                this.add(Fillers.HORIZONTAL_SMALL());

            }
        }
    
    }

    private static class SortingField extends JComboBox<SortBy> {

        public SortingField() {
            super(SortBy.values());
        }


    }
    
    private static class SearchField extends JTextField {
            
        public SearchField(Dimension minSize, Dimension prefSize, Dimension maxSize, Runnable gotoSearch, Consumer<String> searchUpdater) {
    
            this.setMinimumSize(minSize);
            this.setPreferredSize(prefSize);
            this.setMaximumSize(maxSize);
                
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
    
            this.addFocusListener(new FocusListener() {
                boolean firstFocus = true;

                public void focusGained(FocusEvent e) {
                    if(firstFocus)
                        firstFocus = false;
                    else
                        gotoSearch.run();
                }
                public void focusLost(FocusEvent e) {
                    // Do nothing
                }
            });

        }
    
        private void updateSeach(Consumer<String> searchUpdater) {
            searchUpdater.accept(this.getText());
        }
        
    }
    
    private static class Catalog extends JScrollPane {
    
        private final JPanel innerPanel;
    
        private final Consumer<Media> selectMediaListener;
    
        public Catalog(List<Media> media, Consumer<Media> selectMediaListener) {
            this.selectMediaListener = selectMediaListener;
    
            this.setOpaque(false);
            this.viewport.setOpaque(false);
    
            this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.getVerticalScrollBar().setUnitIncrement(14);
    
            { // Sets the layout of the this outer panel
                ScrollPaneLayout layout = new ScrollPaneLayout();
                this.setLayout(layout);
            }
    
            innerPanel = new JPanel();
            innerPanel.setOpaque(false);
            this.viewport.add(innerPanel);
    
            { // Sets the layout of the inner panel
                WrapLayout layout = new WrapLayout(FlowLayout.CENTER, -1, 0);
                innerPanel.setLayout(layout);
                innerPanel.setAlignmentX(CENTER_ALIGNMENT);
            }
    
            addMediaButtonsTo(media);
        }
    
        /** A layout manager that will lay out components in a wrap like fashion.
         * <p><i> Taken from http://www.camick.com/java/source/WrapLayout.java with very minor modifications.
         * <p> Class explained in https://tips4java.wordpress.com/2008/11/06/wrap-layout/ </i>
         */
        private static class WrapLayout extends FlowLayout {
            
            /** Creates a new flow layout manager with the indicated alignment
             * and the indicated horizontal and vertical gaps.
             * <p>
             * The value of the alignment argument must be one of
             * <code>WrapLayout</code>, <code>WrapLayout</code>,
             * or <code>WrapLayout</code>.
             * @param align the alignment value
             * @param hgap the horizontal gap between components
             * @param vgap the vertical gap between components
             */
            public WrapLayout(int align, int hgap, int vgap) {
                super(align, hgap, vgap);
            }
    
            /** Returns the preferred dimensions for this layout given the
             * <i>visible</i> components in the specified target container.
             * @param target the component which needs to be laid out
             * @return the preferred dimensions to lay out the
             * subcomponents of the specified container
             */
            @Override
            public Dimension preferredLayoutSize(Container target) {
                return layoutSize(target, true);
            }
    
            /** Returns the minimum dimensions needed to layout the <i>visible</i>
             * components contained in the specified target container.
             * @param target the component which needs to be laid out
             * @return the minimum dimensions to lay out the
             * subcomponents of the specified container
             */
            @Override
            public Dimension minimumLayoutSize(Container target) {
                Dimension minimum = layoutSize(target, false);
                minimum.width -= (getHgap() + 1);
                return minimum;
            }
    
            /** Returns the minimum or preferred dimension needed to layout the target
             * container.
             * @param target target to get layout size for
             * @param preferred should preferred size be calculated
             * @return the dimension to layout the target container
             */
            private Dimension layoutSize(Container target, boolean preferred) {
                synchronized (target.getTreeLock()) {
    
                    //  Each row must fit with the width allocated to the containter.
                    //  When the container width = 0, the preferred width of the container
                    //  has not yet been calculated so lets ask for the maximum.
    
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
    
                            //  Can't add the component to current row. Start a new row.
    
                            if (rowWidth + d.width > maxWidth) {
                                addRow(dim, rowWidth, rowHeight);
                                rowWidth = 0;
                                rowHeight = 0;
                            }
    
                            //  Add a horizontal gap for all components after the first
    
                            if (rowWidth != 0)
                                rowWidth += hgap;
    
                            rowWidth += d.width;
                            rowHeight = Math.max(rowHeight, d.height);
                        }
                    }
    
                    addRow(dim, rowWidth, rowHeight);
    
                    dim.width += horizontalInsetsAndGap;
                    dim.height += insets.top + insets.bottom + vgap * 2;
    
                    //     When using a scroll pane or the DecoratedLookAndFeel we need to
                    // make sure the preferred size is less than the size of the
                    // target containter so shrinking the container size works
                    // correctly. Removing the horizontal gap is an easy way to do this.
    
                    Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
                    if (scrollPane != null)
                        dim.width -= (hgap + 1);
    
                    return dim;
                }
            }
    
            /** A new row has been completed. Use the dimensions of this row
             * to update the preferred size for the container.
             * @param dim update the width and height when appropriate
             * @param rowWidth the width of the row to add
             * @param rowHeight the height of the row to add
             */
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
    
        private static class MediaPanel extends JPanel {
    
            public MediaPanel(Media media, Consumer<Media> selectMediaListener) {
        
                this.setOpaque(false);
        
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

    }
    
}
