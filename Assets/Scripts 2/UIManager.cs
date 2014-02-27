using UnityEngine;
using System.Collections;

public class UIManager : MonoBehaviour {

	//these are assigned in the Unity Editor
	public GameObject check;
	public GameObject cross;
	public GameObject great;
	public GameObject nope;
	public GameObject statusBar;

	GameObject[] allUI; //a convenience array to do actions to all UI elements

	//status bar gameObjects
	GameObject statusBarBG;
	GameObject statusBarText;
	float sbBGMovementAmount;
	float sbTextMovementAmount;
	Vector3 sbBGOffscreenPos;
	Vector3 sbTextOffscreenPos;

	//animation variables
	float fadeSpeed;
	bool  selected;
	float scaleSize;


	// Use this for initialization
	void Start () {
		allUI = new GameObject[4];

		allUI[0] = check;
		allUI[1] = cross;
		allUI[2] = great;
		allUI[3] = nope;

		statusBarBG   = GameObject.Find ("status bar background");
		statusBarText = GameObject.Find ("text");

		//TODO: you were in the middle of this
		//status bar original positions: bg = (0, -4.139404, 0), text = (0.5, 0.0682869, 0)
		sbBGOffscreenPos   = new Vector3(0f, -5.65f, 0f);
		sbTextOffscreenPos = new Vector3(0.5f, -0.11f, 0f);

		sbBGMovementAmount = 0f;
		sbTextMovementAmount = 0f;

		fadeSpeed = 0.2f; //smaller number -> slower speed
		selected  = false;
		scaleSize = 1.2f; //larger number -> larger size

		makeUIInvisible();
	}


	void makeUIInvisible(){
		foreach(GameObject go in allUI){

			//change the color of the objects to transparent
			Color c = go.renderer.material.color;
			go.renderer.material.color = new Color(c.r, c.g, c.b, 0f);
		}
	}


	IEnumerator rotate360degrees(GameObject go, float speed, int spins){
		
		float time = speed;
		
		//do the rotation
		while(time <= spins * 360){
			
			go.transform.Rotate(speed, 0f, 0f);
			time += speed;
			yield return new WaitForFixedUpdate();
		}
		
	}
	
	
	IEnumerator fadeIn(GameObject go, float fadeSpeed){
		
		// save the original color
		Color c = go.renderer.material.color;
		
		// make it invisible
		float a = 0f;
		go.renderer.material.color = new Color(c.r, c.g, c.b, a);
		
		// fade the sprite in
		while(a <= 1f){
			go.renderer.material.color = new Color(c.r, c.g, c.b, a);
			a += fadeSpeed;
			yield return new WaitForFixedUpdate();
			
		}
	}
	
	
	IEnumerator fadeOut(GameObject go, float fadeSpeed){

		//if the go is already invisible, then there's no reason to fadeOut
		//TODO: any way to solve this issue?
		if(go.renderer.material.color.a <= 0){
			print ("This is already invisible!");
			StopCoroutine("fadeOut"); //something analogous to 'return'
		}
		
		//save the original color
		Color c = go.renderer.material.color;
		
		//make it fully visible
		float a = 1f;
		go.renderer.material.color = new Color(c.r, c.g, c.b, a);

		//fade the sprite out
		while (a >= 0f){
			go.renderer.material.color = new Color(c.r, c.g, c.b, a);
			a -= fadeSpeed;
			yield return new WaitForFixedUpdate();
			
		}
	}


	//TODO: these rotation functions are now busted for some reason, an easier way would be to do animations?
	IEnumerator returnToZeroDegrees(GameObject go){
		float   angle  = 0f;            // just default values
		Vector3 axis   = Vector3.right; // these two vars will be replaced

		int iterations = 4; //higher number means slower animation

		go.transform.rotation.ToAngleAxis(out angle, out axis);
		print ("angle: " + angle);
		float step = angle/iterations;
		print ("step: " + step);


		while(angle > 0f){
			print ("angle: " + angle);
			go.transform.Rotate (-step, 0f, 0f);
			angle -= step;
			yield return new WaitForFixedUpdate();
		}

	}


	/*
	 * slideStatusBarUpFromBottom
	 * 		Moves the status bar from off screen up to the bottom of the screen
	 * 
	 * input: none - TODO: although... should it take in the status bar game object?
	 * output: none
	 */
	IEnumerable slideStatusBarUpFromBottom(){

		// when it's appearing, add values
		// TODO: when bar and text are disappearing, subtract

		// in the loop
		for(float journey = 0.0f; journey <= 1f; journey += 0.2f){
			//change the location of the bottom bar
			//change the location of the text

			yield return new WaitForFixedUpdate();
		}
			
	}


