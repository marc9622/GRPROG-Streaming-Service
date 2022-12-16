package presentation;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Media;
import domain.Movie;
import domain.Series;
import presentation.UIUtils.*;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class InformationPage {
    
    public final JPanel panel;

    public InformationPage(Media media, Function<Media, Boolean> isMediaFavoriteFunction, Consumer<Media> addToFavoritesListener,
                           Consumer<Media> removeFromFavoritesListener, Consumer<Media> playMediaListener, Runnable goBackListner) {
                
        // Creates the panel
        panel = new BackgroundPanel(Images.BACKGROUND());

        { // Sets the layout of the panel
            BorderLayout layout = new BorderLayout();
            panel.setLayout(layout);
            panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        }

        { // Fill in the edges
            panel.add(Fillers.VERTICAL_LARGE(), BorderLayout.NORTH);
            panel.add(Fillers.VERTICAL_LARGE(), BorderLayout.SOUTH);
            panel.add(Fillers.HORIZONTAL_LARGE(), BorderLayout.WEST);
            panel.add(Fillers.HORIZONTAL_LARGE(), BorderLayout.EAST);
        }

        { // Creates the inner panel
            JPanel innerPanel = new JPanel();
            innerPanel.setOpaque(false);
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
            innerPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            panel.add(innerPanel);

            HeaderPanel headerPanel = new HeaderPanel(media.title, goBackListner);
            innerPanel.add(headerPanel);

            BodyPanel bodyPanel = new BodyPanel(media, isMediaFavoriteFunction, addToFavoritesListener,
                                                       removeFromFavoritesListener, playMediaListener);
            innerPanel.add(bodyPanel);
        }

    }

    private static class HeaderPanel extends JPanel {

        public HeaderPanel(String title, Runnable goBackListener) {

            this.setOpaque(false);
            this.setLayout(new BorderLayout());
            this.setAlignmentX(JPanel.LEFT_ALIGNMENT);

            JLabel mediaTitle = new JLabel(title);
            mediaTitle.setFont(mediaTitle.getFont().deriveFont(Fonts.SIZE_TITLE));
            mediaTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            this.add(mediaTitle, BorderLayout.WEST);

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> goBackListener.run());
            backButton.setAlignmentX(JButton.RIGHT_ALIGNMENT);
            this.add(backButton, BorderLayout.EAST);
        }

    }

    private static class BodyPanel extends JPanel {

        public BodyPanel(Media media, Function<Media, Boolean> isMediaFavoriteFunction, Consumer<Media> addToFavoritesListener,
                                    Consumer<Media> removeFromFavoritesListener, Consumer<Media> playMediaListener) {
            this.setOpaque(false);
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            
            MediaImagePanel imagePanel = new MediaImagePanel(media, isMediaFavoriteFunction, addToFavoritesListener,
                                                                    removeFromFavoritesListener, playMediaListener);
            this.add(imagePanel);
            
            this.add(Fillers.HORIZONTAL_SMALL());

            MediaInformationPanel informationPanel = new MediaInformationPanel(media);
            this.add(informationPanel);
        }

        private static class MediaImagePanel extends JPanel {

            public MediaImagePanel(Media media, Function<Media, Boolean> isMediaFavoriteFunction, Consumer<Media> addToFavoritesListener,
                                                Consumer<Media> removeFromFavoritesListener, Consumer<Media> playMediaListener) {
                super();
                
                { // Sets the layout of the panel
                    BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
                    this.setLayout(layout);
                }
    
                this.setOpaque(false);
    
                { // Creates and adds the components
                    final int IMAGE_SCALE = 2;
                    ImageIcon mediaImageIcon = new ImageIcon(media.imagePath);
                    mediaImageIcon = new ImageIcon(mediaImageIcon.getImage().getScaledInstance(
                                                                                mediaImageIcon.getIconWidth() * IMAGE_SCALE,
                                                                                mediaImageIcon.getIconHeight() * IMAGE_SCALE,
                                                                                Image.SCALE_DEFAULT));
                    JLabel mediaImage = new JLabel(mediaImageIcon);
                    mediaImage.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                    this.add(mediaImage);
    
                    this.add(Fillers.VERTICAL_SMALL());
    
                    { // Creates and adds the buttons panel
                        JPanel buttonsPanel = new JPanel();
                        buttonsPanel.setOpaque(false);
                        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
                        buttonsPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                        this.add(buttonsPanel);
    
                        JButton playButton = new JButton("Play");
                        playButton.addActionListener(e -> playMediaListener.accept(media));
                        buttonsPanel.add(playButton);
    
                        buttonsPanel.add(Fillers.HORIZONTAL_SMALL());
    
                        if(isMediaFavoriteFunction.apply(media)) {
                            JButton removeFromFavoritesButton = new JButton("Remove from favorites");
                            removeFromFavoritesButton.addActionListener(e -> removeFromFavoritesListener.accept(media));
                            buttonsPanel.add(removeFromFavoritesButton);
                        }
                        else {
                            JButton addToFavoritesButton = new JButton("Add to favorites");
                            addToFavoritesButton.addActionListener(e -> addToFavoritesListener.accept(media));
                            buttonsPanel.add(addToFavoritesButton);
                        }
                    }
                }
            }  
    
        }

        private static class MediaInformationPanel extends JPanel {

            public MediaInformationPanel(Media media) {
                super();
                
                { // Sets the layout of the panel
                    BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
                    this.setLayout(layout);
                }
    
                this.setOpaque(false);
    
                { // Creates and adds the components
                    List<Component> components = new ArrayList<>();
    
                    JLabel categoriesTitle = new JLabel("Categories:");
                    categoriesTitle.setFont(categoriesTitle.getFont().deriveFont(Fonts.SIZE_LARGE));
                    components.add(categoriesTitle);
    
                    JLabel categories = new JLabel(media.categories.toString());
                    categories.setFont(categories.getFont().deriveFont(Fonts.SIZE_MEDIUM));
                    components.add(categories);
    
                    components.add(Fillers.VERTICAL_SMALL());
    
                    JLabel ratingTitle = new JLabel("Rating:");
                    ratingTitle.setFont(ratingTitle.getFont().deriveFont(Fonts.SIZE_LARGE));
                    components.add(ratingTitle);
    
                    JLabel rating = new JLabel(media.rating + "");
                    rating.setFont(rating.getFont().deriveFont(Fonts.SIZE_MEDIUM));
                    components.add(rating);
    
                    components.add(Fillers.VERTICAL_SMALL());
    
                    if(media instanceof Movie movie) {
                        JLabel fromYearTitle = new JLabel("From:");
                        fromYearTitle.setFont(fromYearTitle.getFont().deriveFont(Fonts.SIZE_LARGE));
                        components.add(fromYearTitle);
    
                        JLabel releaseYear = new JLabel(movie.releaseYear + "");
                        releaseYear.setFont(releaseYear.getFont().deriveFont(Fonts.SIZE_MEDIUM));
                        components.add(releaseYear);
                    }
    
                    if(media instanceof Series series) {
                        JLabel fromToYearsTitle = new JLabel("From - To:");
                        fromToYearsTitle.setFont(fromToYearsTitle.getFont().deriveFont(Fonts.SIZE_LARGE));
                        components.add(fromToYearsTitle);
    
                        if(series.isEnded) {
                            JLabel fromToYears = new JLabel(series.releaseYear + " - " + series.endYear);
                            fromToYears.setFont(fromToYears.getFont().deriveFont(Fonts.SIZE_MEDIUM));
                            components.add(fromToYears);
                        } else {
                            JLabel fromToYears = new JLabel(series.releaseYear + " - Present");
                            fromToYears.setFont(fromToYears.getFont().deriveFont(Fonts.SIZE_MEDIUM));
                            components.add(fromToYears);
                        }
    
                        components.add(Fillers.VERTICAL_SMALL());
    
                        JLabel seasonTitle = new JLabel("Season:");
                        seasonTitle.setFont(seasonTitle.getFont().deriveFont(Fonts.SIZE_LARGE));
                        components.add(seasonTitle);
    
                        JComboBox<Integer> seasons = new JComboBox<>(IntStream.range(1, series.seasonLengths.length() + 1).boxed().toArray(Integer[]::new));
                        seasons.setFont(seasons.getFont().deriveFont(Fonts.SIZE_MEDIUM));
                        seasons.setMaximumSize(new Dimension(100, 100));
                        components.add(seasons);
    
                        components.add(Fillers.VERTICAL_SMALL());
    
                        JLabel episodeTitle = new JLabel("Episode:");
                        episodeTitle.setFont(episodeTitle.getFont().deriveFont(Fonts.SIZE_LARGE));
                        components.add(episodeTitle);
    
                        JComboBox<Integer> episodes = new JComboBox<>();
                        seasons.addActionListener(e -> {
                            episodes.removeAllItems();
                            IntStream.range(1, series.seasonLengths.get(seasons.getSelectedIndex())).forEach(episodes::addItem);
                        });
                        episodes.setFont(episodes.getFont().deriveFont(Fonts.SIZE_MEDIUM));
                        episodes.setMaximumSize(new Dimension(100, 100));
                        components.add(episodes);
                    }
    
                    components.forEach(component -> {if(component instanceof JComponent jcomponent) jcomponent.setAlignmentX(JLabel.LEFT_ALIGNMENT);});
                    components.forEach(this::add);
    
                }
            }  
    
        }

    }

}
