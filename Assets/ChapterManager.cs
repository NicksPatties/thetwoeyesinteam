﻿using UnityEngine;
using System.Collections;
using SimpleJSON;

public class ChapterManager : MonoBehaviour {

	private ActionList[] alist = new ActionList[9];
	//private ActionList al;
	//a reference for eyemanager to get the current action requirment, 
	public ActionList curAction;
	//init action index as 0, later we will increase it in order to trigger new action requirment
	public int actionIndex = 0;

	private GameObject action01;
	private GameObject action02;
	
	GameObject actionPrompt;
	TimerScript actionTimer;

	public WWW www;
	private int level = 4;//change this number to the target level for loading JSON data
	private bool loaded = false;

	// Use this for initialization
	void Start () {
		//load JSON file from a relative path
		www = new WWW("file://"+Application.dataPath+"/ChaptersData/chapter"+level+".json");

		/*we haven't finish loading the JSON data here, so there is no data to load for UI text. I remove them to Updata()*/
		//TODO find out a mechanics to listen to a event like onComplete
		//actionPrompt = GameObject.Find("Action Prompt");
		//actionPrompt.guiText.text = formatActionPromptText();

		if (level == 3){
			action01 = GameObject.Find("Action01");
			action02 = GameObject.Find("Action02");
			action02.SetActive(false);
		}
		
		actionTimer = GameObject.Find("Timer").GetComponent<TimerScript>();
	}

	string formatActionPromptText(){
		//TODO: make this look prettier than how it is right now
		//return curAction[1] + " " + curAction[0] + "!";
		return curAction.actionName + " " + curAction.targetObjects[0] + "!";
	}

	public void updateAction() {
		actionIndex++;
		if(actionIndex >= 4){
			//stop the timer
			print ("I should now stop the timer");
			actionTimer.timerHasStopped = true;
			
		}else{
			curAction = alist[actionIndex];
			actionPrompt.guiText.text = formatActionPromptText();
			//the code that delay a code execution
			Invoke("changeEnvironment", 1); 
		}
	}
	
	// Update is called once per frame
	void Update () {
		if (loaded == false){
			if (www.isDone)//Once the JSON data is loaded, run this
				{
					var jsonData = JSON.Parse(www.text);
					for(int i = 0; i<jsonData["actions"].Count; i++){
						ActionList al = new ActionList();
						string[] tObjects = new string[jsonData["actions"][i]["objects"].Count+1];
						for(int j = 0; j<jsonData["actions"][i]["objects"].Count; j++){
							tObjects[j] = jsonData["actions"][i]["objects"][j]["name"];
//							Debug.Log("-------action "+i+" is loaded for object: "+j+"-------: "+tObjects[j]);
						}
						al.targetObjects = tObjects;
						al.targetObjects[al.targetObjects.Length-1] = null;
						al.actionName = jsonData["actions"][i]["type"];
						alist[i] = al;
//						Debug.Log("-------action is loaded for: "+i+"-------: "+alist[i].targetObjects[0]);
					}
					loaded = true;
					curAction = alist[actionIndex];
//					Debug.Log("-------current target is: "+curAction.targetObjects.Length);
//					Debug.Log("-------current target is: "+curAction.targetObjects[0]);
				}
		}

		//Here is the action prompt:) It has to be called after the JSON data is loaded
		if (loaded == true){
			actionPrompt = GameObject.Find("Action Prompt");
			actionPrompt.guiText.text = formatActionPromptText();
		}

		//Here is the blur effect code. multiple conditions for different distance domain
		GameObject player = GameObject.Find("Player");
		float d = player.GetComponent<EyeManager>().lrDistance;
		//Debug.Log("-------current left right distance is-------"+d);
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
		if (level == 3){
			action01.SetActive(false);
			action02.SetActive(true);
		}
	}
}
