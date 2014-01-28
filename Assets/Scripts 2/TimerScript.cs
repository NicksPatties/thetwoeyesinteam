using UnityEngine;
using System.Collections;

public class TimerScript : MonoBehaviour {

	float time;
	bool timerHasStopped;
 

	void Start () {
		time = 0.0f;
		timerHasStopped = false;
		formatTimer();
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


	void Update () {

		//switch timer on and off for testing purposes
		if(Input.GetKeyDown(KeyCode.Space)){
			if(timerHasStopped)
				timerHasStopped = false;
			else
				timerHasStopped = true;
		}


		// if time has not been expired
		if(!timerHasStopped){

			time = time + Time.deltaTime;

			formatTimer();

		}
	}
}
