import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class SongWorker implements Runnable {

	private Path path;
	private String extension;
	private ConcurrentSongLibrary library;
	
	
	/**
	 * Constructor that takes in the path of json file, and the Thread-safe Songlibrary for work
	 * @param path
	 * @param extension
	 * @param library
	 */
	public SongWorker(Path path, String extension, ConcurrentSongLibrary library){
		this.path = path;
		this.extension = extension;
		this.library = library;
	}
	
	@Override
	public void run() {
		Song song = parseSongs(path, extension);
		this.library.addSong(song);
		
	}
	
	/*
	 * Parse the json file into song.
	 */
	public Song parseSongs(Path path, String extension){
		JSONParser parser = new JSONParser();
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))){

			JSONObject contents = (JSONObject) parser.parse(reader);
			Song song = new Song(contents);
			
			return song;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	

	
	
}
