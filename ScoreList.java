/**
 * A ScoreList is a container used to hold a student's scores on assignments
 * This class implements the ScoreListADT
 * 
 * @author Cory Van Beek
 * 
 */
public class ScoreList implements ScoreListADT{

	//size is the number of non-null elements in the scores array.
	private int size;
	private Score[] scores;
	
	/**
	 * Constructs an object of the type ScoreList.
	 *
	 * Assigns an initial size of 0 and an initial array length of 50.
	 */
	public ScoreList(){
		this.size = 0;
		//50 seems like a good number that won't make us expand the array a lot or take up too much room
		this.scores = new Score[50];
	}
	
	
	@Override
	/**
	 * (non-Javadoc)
	 * @see ScoreListADT#size()
	 */
	public int size() {
		return size;
	}

	
	@Override
	/**
	 * (non-Javadoc)
	 * @see ScoreListADT#add(Score)
	 */
	public void add(Score s) throws IllegalArgumentException {
		//Checks that the param isn't null
		if(s == null){
			throw new IllegalArgumentException();
		}
		
		else{
			//checks if the array is full and expands if it is
			if(size == scores.length) {
				//Makes a new temp array twice the size of scores
				Score[] temp = new Score[size * 2];
				
				//Assigns scores' values to the temp array
				for(int i = 0; i < size; i++){
					temp[i] = scores[i];
				}
				
				//points scores to the new, twice as big, temp array
				scores = temp;
			}
			
			//Adds s to the end of the list
			scores[size++] = s;
		}
		
	}

	@Override
	/**
	 * (non-Javadoc)
	 * @see ScoreListADT#remove(int)
	 */
	public Score remove(int i) throws IndexOutOfBoundsException {
		//checks if param is meets preconditions
		if(i < 0 || i >= this.size){
			throw new IndexOutOfBoundsException();
		}
		
		else{
			Score temp = scores[i];
			
			//a loop to shift all the scores over to the left to fill the spot of the removed score
			for(int j = i; j < size - 1; j++){
				scores[j] = scores[j + 1];
			}
			
			//Decrease size by one for the deleted Score and return that score
			size --;
			return temp;
		}
	}

	@Override
	/**
	 * (non-Javadoc)
	 * @see ScoreListADT#get(int)
	 */
	public Score get(int i) throws IndexOutOfBoundsException {
		//Checks if param is valid
		if(i < 0 || i >= this.size){
			throw new IndexOutOfBoundsException();
		}
		
		//Returns the score
		else {
			return this.scores[i];
		}
	}

}
