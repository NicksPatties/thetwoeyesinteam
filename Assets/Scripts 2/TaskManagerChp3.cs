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
		action02.SetActive(false);

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

	void Update (){
		GameObject player = GameObject.Find("Player");
		float d = player.GetComponent<EyeManager>().lrDistance;
		Debug.Log("-------current left right distance is-------"+d);
		GameObject mainCamera = GameObject.Find("Main Camera");
		if (d>=0.0f&&d<=1.5f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 0;
			mainCamera.GetComponent<ChangableBlur>().enabled = false;
		}else{
			mainCamera.GetComponent<ChangableBlur>().enabled = true;
		}
		if (d>1.5f&&d<=3.0f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 1;
		}else if (d>3.0f&&d<=4.5f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 2;
		}else if (d>1.5f&&d<=3.0f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 3;
		}else if (d>3.0f&&d<=4.5f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 3;
		}else if (d>4.5f&&d<=6.0f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 4;
		}else if (d>6.0f&&d<=7.5f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 4;
		}else if (d>7.5f&&d<=9.0f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 5;
		}else if (d>9.0f&&d<=10.5f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 5;
		}else if (d>10.5f&&d<=12.0f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 5;
		}else if (d>12.0f&&d<=13.5f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 6;
		}else if (d>13.5f&&d<=15.0f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 6;
		}else if (d>15.0f&&d<=16.5f){
			mainCamera.GetComponent<ChangableBlur>().iterations = 6;
		}else{
			mainCamera.GetComponent<ChangableBlur>().iterations = 7;
		}
	}

	private void changeEnvironment(){
		action01.SetActive(false);
		action02.SetActive(true);
	}
}
