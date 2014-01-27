using UnityEngine;
using System.Collections;

public class TaskManager : MonoBehaviour {
	public string[][] actionList = new string[3][];
	public string[] curAction;
	public int actionIndex = 0;

	void Start () {
		actionList[0] = new string[2]{"lcs","find"};
		actionList[1] = new string[2]{"rgst","find"};
		actionList[2] = new string[2]{"handle","find"};
		//string[] fingers = new string[5]{"index finger","middle finger","ring finger","pinky finger","thumb"};
		//actionList[3] = new string[2]{fingers,"find"};
		//actionList[4] = new string[2]{"index finger","focus"};
		int index = 0;
		curAction = actionList[actionIndex];
	}
	
	// Update is called once per frame
	void Update () {

	}

	public void updateAction() {
		actionIndex++;
		curAction = actionList[actionIndex];
	}
}
