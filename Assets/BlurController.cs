using UnityEngine;
using System.Collections;

public class BlurController : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
		changeIteration();
		//changeSpread();
	}

	void changeSpread() {
		//Here is the blur effect code. multiple conditions for different distance domain
		GameObject player = GameObject.Find("Player");
		float d = player.GetComponent<EyeManager>().lrDistance;
		//Debug.Log("-------current left right distance is-------"+d);
		GameObject mainCamera = GameObject.Find("Blur Camera");

		if (d>=0.0f&&d<=1.0f){
			mainCamera.GetComponent<BlurOnCamera>().blurSpread = 0.0f;
			mainCamera.GetComponent<BlurOnCamera>().enabled = false;
		}else{
			mainCamera.GetComponent<BlurOnCamera>().enabled = true;
		}
		if (d>1.0f){
			mainCamera.GetComponent<BlurOnCamera>().blurSpread = (float)(0.99*(d)/16);
		}
	}

	void changeIteration() {
		//Here is the blur effect code. multiple conditions for different distance domain
		GameObject player = GameObject.Find("Player");
		float d = player.GetComponent<EyeManager>().lrDistance;
		//Debug.Log("-------current left right distance is-------"+d);
		GameObject mainCamera = GameObject.Find("Blur Camera");
		if (d>=0.0f&&d<=1.5f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 0;
			mainCamera.GetComponent<BlurOnCamera>().enabled = false;
		}else{
			mainCamera.GetComponent<BlurOnCamera>().enabled = true;
		}
		if (d>1.5f&&d<=3.0f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 1;
		}else if (d>3.0f&&d<=4.5f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 2;
		}else if (d>1.5f&&d<=3.0f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 3;
		}else if (d>3.0f&&d<=4.5f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 3;
		}else if (d>4.5f&&d<=6.0f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 4;
		}else if (d>6.0f&&d<=7.5f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 4;
		}else if (d>7.5f&&d<=9.0f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 5;
		}else if (d>9.0f&&d<=10.5f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 5;
		}else if (d>10.5f&&d<=12.0f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 5;
		}else if (d>12.0f&&d<=13.5f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 6;
		}else if (d>13.5f&&d<=15.0f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 6;
		}else if (d>15.0f&&d<=16.5f){
			mainCamera.GetComponent<BlurOnCamera>().iterations = 6;
		}else{
			mainCamera.GetComponent<BlurOnCamera>().iterations = 7;
		}
	}
}