	/*
	 * makeUIAppearAtTopCenter
	 * 		Just as it says; the negative feedback at the top center of the screen appears.
	 * 		Future versions can have a gameObject passed into the funtion
	 * 
	 * input: 
	 * 		go - the GameObject to place in the top center of the screen
	 * 		positiveFeedback - true for positive, false for negative, makes the UI spin in place
	 * 
	 * output: none
	 */
	public void makeUIAppearAtTopCenter(GameObject go, bool positiveFeedback){
		StartCoroutine(makeUIAppearAtTopCenterCO(go, positiveFeedback));
	}
	IEnumerator makeUIAppearAtTopCenterCO(GameObject go, bool positiveFeedback){

		//TODO: in the future, fix this function, or replace it
		//if(positiveFeedback){
		//	StartCoroutine(rotate360degrees(go, 60f, 2));
		//}

		//make the sprite appear
		StartCoroutine(fadeIn (go, fadeSpeed));

		//wait a bit
		yield return new WaitForSeconds(1f);

		//fade the sprite out
		StartCoroutine(fadeOut(go, fadeSpeed));
	} 


	/*
	 * makeCheckmarkOrCrossAppear
	 * 		Makes the check or cross UI appear on top of a given position.
	 * 
	 * input:
	 * 		newPosition - the position where the cross or check are going to appear,
	 * 			ideally the position of the object that is being selected
	 * 		correctObject - true for correct, false for incorrect, selects which UI to
	 * 			display on the screen
	 * 
	 * output: none
	 */
	public void makeCheckmarkOrCrossAppear(Vector3 newPosition, bool correctObject){
		StartCoroutine(makeCheckmarkOrCrossAppearCO(newPosition, correctObject));
	}
	IEnumerator makeCheckmarkOrCrossAppearCO(Vector3 newPosition, bool correctObject){

		//select whether to use the X or the Check
		GameObject go;
		if(correctObject){
			go = check;
		}else{
			go = cross;
		}

		//set the location of the checkmark to the input location
		go.transform.position = newPosition;

		//rotate the checkmark by 90 degrees immediately
		//go.transform.rotation = Quaternion.AngleAxis(90f, Vector3.right);

		//make visible immediately
		Color c = go.renderer.material.color;
		go.renderer.material.color = new Color(c.r, c.g, c.b, 1f);

		//TODO: Recall, these functions are all fuckey so I'm commenting them out
		//rotate it back to 0 degrees
		//StartCoroutine(returnToZeroDegrees(go));

		//wait a little bit
		yield return new WaitForSeconds(1.5f);

		//fade out 
		StartCoroutine(fadeOut(go, fadeSpeed));
	}


	/*
	 * increaseOrDecreaseInSize
	 * 		Increases or decreases the size of a given gameobject. Uses the 
	 * 
	 * input:
	 * 		go - the gameObject to increase or decrease the size of
	 * 		isSelected - true if the object is selected by the eyeManager's cursor, false
	 * 			if not, chooses to increase or decrease the size of the object
	 */
	public void increaseOrDecreaseInSize(Transform go, bool grow){
		StartCoroutine(increaseOrDecreaseInSizeCO(go, grow));
	}
	IEnumerator increaseOrDecreaseInSizeCO(Transform go, bool grow){
		Vector3 originalSize = go.localScale;
		Vector3 changedSize  = originalSize;
		print("originalSize: " + originalSize);
		
		if(grow){
			changedSize *= scaleSize;
			print ("changedSize after growing: " + changedSize);
		}else{
			changedSize /= scaleSize;
			print ("changedSized after shrinking: " + changedSize);
		}
		
		//this is where the loop is supposed to go
		for(float journey = 0.0f; journey <= 1.0f; journey += 0.2f){
			go.transform.localScale = Vector3.Lerp(originalSize, changedSize, journey);
			yield return new WaitForFixedUpdate();
		}
	}

	
	void Update () {
		if(Input.GetKey(KeyCode.I)){
			//makeUIAppearAtTopCenter(great, true);
			makeCheckmarkOrCrossAppear(Vector3.zero, check);
		}
	}
}
