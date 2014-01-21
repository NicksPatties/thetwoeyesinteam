using UnityEngine;
using System.Collections;

public class TimerScript : MonoBehaviour {

	float time;
	float freakOutThreshold;
	public bool testingEnabled;
 

	void Start () {
		time = 0.0f;
		freakOutThreshold = 3.0f;
		testingEnabled = false;
	}


	/**
	 * Check if time has expired
	 * 
	 * input: none
	 * 
	 * output:
	 * 		true if time is zero, false if there is time remaining
	 */ 
	public bool timeHasExpired(){
		if (time <= 0f)
			return true;
		return false;
	}


	void formatTimer(){

		if (time <= 0f) time = 0f;

		// turn the float into a string that looks like xx:xx
		string timerString = string.Format("{0:00.00}", time);
		timerString = timerString.Replace('.', ':');
		gameObject.guiText.text = timerString;

	}


	/**
	 * Adds additional time to the timer
	 * 
	 * input: float t
	 * 		the time to add to the current timer
	 * 
	 * output: void
	 * 		changes the timer to the new value
	 * 
	 */ 
	public void addTime(float t){
		time = time + t;

		formatTimer();
	}


	/**
	 * Convenience method to set the timer to a particular value
	 * 
	 * input: float t
	 * 		the timer's new time
	 * 
	 * output: void
	 * 		changes the timer to the new value
	 */ 
	public void setTimer(float t){
		time = t;

		formatTimer ();
	}


	void flashTimer(){
		gameObject.guiText.color = Color.red;
	}


	void testTimer(){

		// test adding time
		setTimer(5f);
	}


	void Update () {
		if(Input.GetKeyDown(KeyCode.Space) && testingEnabled)
			testTimer ();

		// if time has not been expired
		if(!timeHasExpired()){

			time = time - Time.deltaTime;

			// if the player is running out of time
			if(time < freakOutThreshold){

				//flash the timer red
				flashTimer ();
			}

			formatTimer();

		}else{

			//TODO: play a "you lose sound only once"
			/*if(!audio.isPlaying){
				audio.Play();
			}*/
		}
	}
}
