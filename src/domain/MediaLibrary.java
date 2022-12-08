package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.MediaSorting.SearchCache;

public class MediaLibrary {
    
    private Set<Media> mediaSet;
    private SearchCache searchCache;

    public MediaLibrary() {
        mediaSet = new HashSet<Media>();
    }

    public List<Media> getMedia() {
        return new ArrayList<Media>(mediaSet);
    }

    public List<Media> search(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
