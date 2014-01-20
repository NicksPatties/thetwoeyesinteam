using UnityEngine;
using System.Collections;

public class BottomStatusBarScript : MonoBehaviour {

	bool visible;
	Vector3 onscreenPosition;
	Vector3 offscreenPosition;
	public bool testingEnabled;


	// Use this for initialization
	void Start () {
		visible = true;
		onscreenPosition = new Vector3(0f, 0f, 0f);
		offscreenPosition = new Vector3(0f, -2.5f, 0f);
		testingEnabled = false;

		transform.position = onscreenPosition;
	}

	void changeVisibility(){
		if (visible){
			visible = false;
		}else{
			visible = true;
		}
	}


	// Update is called once per frame
	void Update () {
		if(Input.GetKeyDown(KeyCode.Space) && testingEnabled){
			changeVisibility();
		}

		//linearly interpolate between current position and newposition
		if(visible){
			transform.position = Vector3.Lerp(transform.position, onscreenPosition, 5f * Time.deltaTime);
		}else{
			transform.position = Vector3.Lerp(transform.position, offscreenPosition, 5f * Time.deltaTime);
		}
	}
}
