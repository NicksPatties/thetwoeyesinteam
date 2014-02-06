using UnityEngine;
using System.Collections;

public class CheatingKeys : MonoBehaviour {
	private bool blurred = true;
	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
		if(Input.GetKey(KeyCode.B)){
			GameObject mainCamera = GameObject.Find("Main Camera");
			blurred = !blurred;
			mainCamera.GetComponent<ChangableBlur>().enabled = blurred;
		}
	}
}
