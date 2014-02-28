using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class ResultsScreenScript : MonoBehaviour {

	//variables that will be acquired from the gameplay
	public float finalTime;
	public bool enable;
	private bool enabled;
	
	//gameobjects of different elements of the results screen
	private Transform backDrop;
	private Transform successOrFailMessage;
	private Transform stevensPortrait;
	private Transform playerTime;
	private Transform timeIcon;
	private Transform bestTime;
	private Transform targetTime;
	private Transform achievementsText;
	public List<Transform> achievements;
	private Transform replayButton;
	private Transform menuButton;
	private Transform nextButton;

	void Start () {
		finalTime = 0f;
		enable = true;

		backDrop = transform.Find("Back Drop");
		successOrFailMessage = transform.Find("Success or Fail Message");
		stevensPortrait = transform.Find ("Steven's Portrait");
		timeIcon = transform.Find("Time Icon");
		playerTime = transform.Find("PlayerTime");
		bestTime = transform.Find("Best Time");
		targetTime = transform.Find("targetTime");

		achievementsText = transform.Find("Achievements");
		achievements = new List<Transform>();

		replayButton = transform.Find("Replay Button");
		menuButton = transform.Find("Menu Button");
		nextButton = transform.Find("Next Button");

	}


	/*
	 * set the score and remaining time values to be used in the results screen
	 * 
	 * input: int s (score), float rt (remaining time)
	 * 
	 * output: void
	 * 		assigns the score and remaining time to use for final score calculations
	 */
	void setFinalTime(float ft){
		finalTime = ft;
	}


	void Update () {

		if (enable && !enabled) {
			enableChildren();
		}
		if (!enable && enabled) {
			disableChildren();
		}

		//if(enabled) {
		//	playerTime = Timer1.timerSprite(Timer1.formatTime(finalTime), playerTime.gameObject).transform;

		//}
	}

	void enableChildren() {
		achievementsText.gameObject.SetActive(true);
		backDrop.gameObject.SetActive(true);
		bestTime.gameObject.SetActive(true);
		menuButton.gameObject.SetActive(true);
		replayButton.gameObject.SetActive(true);
		nextButton.gameObject.SetActive(true);
		successOrFailMessage.gameObject.SetActive(true);
		timeIcon.gameObject.SetActive(true);
		playerTime.gameObject.SetActive(true);
		targetTime.gameObject.SetActive(true);
		stevensPortrait.gameObject.SetActive(true);

		enabled = true;
	}


	void disableChildren() {
		achievementsText.gameObject.SetActive(false);
		backDrop.gameObject.SetActive(false);
		bestTime.gameObject.SetActive(false);
		menuButton.gameObject.SetActive(false);
		replayButton.gameObject.SetActive(false);
		nextButton.gameObject.SetActive(false);
		successOrFailMessage.gameObject.SetActive(false);
		timeIcon.gameObject.SetActive(false);
		playerTime.gameObject.SetActive(false);
		targetTime.gameObject.SetActive(false);
		stevensPortrait.gameObject.SetActive(false);

		enabled = false;
	}
}
