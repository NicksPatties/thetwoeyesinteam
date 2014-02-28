using UnityEngine;
using System.Collections;
using System;

public class Timer : MonoBehaviour {

	public float t1;
	public string string1;

	public GameObject minuteTemplate;

	public Sprite[] nums;

	private string[] digits;

	public SpriteRenderer millisec1;
	public SpriteRenderer millisec2;
	public SpriteRenderer sec1;
	public SpriteRenderer sec2;
	public SpriteRenderer min1;
	public SpriteRenderer min2;
	public SpriteRenderer colon1;

	public GameObject temp1;

	// Use this for initialization
	void Start () {
		//go = new GameObject();
		string1 = "00:00:000";
		temp1 = null;
	}


	public string formatTime (float timer) {
		TimeSpan timeSpan = TimeSpan.FromSeconds(timer);
		string timerString;
		/*if(timeSpan.Hours > 0) {
			timerString = string.Format("{0:D2}:{1:D2}:{2:D2}:{3:D2}", timeSpan.Hours, timeSpan.Minutes, timeSpan.Seconds, timeSpan.Milliseconds);
		}
		else if (timeSpan.Minutes > 0) {*/
			timerString = string.Format("{0:D2}:{1:D2}:{2:D2}", timeSpan.Minutes, timeSpan.Seconds, timeSpan.Milliseconds);
		//}
		//else {
		//	timerString = string.Format("{0:D2}:{1:D2}", timeSpan.Seconds, timeSpan.Milliseconds);
		//}
		return timerString;
	}

	public GameObject timerSprite (string s, GameObject ts) {
		// if a new sprite set is needed
		if (ts == null) {
			ts = (GameObject) Instantiate(minuteTemplate);
		}

		// Grab the individual sprites of the digits
		millisec1 = (SpriteRenderer) ts.transform.Find("MilliSeconds1").GetComponent(typeof (SpriteRenderer));
		millisec2 = (SpriteRenderer) ts.transform.Find("MilliSeconds2").GetComponent(typeof (SpriteRenderer));
		sec1 = (SpriteRenderer) ts.transform.Find("Seconds1").GetComponent(typeof (SpriteRenderer));
		sec2 = (SpriteRenderer) ts.transform.Find("Seconds2").GetComponent(typeof (SpriteRenderer));
		colon1 = (SpriteRenderer) ts.transform.Find("MinutesColon").GetComponent(typeof (SpriteRenderer));
		min1 = (SpriteRenderer) ts.transform.Find("Minutes1").GetComponent(typeof (SpriteRenderer));
		min2 = (SpriteRenderer) ts.transform.Find("Minutes2").GetComponent(typeof (SpriteRenderer));

		// Set the second and millisecond digits with the new number sprites
		millisec1.sprite = getDigitSprite(s[7]);
		millisec2.sprite = getDigitSprite(s[6]);
		sec1.sprite = getDigitSprite(s[4]);
		sec2.sprite = getDigitSprite(s[3]);

		// If the minute values are non-zero, enable and set, otherwise hide them
		if(s[0] != '0') {
			min2.gameObject.SetActive(true);
			min2.sprite = getDigitSprite(s[0]);
		}
		else {
			min2.gameObject.SetActive(false);
		}

		if(s[1] != '0') {
			colon1.gameObject.SetActive(true);
			min1.gameObject.SetActive(true);
			min1.sprite = getDigitSprite(s[1]);
		}
		else if(s[0] == '0'){
			colon1.gameObject.SetActive(false);
			min1.gameObject.SetActive(false);
			min2.gameObject.SetActive(false);
		}



		//if (s[0] != '0' || s[1] != '0')
			// show colon
		// s[1] != '0'
			// show colon and s[1]
		// s[0] != '0'
			// show everything

		return ts;
	}

	Sprite getDigitSprite(char c) {
		switch(c) {
			case '0':
				return nums[0];
			case '1':
				return nums[1];
			case '2':
				return nums[2];
			case '3':
				return nums[3];
			case '4':
				return nums[4];
			case '5':
				return nums[5];
			case '6':
				return nums[6];
			case '7':
				return nums[7];
			case '8':
				return nums[8];
			case '9':
				return nums[9];
			case ':':
				return nums[10];
			default: 
				return nums[0];
		}
	}

	
	// Update is called once per frame
	void Update () {
		string1 = formatTime(t1);
		temp1 = timerSprite(string1, temp1);

	}
}
