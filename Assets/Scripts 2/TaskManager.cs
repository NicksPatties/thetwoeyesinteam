using UnityEngine;
using System.Collections;

public class TaskManager : MonoBehaviour {
	//we currently have 3 actions for this chapter. each action is a 2-elements string array, first element is the target object, second is the name of action
	public string[][] actionList = new string[3][];
	//a reference for eyemanager to get the current action requirment, 
	public string[] curAction;
	//init action index as 0, later we will increase it in order to trigger new action requirment
	public int actionIndex = 0;

	void Start () {
		//init these 3 actions at the very beginning
		actionList[0] = new string[2]{"lcs","find"};
		actionList[1] = new string[2]{"rgst","find"};
		actionList[2] = new string[2]{"handle","find"};
		//string[] fingers = new string[5]{"index finger","middle finger","ring finger","pinky finger","thumb"};
		//actionList[3] = new string[2]{fingers,"find"};
		//actionList[4] = new string[2]{"index finger","focus"};
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
