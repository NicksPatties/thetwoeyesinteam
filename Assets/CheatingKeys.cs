using UnityEngine;
using System.Collections;

public class CheatingKeys : MonoBehaviour {
	private bool blurred = true;
	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
		if(Input.GetKey(KeyCode.B)){//cheating mode for blur effect
			GameObject mainCamera = GameObject.Find("Main Camera");
			blurred = !blurred;
			mainCamera.GetComponent<BlurOnCamera>().enabled = blurred;
		}
		if(Input.GetKey(KeyCode.C)){//cheating mode for eyeball following
			GameObject rightEye = GameObject.Find("Right Eye");
			GameObject leftEye = GameObject.Find("Left Eye");
			rightEye.transform.position = Vector3.Lerp(rightEye.transform.position, leftEye.transform.position, 1);
//			rightEye.transform = leftEye.transform;
		}
	}
}
