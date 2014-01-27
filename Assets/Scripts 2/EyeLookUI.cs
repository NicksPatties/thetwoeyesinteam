using UnityEngine;
using System.Collections;

public class EyeLookUI : MonoBehaviour {

	GameObject leftEye;
	GameObject rightEye;
	Vector2 eyePos;
	bool    isLeftEye;

	//boundaries of UI eye placement
	//"right" and "left" is relative to the player's screen
	public float RightEyeMaxX;
	public float RightEyeMinX;
	public float RightEyeMaxY;
	public float RightEyeMinY;

	public float LeftEyeMaxX;
	public float LeftEyeMinX;
	public float LeftEyeMaxY;
	public float LeftEyeMinY;

	void Start () {
		eyePos = Vector2.zero;

		//get the position of the player's left and right eye
		leftEye = GameObject.Find("Player/Left Eye");
		rightEye = GameObject.Find ("Player/Right Eye");

		if(this.name.Equals("left eye")){
			eyePos = leftEye.transform.position;
			isLeftEye = true;
		}else{
			eyePos = rightEye.transform.position;
			isLeftEye = false;
		}
		
		RightEyeMaxX = 0.86f;
		RightEyeMinX = 1.08f;
		RightEyeMaxY = -4.48f;
		RightEyeMinY = -4.67f;

		LeftEyeMaxX = -0.11f;
		LeftEyeMinX = 0.14f;
		LeftEyeMaxY = -4.5f;
		LeftEyeMinY = -4.61f;
	}
	

	/*
	 * Map the eye position of the Player's eyes to the UI at the bottom of the screen
	 * 
	 * input: eyePosition
	 * 		the position of the Player's eye game object
	 * 
	 * input: left
	 * 		chooses between the left eye or the right eye
	 * 
	 * output: Vector2
	 * 		the new position to apply to the UI eye object
	 */
	Vector2 mapEyePosToUIEyePos(Vector2 eyePosition, bool left){

		Vector2 UIEyePosition = Vector2.zero;

		// the maxes and mins for the player controlled eyeballs
		float playerEyeMaxX = 26f;
		float playerEyeMinX = 9.5f;
		float playerEyeMaxY = -6.67f;
		float playerEyeMinY = 1.3f;

		if(left){
			//find the mapping of X
			float playerEyeX = leftEye.transform.position.x;
			UIEyePosition.x = (playerEyeX - playerEyeMaxX)/(playerEyeMinX - playerEyeMaxX)
				* (LeftEyeMaxX - LeftEyeMinX) + LeftEyeMinX;
			
			//do the same thing again for Y
			float playerEyeY = leftEye.transform.position.y;
			UIEyePosition.y = (playerEyeY - playerEyeMaxY)/(playerEyeMinY - playerEyeMaxY)
				* (LeftEyeMaxY - LeftEyeMinY) + LeftEyeMinY;
		}else{
			//find the mapping of X
			float playerEyeX = rightEye.transform.position.x;
			UIEyePosition.x = (playerEyeX - playerEyeMaxX)/(playerEyeMinX - playerEyeMaxX)
				* (RightEyeMaxX - RightEyeMinX) + RightEyeMinX;
			
			//do the same thing again for Y
			float playerEyeY = rightEye.transform.position.y;
			UIEyePosition.y = (playerEyeY - playerEyeMaxY)/(playerEyeMinY - playerEyeMaxY)
				* (RightEyeMaxY - RightEyeMinY) + RightEyeMinY;
		}
		return UIEyePosition;
	}


	void Update () {
		if(isLeftEye){
			this.transform.position = mapEyePosToUIEyePos(eyePos, isLeftEye);

		}else{
			this.transform.position = mapEyePosToUIEyePos(eyePos, isLeftEye);
		}
	}
}
