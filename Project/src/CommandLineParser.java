import java.nio.file.Path;
import java.nio.file.Paths;


public class CommandLineParser {
	
	private Path inputPath;
	private Path outputPath;
	private String order;
	private int numThreads;
	
	private Path searchInputPath;
	private Path searchOutputPath;
	
	//Constructor takes command line arguments as input,
	//parses and stores them as data member in this class.
	
	public CommandLineParser(String[] args){
		for (int i=0; i<args.length; i+=2){
			if(args[i].equals("-input")){
				this.inputPath = Paths.get(args[i+1]);
			}
			else if(args[i].equals("-output")){
				this.outputPath = Paths.get(args[i+1]);
			}
			else if(args[i].equals("-order")){
				this.order = args[i+1];
			}
			
			else if(args[i].equals("-searchInput")){
				this.searchInputPath = Paths.get(args[i+1]);
			}
			else if (args[i].equals("-searchOutput")){
				this.searchOutputPath = Paths.get(args[i+1]);
			}
			
			else if(args[i].equals("-threads")){
				try{
					int a = Integer.valueOf(args[i+1]);
					if (a > 0 && a <= 1000){
						this.numThreads = a;
					}
					else{
						this.numThreads = 10;

					}
				}
				catch (NumberFormatException e){

					this.numThreads = 10;
				}
			}
			else{

				this.numThreads = 10;
			}
		}
		if(this.numThreads == 0){
			this.numThreads = 10;
		}
	}
	
	public Path getInputPath(){
		return this.inputPath;
	}
	
	public Path getOutputPath(){
		return this.outputPath;
	}
	
	public String getOrder(){
		return this.order;
	}
	
	public int getNumThreads(){
		return this.numThreads;
	}
	
	public Path getSearchInputPath(){
		return this.searchInputPath;
	}
	
	public Path getSearchOutputPath(){
		return this.searchOutputPath;
	}
}
