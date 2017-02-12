
/** 
 * A container that stores references to instances of Score in a 
 * contiguous and ordered list of those instances.
 */
public interface ScoreListADT {

	/** 
	 * Returns the number of Scores in the list or zero
	 * @return the number of scores in this list
	 */
	int size();
	
	/** 
	 * Adds the score to the end of this list.
	 * 
	 * @param s a non-null Score to place as the last item in the list.
	 * @throws IllegalArgumentException
	 */
	void add(Score s) throws IllegalArgumentException;
	
	/**
	 * Removes and returns the item at index position i.
	 * If i is less than zero or greater than size()-1,
	 * will throw an IndexOutOfBoundsException.
	 * @param i must be greater than or equal to zero and less than size()
	 * @return the item at index i
	 * @throws IndexOutOfBoundsException
	 */
	Score remove(int i) throws IndexOutOfBoundsException;

	/**
	 * Returns (without removing) the item at index position i.
	 * If i is less than zero or greater than size()-1,
	 * will throw an IndexOutOfBoundsException.
	 * @param i must be greater than or equal to zero and less than size()
	 * @return the item at index i
	 * @throws IndexOutOfBoundsException
	 */
	Score get(int i) throws IndexOutOfBoundsException;
}
