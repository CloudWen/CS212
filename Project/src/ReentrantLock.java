import java.util.HashMap;
import java.util.Set;

/**
 * A read/write lock that allows multiple readers, disallows multiple writers, and allows a writer to 
 * acquire a read lock while holding the write lock. 
 * 
 */
public class ReentrantLock {

	//Declare data members here!
	private HashMap<Long, Integer> readers;
	private HashMap<Long, Integer> writers;
	

	/**
	 * Construct a new ReentrantLock.
	 */
	public ReentrantLock() {
		this.readers = new HashMap<>();
		this.writers = new HashMap<>();
	}

	/**
	 * Returns true if the invoking thread holds a read lock.
	 * @return
	 */
	public synchronized boolean hasRead() {
		
		long id = Thread.currentThread().getId();
		return readers.containsKey(id);
	}

	/**
	 * Returns true if the invoking thread holds a write lock.
	 * @return
	 */
	public synchronized boolean hasWrite() {

		long id = Thread.currentThread().getId();
		return writers.containsKey(id);
	}

	/**
	 * Non-blocking method that attempts to acquire the read lock.
	 * Returns true if successful.
	 * @return
	 */
	public synchronized boolean tryLockRead() {
		
		long id = Thread.currentThread().getId();		
		if (writers.size() == 0 || (this.writers.size() == 1 && this.writers.containsKey(id))){
			if (this.readers.get(id) == null){
				this.readers.put(id, 1);
			}
			else{
				int i = (int) this.readers.get(id);
			    i++;
			    this.readers.put(id, i);
			}
			return true;
		}
		return false;
	}

	/**
	 * Non-blocking method that attempts to acquire the write lock.
	 * Returns true if successful.
	 * @return
	 */	
	public synchronized boolean tryLockWrite() {
		
		long id = Thread.currentThread().getId();
		if (readers.size() == 0 && writers.size() == 0 || (readers.size() != 0 && this.writers.size() == 1 && this.writers.containsKey(id))){
			if (this.writers.get(id) == null){
				this.writers.put(id, 1);
			}
			else{
				int i = (int) this.writers.get(id);
				i++;
				this.writers.put(id, i);
			}
			return true;
		}
		return false;
	}

	/**
	 * Blocking method that will return only when the read lock has been 
	 * acquired.
	 */	 
	public synchronized void lockRead() {
		while (!tryLockRead()){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Releases the read lock held by the calling thread. Other threads may continue
	 * to hold a read lock.
	 */
	public synchronized void unlockRead() {
		long id =  Thread.currentThread().getId();
		if (this.readers.get(id) != null ){
			if (this.readers.get(id) == 1){
				this.readers.remove(id);
				try{
					notifyAll();
				}
				catch(IllegalMonitorStateException e){
					e.printStackTrace();
				}
			}
			else if(this.readers.get(id) > 1){
				int i = this.readers.get(id);
				i--;
				this.readers.put(id, i);
			}
		}
	}

	/**
	 * Blocking method that will return only when the write lock has been 
	 * acquired.
	 */
	public synchronized void lockWrite() {
		while (!tryLockWrite()){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Releases the write lock held by the calling thread. The calling thread may continue to hold
	 * a read lock.
	 */
	public synchronized void unlockWrite() {
		long id = Thread.currentThread().getId();
		if (this.writers.get(id) != null){
			if (this.writers.get(id) == 1){
				this.writers.remove(id);
				try{
					notifyAll();
				}
				catch(IllegalMonitorStateException e){
					e.printStackTrace();
				}
			}
			else if(this.writers.get(id) > 1){
				int i = this.writers.get(id);
				i--;
				this.writers.put(id, i);
			}
		}
//TODO: see above.		
		
	}
}
