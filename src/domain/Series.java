package domain;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** A series.
 * Uses only immutable fields, to ensure that no media
 * can be changed after it has been added to a library.
*/
public class Series extends Media {

    private final boolean isEnded;
    
    /** <i>Should only be used if {@link #isEnded} is {@code true}</i>.*/
    private final int endYear;

    /** The number of episodes per season in order. The indices are therefore the seasons numbers.*/
    private final ImmutableArray<Integer> seasonLengths;

    /**
     * @param title The title of the serie.
     * @param releaseYear The year the serie started.
     * @param isEnded Whether the serie has ended.
     * @param endYear The year the serie ended. <i>Only relevant if {@link #isEnded} is {@code true}</i>.
     * @param categories The categories the serie belongs to.
     * @param rating The rating of the serie.
     * @param seasonLengths The number of episodes per season in order. The indices are therefore the seasons numbers.
     */
    Series(String title, int releaseYear, boolean isEnded, int endYear, String[] categories, float rating, int[] seasonLengths) {
        super(title, releaseYear, categories, rating);
        this.isEnded = isEnded;
        this.endYear = endYear;
        this.seasonLengths = new ImmutableArray<Integer>(IntStream.of(seasonLengths).boxed().toArray(Integer[]::new));
    }

    private String getSeasonLengthsString() {
        return IntStream.range(0, seasonLengths.length())
                        .mapToObj(i -> (i + 1) + "-" + seasonLengths.get(i))
                        .collect(Collectors.joining(", "));
    }

    public String toString() {
        return title + "; " +
               releaseYear + "- " +
               (isEnded ? endYear : "") + "; " +
               getCategoriesString() + "; " +
               rating + "; " +
               getSeasonLengthsString() + ";";
    }

    public int hashCode() {
        int result = 29;
        result = 39 * result + title.hashCode();
        result = 39 * result + releaseYear;
        result = 39 * result + (isEnded ? 1 : 0);
        result = 39 * result + endYear;
        result = 39 * result + categories.hashCode();
        result = 39 * result + Float.floatToIntBits(rating);
        result = 39 * result + seasonLengths.hashCode();
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return true;

        if (obj == this)
            return true;

        if (!(obj instanceof Series))
            return false;
        
        Series other = (Series) obj;
        return title.equals(other.title) &&
               releaseYear == other.releaseYear &&
               isEnded == other.isEnded &&
               endYear == other.endYear &&
               categories.equals(other.categories) &&
               rating == other.rating &&
               seasonLengths.equals(other.seasonLengths);
    }
}