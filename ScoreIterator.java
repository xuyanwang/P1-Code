import java.util.NoSuchElementException;

/**
 * An iterator to go through a scoreList
 * 
 * @author Cory Van Beek
 *
 */
public class ScoreIterator implements ScoreIteratorADT {

	private ScoreList list;
	private int pointer;
	
	/**
	 * Construct an iterator iterate through a given ScoreList 
	 * considering only the items in the ScoreList that match a given category.
	 * What if the input category does not exist? Throw an exception?
	 * @param list
	 * @param cat
	 */
	public ScoreIterator(ScoreList l, String cat) {
		this.list = new ScoreList();
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).getCategory().equals(cat.substring(0,1))) {
				this.list.add(l.get(i));
			}
		}
		this.pointer = 0;
	}
	
	@Override
	public boolean hasNext() {
		return pointer < list.size();
	}

	@Override
	public Score next() {
		if(!hasNext())
			throw new NoSuchElementException();
		
		return list.get(pointer++);
	}
	
}
