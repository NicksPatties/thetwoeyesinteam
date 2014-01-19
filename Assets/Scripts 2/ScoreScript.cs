using UnityEngine;
using System.Collections;

public class ScoreScript : MonoBehaviour {
	
	int score;


	void Start () {
		score = 0;

		formatGUIText();
	}


	void formatGUIText(){
		string scoreString = string.Format("{0:D10}", score);
		gameObject.guiText.text = scoreString;
	}


	/**
	 * Adds points to the GUI Score text object
	 * 
	 * input: int points
	 * 		the number of points to add to the score
	 * 
	 * output: void
	 * 		changes the GUI Text object to the new score
	 */
	public void addPoints(int points){

		score = score + points;

		// format and place the string into GUIText object
		formatGUIText ();
	}


	/**
	 * A convenience function that sets the score to a given value.
	 * 
	 * input: int points
	 * 		sets the score to the points value
	 * 
	 * output: void
	 * 		the GUI Text object to is set to points
	 * 
	 */ 
	public void setScore(int points){
		score = points;

		// format and place the string into GUIText object
		formatGUIText ();
	}


	void testScoreGUI(){
	}


	void Update () {
	}
}
