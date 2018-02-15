import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ConcurrentSongLibrary extends SongLibrary {
	
	private ReentrantLock lock;

	public ConcurrentSongLibrary(){
		super();
		this.lock = new ReentrantLock();
	}
	
	
	/**
	 * Override addSong to make it Thread-safe.
	 */
	@Override
	public void addSong(Song song){
		try{
			lock.lockWrite();
			super.addSong(song);
		}finally{
			lock.unlockWrite();
		}
	}
	

	
	/**
	 * Override printLibrary method to make it Thread-Safe.
	 */
	@Override
	public  void printLibrary(String order, Path outputPath){
		lock.lockRead();
		super.printLibrary(order, outputPath);
		lock.unlockRead();
	}
	

	
	@Override
	public TreeSet<Song> getSongsByArtist(String artist){
		try{
			lock.lockRead();		
			return super.getSongsByArtist(artist);
		}finally{
			lock.unlockRead();
		}
	}
	
	@Override
	public TreeSet<Song> getSongsByTitle(String title){
		try{
			lock.lockRead();		
			return super.getSongsByTitle(title);
		}finally{
			lock.unlockRead();
		}
	}
	
	@Override
	public TreeSet<Song> getSongsByTag(String tag){
		try{
			lock.lockRead();		
			return super.getSongsByTag(tag);
		}finally{
			lock.unlockRead();
		}
	}
	
	@Override
	public Song getSongById(String trackId){
		try{
			lock.lockRead();
			return super.getSongById(trackId);
		}finally{
			lock.unlockRead();
		}
	}

	public TreeSet<JSONObject> searchByArtist(String artist){
		try{
			lock.lockRead();
			TreeSet<JSONObject> resultList = new TreeSet<JSONObject>(new ByTrackIdComparator());
			TreeSet<Song> songs = getSongsByArtist(artist);
			for (Song song: songs){
				ArrayList<String> similarList = song.getSimilars();
				for(int i = 0; i < similarList.size(); i++){
					String trackId = similarList.get(i);
					Song similarSong = getSongById(trackId);
					if(similarSong != null){
						JSONObject tmp = new JSONObject();
						tmp.put("artist", similarSong.getArtist());
						tmp.put("trackId", similarSong.getTrackId());
						tmp.put("title", similarSong.getTitle());
						resultList.add(tmp);
					}
				}
			}
			return resultList;
		}finally{
			lock.unlockRead();
		}
	}
	
	public TreeSet<JSONObject> searchByTitle(String title){
		try{
			lock.lockRead();
			TreeSet<JSONObject> resultList = new TreeSet<JSONObject>(new ByTrackIdComparator());
			TreeSet<Song> songs = getSongsByTitle(title);
			for (Song song: songs){
				ArrayList<String> similarList = song.getSimilars();
				for(int i = 0; i < similarList.size(); i++){
					String trackId = similarList.get(i);
					Song similarSong = getSongById(trackId);
					if(similarSong != null){
						JSONObject tmp = new JSONObject();
						tmp.put("artist", similarSong.getArtist());
						tmp.put("trackId", similarSong.getTrackId());
						tmp.put("title", similarSong.getTitle());
						resultList.add(tmp);
					}
				}
			}
			return resultList;
		}finally{
			lock.unlockRead();
		}
	}
	
	public TreeSet<JSONObject> searchByTag(String tag){
		try{
			lock.lockRead();
			TreeSet<JSONObject> resultList = new TreeSet<JSONObject> ( new ByTrackIdComparator());
			TreeSet<Song> songs = getSongsByTag(tag);
			for (Song song: songs){
				JSONObject tmp = new JSONObject();
				tmp.put("artist", song.getArtist());
				tmp.put("trackId", song.getTrackId());
				tmp.put("title", song.getTitle());
				resultList.add(tmp);
			}
			return resultList;
		
		}finally{
			lock.unlockRead();
		}
	}
	
//	public void searchByQueries(Path searchInputPath, Path searchOutputPath){
//		try{
//			lock.lockRead();
//			System.out.println("Readlock get!");
//			JSONObject result = new JSONObject();
//			
//			
//			ArrayList<String> artistAl = new ArrayList<String>();
//			ArrayList<String> titleAl = new ArrayList<String>();
//			ArrayList<String> tagAl = new ArrayList<String>();
//			
//			if(searchInputPath != null){
//				if(searchInputPath.toString().endsWith(".json".toLowerCase().trim()) || searchInputPath.toString().endsWith(".json".toUpperCase().trim())){
//					JSONParser parser = new JSONParser();
//					try(BufferedReader reader = Files.newBufferedReader(searchInputPath,Charset.forName("UTF-8"))){
//						JSONObject contents = (JSONObject) parser.parse(reader);
//						
//						ArrayList<String> artistFs = (ArrayList<String>)contents.get("searchByArtist");
//						if(artistFs != null && !artistFs.isEmpty()){
//
//							result.put("searchByArtist", new JSONArray());
//							
//							for (int i = 0; i<artistFs.size(); i++){
//								TreeSet<JSONObject> tmp = searchByArtist(artistFs.get(i));
//								JSONArray tmpJa = (JSONArray) result.get("searchByArtist");
//								JSONObject tmpJo = new JSONObject();
//								tmpJo.put("artist", artistFs.get(i));
//								JSONArray similarJa = new JSONArray();
//								for(JSONObject obj:tmp){
//									similarJa.add(obj);
//								}
//								tmpJo.put("similars", similarJa);
//								tmpJa.add(tmpJo);								
//							}	
//						}
//						
//						ArrayList<String> titleFs = (ArrayList<String>)contents.get("searchByTitle");
//						if(titleFs != null && !titleFs.isEmpty()){
//
//							result.put("searchByTitle", new JSONArray());
//							
//							for (int i = 0; i<titleFs.size(); i++){
//								TreeSet<JSONObject> tmp = searchByTitle(titleFs.get(i));
//								JSONArray tmpJa = (JSONArray) result.get("searchByTitle");
//								JSONObject tmpJo = new JSONObject();
//								tmpJo.put("title", titleFs.get(i));
//								JSONArray similarJa = new JSONArray();
//								for(JSONObject obj:tmp){
//									similarJa.add(obj);
//								}
//								tmpJo.put("similars", similarJa);
//								tmpJa.add(tmpJo);								
//							}	
//						}
//						
//						ArrayList<String> tagFs = (ArrayList<String>)contents.get("searchByTag");
//						if( tagFs != null && !tagFs.isEmpty() ){
//
//							result.put("searchByTag", new JSONArray());
//							
//							for (int i = 0; i<tagFs.size(); i++){
//								TreeSet<JSONObject> tmp = searchByTag(tagFs.get(i));
//								JSONArray tmpJa = (JSONArray) result.get("searchByTag");
//								JSONObject tmpJo = new JSONObject();
//								tmpJo.put("tag", tagFs.get(i));
//								JSONArray similarJa = new JSONArray();
//								for(JSONObject obj:tmp){
//									similarJa.add(obj);
//								}
//								tmpJo.put("similars", similarJa);
//								tmpJa.add(tmpJo);								
//							}	
//						}
//						
//						
//							try(BufferedWriter bf = Files.newBufferedWriter(searchOutputPath, Charset.forName("UTF-8"))){
//
//						
//								System.out.print("Created:   ");
//								bf.write(result.toString());	
//							
//
//							
//									bf.flush();
//									bf.close();
//									System.out.println("Write Done And CLOSED!!!!");
//
//						
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//
//						
//						
//						reader.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						}finally{
//					}
//				}
//			}
//			
//			
//		}finally{
//			lock.unlockRead();
//			System.out.println("Readlock released!!");
//		}
//	}
	
	
		
	private class ByTrackIdComparator implements Comparator<JSONObject>{
		public int compare(JSONObject j1, JSONObject j2){
			return ((String)j1.get("trackId")).compareTo((String) j2.get("trackId"));
		}
	}
	
}
