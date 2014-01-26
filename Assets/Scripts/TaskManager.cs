using UnityEngine;
using System.Collections;

public class TaskManager : MonoBehaviour {
	public string[][] actionList = new string[2][];
	public string[] curAction;


	void Start () {
		actionList[0] = new string[2]{"lcs","find"};
		actionList[1] = new string[2]{"rgst","find"};
		int index = 0;
		curAction = actionList[index];
	}
	
	// Update is called once per frame
	void Update () {
	
	}
}
