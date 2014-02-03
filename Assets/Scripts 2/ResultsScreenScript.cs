using UnityEngine;
using System.Collections;

public class ResultsScreenScript : MonoBehaviour {

	//variables that will be acquired from the gameplay
	int score;
	float remainingTime;
	
	//gameobjects of different elements of the results screen
	GameObject successOrFailMessage;
	GameObject stevensPortrait;
	GameObject timeBonusText;
	GameObject timeBonusIcon;
	GameObject totalScoreText;
	GameObject totalScoreIcon;


	void Start () {
		score = 0;
		remainingTime = 0f;

		successOrFailMessage = GameObject.Find("Success or Fail Message");
		stevensPortrait = GameObject.Find ("Steven's Portrait");
		timeBonusText = GameObject.Find ("Time Bonus Text");
		totalScoreText = GameObject.Find("Total Score Text");
		totalScoreIcon = GameObject.Find("Total Score Icon");


		//testing to see if I'm grabbing the correct game objects
		print (successOrFailMessage);
		print (stevensPortrait);
		print (totalScoreText);
		print (totalScoreIcon);

	}


	/*
	 * set the score and remaining time values to be used in the results screen
	 * 
	 * input: int s (score), float rt (remaining time)
	 * 
	 * output: void
	 * 		assigns the score and remaining time to use for final score calculations
	 */
	void setScoreAndRemainingTime(int s, float rt){
		score = s;
		remainingTime = rt;
	}


	void Update () {
	
	}
}
