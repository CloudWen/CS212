import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Song {

	private String artist;
	private String trackId;
	private String title;
	private ArrayList<String> similars;
	private ArrayList<String> tags;
	
	/**
	 * Constructor that takes as input a JSONObject as illustrated in the example above and
	 * constructs a Song object by extract the relevant data.
	 * @param object
	 */
	public Song(JSONObject object) {
		this.artist = (String) object.get("artist");
		this.trackId = (String) object.get("track_id");
		this.title = (String) object.get("title");
		
		
		ArrayList<String> similarsList = new ArrayList<String>();	
		JSONArray similars = (JSONArray) object.get("similars");
		
		for (int i=0; i<similars.size(); i++){
			similarsList.add((String) ((JSONArray) similars.get(i)).get(0));
		}
		
		this.similars = similarsList;
		
		ArrayList<String> stringList = new ArrayList<String>();
		JSONArray objectTags = (JSONArray) object.get("tags");

		for (int i=0; i<objectTags.size(); i++){
			stringList.add((String) ((JSONArray) objectTags.get(i)).get(0));
		}
		
		this.tags = stringList;
		
	}
	
	public Song(){
		this.artist = null;
		this.trackId = null;
		this.title = null;
		this.similars = new ArrayList<String>();
		this.tags = new ArrayList<String>();
	}
	/**
	 * Return artist.
	 * @return
	 */
	
	public String getArtist() {
		return this.artist;
	}

	/**
	 * Return track ID.
	 * @return
	 */
	public String getTrackId() {
		return this.trackId;
	}

	/**
	 * Return title.
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Return a list of the track IDs of all similar tracks.
	 * @return
	 */
	public ArrayList<String> getSimilars() {
		return this.similars;
	}

	/**
	 * Return a list of all tags for this track.
	 * @return
	 */
	public ArrayList<String> getTags() {
		return this.tags;
	}

	public String toString(Song song){
		return String.format("%s - %s%n", song.getArtist(),song.getTitle());
	}
	
	/**
	 * Return a deep copy of Song.
	 */
	@Override
	public Song clone(){
		Song copy = new Song();
		copy.artist = this.artist;
		copy.title = this.title;
		copy.trackId = this.trackId;
		for(String s:this.similars){
			copy.similars.add(s);
		}
		for(String t:this.tags){
			copy.tags.add(t);
		}
		return copy;
	}
}
