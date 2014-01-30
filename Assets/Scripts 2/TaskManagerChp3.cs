using UnityEngine;
using System.Collections;

public class TaskManagerChp3 : MonoBehaviour {
	//TODO: Find a way to not have to create Task Managers for each chapter

	//each action is a 2-elements string array, first element is the target object, second is the name of action
	public string[][] actionList = new string[5][];
	//a reference for eyemanager to get the current action requirment, 
	public string[] curAction;
	//init action index as 0, later we will increase it in order to trigger new action requirment
	public int actionIndex = 0;

	private GameObject action01;
	private GameObject action02;

	GameObject actionPrompt;
	TimerScript actionTimer;
	
	void Start () {
		//init the actions in the level
		actionList[0] = new string[2]{"manrestroom","find"};
		actionList[1] = new string[2]{"pantsfly","find"};
		actionList[2] = new string[2]{"toilet","focus"};
		actionList[3] = new string[2]{"pantsfly", "find"};
		actionList[4] = new string[2]{"done", "you're"};
		curAction = actionList[actionIndex];

		actionPrompt = GameObject.Find("Action Prompt");
		actionPrompt.guiText.text = formatActionPromptText();

		action01 = GameObject.Find("Action01");
		action02 = GameObject.Find("Action02");
		action02.active = false;

		actionTimer = GameObject.Find("Timer").GetComponent<TimerScript>();

	}

	string formatActionPromptText(){
		//TODO: make this look prettier than how it is right now
		return curAction[1] + " " + curAction[0] + "!";
	}
	
	public void updateAction() {
		actionIndex++;
		if(actionIndex >= 4){
			//stop the timer
			print ("I should now stop the timer");
			actionTimer.timerHasStopped = true;

		}else{
			curAction = actionList[actionIndex];
			actionPrompt.guiText.text = formatActionPromptText();
			//the code the delay a code execution
			Invoke("changeEnvironment", 1); 
		}
	}

	private void changeEnvironment(){
		//GameObject action01 = GameObject.Find("Action01");
		action01.active = false;
		//GameObject action02 = GameObject.Find("Action02");
		action02.active = true;
	}
}
