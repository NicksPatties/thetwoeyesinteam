using UnityEngine;
using System.Collections;
using System;

public class Timer : MonoBehaviour {

	public float t1;
	public float t2;
	public string s;

	public GameObject go;

	// Use this for initialization
	void Start () {
		//go = new GameObject();
	}


	public string formatTime (float timer) {
		TimeSpan timeSpan = TimeSpan.FromSeconds(timer);
		string timerString;
		if(timeSpan.Hours > 0) {
			timerString = string.Format("{0:D2}:{1:D2}:{2:D2}:{3:D2}", timeSpan.Hours, timeSpan.Minutes, timeSpan.Seconds, timeSpan.Milliseconds);
		}
		else if (timeSpan.Minutes > 0) {
			timerString = string.Format("{1:D2}:{2:D2}:{3:D2}", timeSpan.Minutes, timeSpan.Seconds, timeSpan.Milliseconds);
		}
		else {
			timerString = string.Format("{0:D2}:{1:D2}", timeSpan.Seconds, timeSpan.Milliseconds);
		}
		return timerString;
	}

	public 
	
	// Update is called once per frame
	void Update () {
		s = formatTime(t1);

	}
}
