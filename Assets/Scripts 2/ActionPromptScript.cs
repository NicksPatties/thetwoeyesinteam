using UnityEngine;
using System.Collections;

public class ActionPromptScript : MonoBehaviour {

	bool isVisible;
	public bool testingEnabled;

	
	void Start () {
		isVisible = true;
		testingEnabled = false;
	}


	// changes the text in the action prompt to newString
	public void changeActionPrompt(string newString){
		guiText.text = newString;
	}


	// makes the text either visible or invisible
	public void changeVisibility(){

		if(isVisible){
			guiText.material.color = Color.clear;
			isVisible = false;
		}else{
			guiText.material.color = Color.white;
			isVisible = true;
		}
	}


	void testActionPrompt(){
		//check changeActionPrompt
		changeActionPrompt("I'm now a new action!");

		//change the visibility
		changeVisibility();
	}


	void Update () {
		if (Input.GetKey(KeyCode.Space) && testingEnabled)
			testActionPrompt();
	}
}
