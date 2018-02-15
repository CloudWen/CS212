import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.simple.JSONObject;


public class SongLibrary {

	
	private TreeMap<String, TreeSet<Song>> songByArtist;
	private TreeMap<String, TreeSet<Song>> songByTitle;
	private TreeMap<String, TreeSet<Song>> songByTag;

	private TreeMap<String, Song> songByTrackId;
	
	
	public SongLibrary(){

		songByTrackId = new TreeMap<String, Song>();
		songByArtist = new TreeMap<String, TreeSet<Song>>();
		songByTitle = new TreeMap<String, TreeSet<Song>>();
		songByTag = new TreeMap<String, TreeSet<Song>>();
	}
	
	
	
	// Method that add the same song into different TreeMap by different order.
	
	public void addSong(Song song){

		songByTrackId.put(song.getTrackId(), song);
		if (!songByArtist.containsKey(song.getArtist())){
			songByArtist.put(song.getArtist(), new TreeSet<Song>(new ByArtistComparator()));
		}
		songByArtist.get(song.getArtist()).add(song);
		
		if (!songByTitle.containsKey(song.getTitle())){
			songByTitle.put(song.getTitle(), new TreeSet<Song>(new ByTitleComparator()));
		}
		songByTitle.get(song.getTitle()).add(song);
		
		
		if (song.getTags().size() != 0){		
			for(int i=0; i<song.getTags().size(); i++){

				if (!songByTag.containsKey(song.getTags().get(i))){
					songByTag.put(song.getTags().get(i), new TreeSet<Song>(new ByTagCompartor()));
					songByTag.get(song.getTags().get(i)).add(song);
				}
				songByTag.get(song.getTags().get(i)).add(song);
			}
		}
	}
	
	
//TODO: because these read methods are not overridden the Concurrent library is not thread safe.	
	//Return the TreeSet of songs which searched by artist.
	
	public TreeSet<Song> getSongsByArtist(String artist){	
		
		
		
		TreeSet<Song> copy = new TreeSet<Song>(new ByArtistComparator());
		TreeSet<Song> tmp = this.songByArtist.get(artist);
		if (tmp != null){
			for (Song s: tmp){
			
				copy.add(s.clone());
			}
		}
		
		return copy;
	}
	
	//Return the TreeSet of songs which searched by title.
	
	public TreeSet<Song> getSongsByTitle(String title){
		TreeSet<Song> copy = new TreeSet<Song>(new ByTitleComparator());
		TreeSet<Song> tmp = this.songByTitle.get(title);
		if(tmp != null){
			for (Song s: tmp){
				copy.add(s.clone());
			}
		}
		return copy;		
	}
	
	//Return the TreeSet of songs which searched by Tag.
	
	public TreeSet<Song> getSongsByTag(String tag){
		TreeSet<Song> copy = new TreeSet<Song>(new ByTagCompartor());
		TreeSet<Song> tmp = this.songByTag.get(tag);
		if(tmp != null){
			for (Song s: tmp){
				copy.add(s.clone());
			}
		}
		return copy;
	}
	
	public Song getSongById(String trackId) {
		if(songByTrackId.containsKey(trackId)){
			Song copy = this.songByTrackId.get(trackId).clone();
			return copy;
		}
		return null;
	}
	
	//Output method.
	
	public void printLibrary(String order, Path outputPath){
		
		
		String outPath = String.valueOf(outputPath);
		
		if ((order != null) && (order.equals("artist") || order.equals("title"))){	
			
			try{
				
				TreeMap<String, TreeSet<Song>> tml = new TreeMap<>();
				if (order.equals("artist")){
//					tml = sl.getSongsByArtist();
					tml = this.songByArtist;
				}
				else{
//					tml = sl.getSongsByTitle();
					tml = this.songByTitle;
				}
				BufferedWriter bf = Files.newBufferedWriter(outputPath, Charset.forName("UTF-8"));
				Set keySet = tml.keySet();
				
				for (Object key: keySet){
					TreeSet<Song> local = tml.get(key);
					for (Song song:local){
						bf.write(song.toString(song));
				}
			}
				bf.flush();
				bf.close();
			}
			
			catch (IOException e){
				e.printStackTrace();
			}
		}

		
		else if ((order != null) && (order.equals("tag"))){
			try{
//				TreeMap<String, TreeSet<Song>> tml = sl.getSongsByTag();

				TreeMap<String, TreeSet<Song>> tml = this.songByTag;
				BufferedWriter bf = Files.newBufferedWriter(outputPath, Charset.forName("UTF-8"));
				Set keySet = tml.keySet();
				for (Object key: keySet){
					TreeSet<Song> local = tml.get(key);
					bf.write(String.format("%s: ", key));
					for (Song song:local){
						bf.write(String.format("%s ", song.getTrackId()));
					}
					bf.write(String.format("%n"));
			}
				bf.flush();
				bf.close();
		}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}
