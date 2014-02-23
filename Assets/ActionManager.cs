using UnityEngine;
using System.Collections;

public class ActionManager : MonoBehaviour {

	// Use this for initialization
	void Start () {

	}

	private Transform curObj;
	private Transform lastObj;
	private string[] targetObjects;
	private string mode = "";
	//for paint //TODO: change the name of "paint" to "scan," because it makes more sense
	private string[] targetObjectsForPaint; //TODO: perhaps this should be an array of GameObjects instead?
	private Transform[] paintedObjects;
	
	//for trace
	private string[] targetObjectsForTrace;
	private Transform[] tracedObjects;
	private int tracedIndex;
	private bool invisible;

//	public  float focusTime; //number of seconds the cursor been on an object
//	public  float waitOnFocusTime;  //the time in seconds the cursor must be on an object before moving to the next action

	public bool checkFindCompleted () {
		//string[] targetObjs = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
		int targetObjectNum = targetObjects.Length;
		for (int i=0; i<targetObjectNum; i++){
			if (targetObjects[i] != "done"&&targetObjects[i] != null){
				Debug.Log("what is not done?: "+targetObjects[i]);
				return false;
			}
		}
		return true;
	}

	public bool checkPaintCompleted () {
		for (int i=0; i<targetObjectsForPaint.Length; i++){
			if (targetObjectsForPaint[i] == "unvisited"&&targetObjects[i] != ""&&targetObjects[i] != null){
				Debug.Log("what is unvisited?: "+targetObjects[i]);
				return false;
			}
		}
		for (int j=0; j<paintedObjects.Length; j++){
			if (paintedObjects[j]){
				paintedObjects[j].GetComponent<SpriteRenderer>().color = Color.white;
			}
		}
		Debug.Log("all are visited.");
		targetObjectsForPaint = null;
		return true;
	}

	public bool checkTraceCompleted () {
		for (int i=0; i<targetObjectsForTrace.Length; i++){
			if (targetObjectsForTrace[i] == "unvisited"&&targetObjects[i] != ""&&targetObjects[i] != null){
				Debug.Log("what is unvisited?: "+targetObjects[i]);
				return false;
			}
		}
		for (int j=0; j<tracedObjects.Length; j++){
			if (tracedObjects[j].transform){
				tracedObjects[j].transform.GetComponent<SpriteRenderer>().color = Color.white;
			}
		}
		Debug.Log("all are visited.");
		targetObjectsForTrace = null;
		return true;
	}

