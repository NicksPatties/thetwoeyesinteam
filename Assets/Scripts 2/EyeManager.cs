using UnityEngine;
using System.Collections;

public class EyeManager : MonoBehaviour {

	private Transform leftEye;
	private Transform rightEye;

	public float LEX;
	public float LEY;
	public float REX;
	public float REY;

	// Use this for initialization
	void Start () {
		leftEye = transform.Find("Left Eye");
		rightEye = transform.Find("Right Eye");
	}
	
	// Update is called once per frame
	void Update () {
		LEX = leftEye.transform.position.x;
		LEY = leftEye.transform.position.y;
		REX = rightEye.transform.position.x;
		REY = rightEye.transform.position.y;
	}
}
