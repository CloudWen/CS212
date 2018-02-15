import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class P3P1tester {
	
	private ReentrantLock lock;
	private ConcurrentSongLibrary csl;
	
	public P3P1tester(ConcurrentSongLibrary csl){
		this.lock = new ReentrantLock();
		this.csl = csl;
	}

	
	public void searchByQueries(Path searchInputPath, Path searchOutputPath){
		try{
//			lock.lockRead();
			System.out.println("Readlock get!");
			JSONObject result = new JSONObject();
			
			
			ArrayList<String> artistAl = new ArrayList<String>();
			ArrayList<String> titleAl = new ArrayList<String>();
			ArrayList<String> tagAl = new ArrayList<String>();
			
			if(searchInputPath != null){
				if(searchInputPath.toString().endsWith(".json".toLowerCase().trim()) || searchInputPath.toString().endsWith(".json".toUpperCase().trim())){
					JSONParser parser = new JSONParser();
					try(BufferedReader reader = Files.newBufferedReader(searchInputPath,Charset.forName("UTF-8"))){
						JSONObject contents = (JSONObject) parser.parse(reader);
						
						ArrayList<String> artistFs = (ArrayList<String>)contents.get("searchByArtist");
						if(artistFs != null && !artistFs.isEmpty()){

							result.put("searchByArtist", new JSONArray());
							
							for (int i = 0; i<artistFs.size(); i++){
								TreeSet<JSONObject> tmp = csl.searchByArtist(artistFs.get(i));
								JSONArray tmpJa = (JSONArray) result.get("searchByArtist");
								JSONObject tmpJo = new JSONObject();
								tmpJo.put("artist", artistFs.get(i));
								JSONArray similarJa = new JSONArray();
								for(JSONObject obj:tmp){
									similarJa.add(obj);
								}
								tmpJo.put("similars", similarJa);
								tmpJa.add(tmpJo);								
							}	
						}
						
						ArrayList<String> titleFs = (ArrayList<String>)contents.get("searchByTitle");
						if(titleFs != null && !titleFs.isEmpty()){

							result.put("searchByTitle", new JSONArray());
							
							for (int i = 0; i<titleFs.size(); i++){
								TreeSet<JSONObject> tmp = csl.searchByTitle(titleFs.get(i));
								JSONArray tmpJa = (JSONArray) result.get("searchByTitle");
								JSONObject tmpJo = new JSONObject();
								tmpJo.put("title", titleFs.get(i));
								JSONArray similarJa = new JSONArray();
								for(JSONObject obj:tmp){
									similarJa.add(obj);
								}
								tmpJo.put("similars", similarJa);
								tmpJa.add(tmpJo);								
							}	
						}
						
						ArrayList<String> tagFs = (ArrayList<String>)contents.get("searchByTag");
						if( tagFs != null && !tagFs.isEmpty() ){

							result.put("searchByTag", new JSONArray());
							
							for (int i = 0; i<tagFs.size(); i++){
								TreeSet<JSONObject> tmp = csl.searchByTag(tagFs.get(i));
								JSONArray tmpJa = (JSONArray) result.get("searchByTag");
								JSONObject tmpJo = new JSONObject();
								tmpJo.put("tag", tagFs.get(i));
								JSONArray similarJa = new JSONArray();
								for(JSONObject obj:tmp){
									similarJa.add(obj);
								}
								tmpJo.put("similars", similarJa);
								tmpJa.add(tmpJo);								
							}	
						}
						
						
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

						
						
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (org.json.simple.parser.ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}finally{
					}
					
					//Try 1
					try(BufferedWriter bf = Files.newBufferedWriter(searchOutputPath, Charset.forName("UTF-8"))){

						
						System.out.print("Created:   ");
						bf.write(result.toString());	
					

					
							bf.flush();
							bf.close();
							System.out.println("Write Done And CLOSED!!!!");

				
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//Try 2
//					BufferedWriter bf = Files.newBufferedWriter(searchOutputPath, Charset.forName("UTF-8"));
//					
//					try{
//						bf.write(result.toString());
//					}finally{
//						bf.flush();
//						bf.close();
//					}
					
					
					//Try 3
//					BufferedWriter bf;
//					bf = Files.newBufferedWriter(searchOutputPath, Charset.forName("UTF-8"));
//					bf.write(result.toString());
//					bf.flush();
//					bf.close();
					
				//Try 4
//				BufferedWriter bf = Files.newBufferedWriter(searchOutputPath, Charset.forName("UTF-8"));
//				try{	
//					bf.write(result.toString());
//				}finally{
//					if (bf != null){
//						bf.flush();
//						bf.close();
//					}
//				}
				}
			}
			
			
		}finally{
//			lock.unlockRead();
			System.out.println("Readlock released!!");
		}
	}
	
}
