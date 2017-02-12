/**
 * This class is an object which holds information for an assignment including its name, points scored, and points possible
 * @author Cory Van Beek
 *
 */
public class Score {

	private String name;
	private double points;
	private double maxPoints;
	
	/**
	 * Generates a object of type Score
	 * @param name The name of the assignment
	 * @param points The number of points the student got on the assignment
	 * @param maxPoints The number of possible points in the assignment
	 */
	public Score(String name, double points, double maxPoints){
		//Check for valid parameters
		if(points < 0 || points > maxPoints || name == null || maxPoints < 0)
			throw new IllegalArgumentException();
			
		this.name = name;
		this.points = points;
		this.maxPoints = maxPoints;
	}
	
	/**
	 * @return the maxPoints
	 */
	public double getMaxPossible() {
		return maxPoints;
	}
	
	/**
	 * @return the points
	 */
	public double getPoints() {
		return points;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the first letter of the category
	 */
	public String getCategory(){
		return name.substring(0, 1);
	}
	
	/**
	 * Gets the percentage score for the assignment
	 * @return The percentage
	 */
	public double getPercent(){
		//turns the raw score and max score into a percentage value.
		return (points / maxPoints) * 100.0;
	}
	
	
}
