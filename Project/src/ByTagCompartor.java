import java.util.Comparator;


public class ByTagCompartor implements Comparator<Song> {
	
	@Override
	public int compare (Song s1, Song s2){
		return s1.getTrackId().compareTo(s2.getTrackId());
	}
}
