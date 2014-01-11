using UnityEngine;
using System.Collections;

public class EyeManager : MonoBehaviour {

	private Transform leftEye;
	private Transform rightEye;
	

	// Use this for initialization
	void Start () {
		leftEye = transform.Find("Left Eye");
		rightEye = transform.Find("Right Eye");
	}
	
	// Update is called once per frame
	void Update () {
	
	}
}
