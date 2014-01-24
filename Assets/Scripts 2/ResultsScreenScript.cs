using UnityEngine;
using System.Collections;

public class ResultsScreenScript : MonoBehaviour {

	//variables that will be acquired from the gameplay
	int score;
	float remainingTime;


	//gameobjects of different elements of the results screen
	GameObject totalScoreText;


	void Start () {
		score = 0;
		remainingTime = 0f;

		totalScoreText = GameObject.Find("Total Score Text");
		totalScoreText.guiText.text = string.Format("{0:D10}", score);
	}


	void setScoreAndRemainingTime(int s, float rt){
		score = s;
		remainingTime = rt;
	}


	void Update () {
	
	}
}
