import java.util.Comparator;


public class ByArtistComparator implements Comparator<Song>{

	@Override
	public int compare(Song s1, Song s2){
		if (s1.getArtist().equals(s2.getArtist())){
			if(s1.getTitle().equals(s2.getTitle())){
				
				return s1.getTrackId().compareTo(s2.getTrackId());
			}
			return s1.getTitle().compareTo(s2.getTitle());
		}
		return s1.getArtist().compareTo(s2.getArtist());
	}
}
