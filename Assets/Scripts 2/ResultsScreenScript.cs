using UnityEngine;
using System.Collections;

public class ResultsScreenScript : MonoBehaviour {

	//variables that will be acquired from the gameplay
	public float finalTime;
	
	//gameobjects of different elements of the results screen
	public Transform successOrFailMessage;
	public Transform stevensPortrait;
	public Transform timeBonusText;
	public Transform timeBonusIcon;
	public Transform totalScoreText;
	public Transform totalScoreIcon;


	void Start () {
		remainingTime = 0f;

		successOrFailMessage = transform.Find("Success or Fail Message");
		stevensPortrait = transform.Find ("Steven's Portrait");
		timeBonusText = transform.Find ("Time Bonus Text");
		totalScoreText = transform.Find("Total Score Text");
		totalScoreIcon = transform.Find("Total Score Icon");


	}


	/*
	 * set the score and remaining time values to be used in the results screen
	 * 
	 * input: int s (score), float rt (remaining time)
	 * 
	 * output: void
	 * 		assigns the score and remaining time to use for final score calculations
	 */
	void setFinalTime(float rt){
		finalTime = rt;
	}


	void Update () {
	
	}
}
