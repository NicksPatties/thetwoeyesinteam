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

	private string[] targetObjectsForScan; //TODO: perhaps this should be an array of GameObjects instead?
	private Transform[] scannedObjects;

	private string[] targetObjectsForTrace;
	private Transform[] tracedObjects;
	private int tracedIndex;
	private bool invisible;

	public bool checkFindCompleted () {
		int targetObjectNum = targetObjects.Length;
		for (int i=0; i<targetObjectNum; i++){
			if (targetObjects[i] != "done"&&targetObjects[i] != null){
				Debug.Log("what is not done?: "+targetObjects[i]);
				return false;
			}
		}
		return true;
	}

	public bool checkScanCompleted () {
		for (int i=0; i<targetObjectsForScan.Length; i++){
			if (targetObjectsForScan[i] == "unvisited"&&targetObjects[i] != ""&&targetObjects[i] != null){
				Debug.Log("what is unvisited?: "+targetObjects[i]);
				return false;
			}
		}
		for (int j=0; j<scannedObjects.Length; j++){
			if (scannedObjects[j]){
				scannedObjects[j].GetComponent<SpriteRenderer>().color = Color.white;
			}
		}
		Debug.Log("all are visited.");
		targetObjectsForScan = null;
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
							Debug.Log("-------Find complete, next target is: "+targetObjects[0]+ " in mode: "+mode+"--------");
						}
					//if it's not correct
					}else if(targetObjects[i] == null){
							
					}else{
						obj.GetComponent<SpriteRenderer>().color = Color.red;
					}
				}
				return true;
			}
			if (mode == "scan"){
				if (targetObjectsForScan == null){
					targetObjectsForScan = new string[targetObjects.Length];
					for (int i = 0; i<targetObjects.Length; i++){
						targetObjectsForScan[i] = "unvisited";
						Debug.Log("-----------before scan starts, init all scan nodes as unvisited----------");
					}
				}
				if (scannedObjects == null){
					scannedObjects = new Transform[targetObjects.Length];
				}
				//if it is correct
				for (int i=0; i<targetObjects.Length; i++){	
					//we are scanning an unvisited node, so we set it green, and set it as visited
					if (targetObjects[i] == objectName && targetObjectsForScan[i] == "unvisited"){
						obj.GetComponent<SpriteRenderer>().color = Color.green;
						targetObjectsForScan[i] = "visited";
						scannedObjects[i] = obj;
						Debug.Log("-----------this is visiting: "+objectName+"----------");
						if (checkScanCompleted()){
							//disable rendering after scan complete
							for (int k = 0; k<targetObjects.Length; k++){
								string s = "ModelOldGuyNode"+k.ToString();
								if (GameObject.Find(s)){
									GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = false;
								}
								Debug.Log("---------Scan complete, so "+s+" is disabled----------");
							}
							//clean the targetObjectFor Scan array after scan complete
							targetObjectsForScan = null;
							GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
							targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
							mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
							GameObject.Find("Player").GetComponent<EyeManager>().mode = mode;
							Debug.Log("-------Scan complete, next target is: "+targetObjects[0]+ " in mode: "+mode+"--------");
							for (int j = 0; j<targetObjects.Length; j++){
								string s = "OutlineOldGuyNode"+j.ToString();
								if (GameObject.Find(s)){
									if (j <= tracedIndex){
										GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = true;
									}else{
										GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = false;
									}
								}
							}
						}
						break;
					}
					//we are scanning a visited node, so we keep it green.
					else if(targetObjects[i] == objectName && targetObjectsForScan[i] == "visited"){
						obj.GetComponent<SpriteRenderer>().color = Color.green;
						Debug.Log("-----------Right object, but it is visited already: "+objectName+"----------");
						break;
					}else if(targetObjects[i] == null){
							
					}
					//we are scanning something that is not inside the scanning shape, so we set it red
					else{
						Debug.Log("-----------WRONG OBJECT!!! this makes it red: "+objectName+"----------");
						obj.GetComponent<SpriteRenderer>().color = Color.red;
					}
				}
				return true;
			}
			if (mode == "trace"){
				if (targetObjectsForTrace == null){
					targetObjectsForTrace = new string[targetObjects.Length];
					for (int i = 0; i<targetObjects.Length; i++){
						targetObjectsForTrace[i] = "unvisited";
						Debug.Log("-----------before trace starts, init all trace nodes as unvisited----------");
					}
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
					if (checkTraceCompleted()){
						targetObjectsForTrace = null;
						GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
						targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
						mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
						GameObject.Find("Player").GetComponent<EyeManager>().mode = mode;
						Debug.Log("-------Trace complete, next target is: "+targetObjects[0]+ " in mode: "+mode+"--------");
					}
					if (invisible == false){
						for (int j = 0; j<targetObjects.Length; j++){
							string s = "OutlineOldGuyNode"+j.ToString();
							if (GameObject.Find(s)){
								if (j <= tracedIndex){
									GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = true;
								}else{
									GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = false;
								}
							}
						}
						invisible = true;
					}
				}else{
					for (int i=0; i<targetObjects.Length; i++){	
						if(targetObjects[i] == objectName && targetObjectsForTrace[i] == "visited"){
							break;
						}
						if (i==targetObjects.Length-1){
							Debug.Log("-----------this makes it red: "+objectName+"----------");
							obj.GetComponent<SpriteRenderer>().color = Color.red;
						}
					}
				}
				//we are tracing a visited node, so we keep it green.
//				for (int i=0; i<targetObjects.Length; i++){	
//					if(targetObjects[i] == objectName && targetObjectsForTrace[i] == "visited"){
//						break;
//					}else{
//						Debug.Log("-----------this makes it red: "+objectName+"----------");
//						obj.GetComponent<SpriteRenderer>().color = Color.red;
//					}
//				}
						//obj.GetComponent<SpriteRenderer>().color = Color.green;
						//Debug.Log("-----------this is visited: "+objectName+"----------");
//						if (checkTraceCompleted()){
//							targetObjectsForTrace = null;
//							GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
//							targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
//							mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
//							GameObject.Find("Player").GetComponent<EyeManager>().mode = mode;
//							Debug.Log("-------Trace complete, next target is: "+targetObjects[0]+ " in mode: "+mode+"--------");
//						}
//						//make next node visible, keep others invisible
//						if (invisible == false){
//							for (int j = 0; j<targetObjects.Length; j++){
//								string s = "OutlineOldGuyNode"+j.ToString();
//								if (GameObject.Find(s)){
//									if (j <= tracedIndex){
//										GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = true;
//									}else{
//										GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = false;
//									}
//								}
//							}
//							invisible = true;
//						}
//					}else if(targetObjects[i] == null){
//						
//					}
//					else{
//						Debug.Log("-----------this makes it red: "+objectName+"----------");
//						obj.GetComponent<SpriteRenderer>().color = Color.red;
//					}
//				}
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