	public bool checkCorrectness(string objectName, Transform obj){
		if (objectName != null){
			targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
//			Debug.Log("curaction[0] is: "+targetObjects[0]);

			if (mode == "find"){
				int targetObjectNum = targetObjects.Length;
				for (int i=0; i<targetObjectNum; i++){
							
					//if it is correct
					if (targetObjects[i] == objectName){
						obj.GetComponent<SpriteRenderer>().color = Color.green;
						targetObjects[i] = "done";
						if (checkFindCompleted()){;
							GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
							targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
							mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
							GameObject.Find("Player").GetComponent<EyeManager>().mode = mode;
							Debug.Log("updated curaction[0] is: "+targetObjects[0]);
						}
					//if it's not correct
					}else if(targetObjects[i] == null){
							
					}else{
						obj.GetComponent<SpriteRenderer>().color = Color.red;
					}
				}
				return true;
			}
			if (mode == "paint"){
				if (targetObjectsForPaint == null){
					targetObjectsForPaint = new string[targetObjects.Length];
					for (int i = 0; i<targetObjects.Length; i++){
						targetObjectsForPaint[i] = "unvisited";
						Debug.Log("-----------init all paint nodes as unvisited----------");
					}
				}
				if (paintedObjects == null){
					paintedObjects = new Transform[targetObjects.Length];
				}
				//if it is correct
				for (int i=0; i<targetObjects.Length; i++){	
					//we are painting an unvisited node, so we set it green, and set it as visited
					if (targetObjects[i] == objectName && targetObjectsForPaint[i] == "unvisited"){
						obj.GetComponent<SpriteRenderer>().color = Color.green;
						targetObjectsForPaint[i] = "visited";
						paintedObjects[i] = obj;
						Debug.Log("focusTimefocusTime-----------this is visiting: "+objectName+"----------");
						break;
					}
					//we are painting a visited node, so we keep it green.
					else if(targetObjects[i] == objectName && targetObjectsForPaint[i] == "visited"){
						obj.GetComponent<SpriteRenderer>().color = Color.green;
						Debug.Log("-----------this is visited: "+objectName+"----------");
						break;
					}else if(targetObjects[i] == null){
							
					}
					//we are painting something that is not inside the painting shape, so we set it red
					else{
						Debug.Log("-----------this makes it red: "+objectName+"----------");
						obj.GetComponent<SpriteRenderer>().color = Color.red;
					}
				}
					
				if (checkPaintCompleted()){
					targetObjectsForPaint = null;
					GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
					targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
					mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
					GameObject.Find("Player").GetComponent<EyeManager>().mode = mode;
					Debug.Log("updated current mode is: "+mode);
					Debug.Log("updated curaction[0] is: "+targetObjects[0]);
				}
				return true;
			}
			if (mode == "trace"){
				if (targetObjectsForTrace == null){
					targetObjectsForTrace = new string[targetObjects.Length];
					for (int i = 0; i<targetObjects.Length; i++){
						targetObjectsForTrace[i] = "unvisited";
						Debug.Log("-----------init all trace nodes as unvisited----------");
					}
				}
				if (invisible == false){
					for (int i = tracedIndex; i<targetObjects.Length; i++){
						string s = "OutlineOldGuyNode"+i.ToString();
						if (GameObject.Find(s)){
							GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = false;
						}
						Debug.Log("-----------"+s+" is disabled----------");
					}
					for (int i = 0; i<= tracedIndex; i++){
						string s1 = "OutlineOldGuyNode"+i.ToString();
						if (GameObject.Find(s1)){
							GameObject.Find(s1).GetComponent<SpriteRenderer>().enabled = true;
						}
						Debug.Log("-----------"+s1+" is enabled----------");
					}
					invisible = true;
				}
				if (tracedObjects == null){
					tracedObjects = new Transform[targetObjects.Length];
				}
				if (targetObjects[tracedIndex] == objectName && targetObjectsForTrace[tracedIndex] == "unvisited"){
					obj.GetComponent<SpriteRenderer>().color = Color.green;
					targetObjectsForTrace[tracedIndex] = "visited";
					tracedObjects[tracedIndex] = obj;
					tracedIndex++;
					Debug.Log("-----------this is visiting: "+objectName+"----------");
					invisible = false;
				}
				//we are tracing a visited node, so we keep it green.
				for (int i=0; i<targetObjects.Length; i++){	
					if(targetObjects[i] == objectName && targetObjectsForTrace[i] == "visited"){
						obj.GetComponent<SpriteRenderer>().color = Color.green;
						Debug.Log("-----------this is visited: "+objectName+"----------");
					}else if(targetObjects[i] == null){
						
					}
					else{
						Debug.Log("-----------this makes it red: "+objectName+"----------");
						obj.GetComponent<SpriteRenderer>().color = Color.red;
					}
				}
				if (invisible == false){
					for (int i = tracedIndex; i<targetObjects.Length; i++){
						string s = "OutlineOldGuyNode"+i.ToString();
						if (GameObject.Find(s)){
							GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = false;
						}
						Debug.Log("-----------"+s+" is disabled----------");
					}
					for (int i = 0; i<= tracedIndex; i++){
						string s1 = "OutlineOldGuyNode"+i.ToString();
						if (GameObject.Find(s1)){
							GameObject.Find(s1).GetComponent<SpriteRenderer>().enabled = true;
						}
						Debug.Log("-----------"+s1+" is enabled----------");
					}
					invisible = true;
				}
				
				if (checkTraceCompleted()){
					targetObjectsForTrace = null;
					GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
					targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
					mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
					GameObject.Find("Player").GetComponent<EyeManager>().mode = mode;
					Debug.Log("updated curaction[0] is: "+targetObjects[0]);
				}
				return true;
			}
		}	
		return false;
	}

	// Update is called once per frame
	void Update () {
		if (mode == "")	{
			mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
			Debug.Log("current mode is: "+mode);
		}
	}
}
