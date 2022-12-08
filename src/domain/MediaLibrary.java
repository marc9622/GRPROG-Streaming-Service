package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MediaLibrary {
    
    private Set<Media> mediaSet;

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
