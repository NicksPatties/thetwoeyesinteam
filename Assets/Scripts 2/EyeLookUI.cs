using UnityEngine;
using System.Collections;

public class EyeLookUI : MonoBehaviour {

	GameObject leftEye;
	GameObject rightEye;
	Vector2 eyePos;
	bool    isLeftEye;


	void Start () {
		eyePos = Vector2.zero;

		//get the position of the player's left and right eye
		if(this.name.Equals("left eye")){
			leftEye = GameObject.Find("Player/Left Eye");

			eyePos = leftEye.transform.position;
			isLeftEye = true;
		}else{
			rightEye = GameObject.Find ("Player/Right Eye");

			eyePos = rightEye.transform.position;
			isLeftEye = false;
		}
	}
	

	/*
	 * Map the eye position of the Player's eyes to the UI at the bottom of the screen
	 * 
	 * input: ep
	 * 		the position of the Player's eye game object
	 * 
	 * input: left
	 * 		chooses between the left eye or the right eye
	 * 
	 * output: Vector2
	 * 		the new position to apply to the UI eye object
	 */
	Vector2 mapEyePosToUIEyePos(Vector2 ep, bool left){
		return ep;
	}


	void Update () {
		if(isLeftEye){
			//eyePos = leftEye.transform.position;

			//TODO: map the position of the eyeball on the screen to the position in Steven's head
			//this.transform.position = mapEyePosToUIEyePos(eyePos, isLeftEye);

		}else{
			//eyePos = rightEye.transform.position;

			//TODO: map the position of the eyeball on the screen to the position in Steven's head


		}
	}
}
