using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class EyeManager : MonoBehaviour {

	#region Player Variables
	private Transform leftEye;
	private Transform rightEye;
	private Transform radiusMarker;   //helps calculate the radius of the player's eyeball
	private Transform lastObj;
	private Transform curObj;
	private float eyeballRadius;
	public  float lrDistance;
	private Vector2 midPoint;
	private bool canTarget;
	private bool isOverObject;
	#endregion

	public string mode; //TODO: should THIS be the enum?

	#region Cursor Variables
	private Transform cursorPoint;
	private Transform cursorCollider; //used to check if eyes are focused on something
	public  Vector2 cursorVelocity;
	public  float cursorToTargetDistance;
	public  float targetRadius;
	public  Vector2 cursorTarget;
	public  float focusTime; //number of seconds the cursor been on an object
	public  float waitOnFocusTime;  //the time in seconds the cursor must be on an object before moving to the next action
	public  float maxCursorVelocity;
	#endregion

	//for all actions
	//TODO: separate EyeManager, ActionManager (single action), and ChapterManager (the whole chapter)
	private string[] targetObjects;

	//for paint //TODO: change the name of "paint" to "scan," because it makes more sense
	private string[] targetObjectsForPaint; //TODO: perhaps this should be an array of GameObjects instead?
	private RaycastHit2D[] paintedObjects;

	//for trace
	private string[] targetObjectsForTrace;
	private RaycastHit2D[] tracedObjects;
	private int tracedIndex;
	private bool invisible;

	// Use this for initialization
	void Start () {
		leftEye = transform.Find("Left Eye");
		rightEye = transform.Find("Right Eye");
		radiusMarker = rightEye.transform.Find("topWallCheck");
		cursorPoint = transform.Find("Cursor");
		cursorCollider = cursorPoint.transform.Find("objectCheck");
		mode = "";

		cursorVelocity.x = 0;
		cursorVelocity.y = 0;
		focusTime = 0f;
		waitOnFocusTime = 2f;
		targetRadius = 0.5f;
		maxCursorVelocity = 5f;

		eyeballRadius = Mathf.Abs(rightEye.transform.position.y - radiusMarker.transform.position.y);
	}


	// Update is called once per frame
	void Update () {

		// find distance between the 2 eyes
		lrDistance = Vector2.Distance(rightEye.transform.position, leftEye.transform.position);

		// find midpoint between the 2 eyes to find the cursor's position
		midPoint = (rightEye.transform.position - leftEye.transform.position)*0.5f + leftEye.transform.position;

		// update cursor position with the midPoint
		updateCursor();

		if(lrDistance < eyeballRadius)
			canTarget = true;
		else
			canTarget = false;

		//TODO: Move this to new ActionManager
		if (mode == "")	{
			mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
		}

		//TODO: EVERYTHING
		isOverObject = checkIntersection();

		// if cursor leaves targetable object, restart focus time, reset appearance, and remove references
		if(isOverObject == false && lastObj != null) {
			focusTime = 0f;
			lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
			lastObj = null;
			print("lastObj nullified.");
		}
	}


	void updateCursor() {
		Vector2 cursorPos;
		cursorPos.x = cursorPoint.transform.position.x;
		cursorPos.y = cursorPoint.transform.position.y;

		if(canTarget){ //if players are focused
			float step = maxCursorVelocity * Time.deltaTime;
			cursorPoint.transform.position = Vector2.MoveTowards(cursorPoint.transform.position, midPoint, step);
			cursorVelocity.x = 0f;
			cursorVelocity.y = 0f;
		}else{ //if players are not focused, wander
			//check if a new target needs to be acquired
			if(cursorTarget == null) 
				cursorTarget = getNewCursorTarget();
			if(Vector2.Distance(cursorPos, cursorTarget) < targetRadius || Vector2.Distance(cursorPos, cursorTarget) > lrDistance)
				cursorTarget = getNewCursorTarget();

			//determine which direction to move cursor
			if(cursorPos.x > cursorTarget.x)
				cursorVelocity.x -= 0.001f;
			else if(cursorPos.x < cursorTarget.x)
				cursorVelocity.x += 0.001f;

			if(cursorPos.y > cursorTarget.y)
				cursorVelocity.y -= 0.001f;
			else if(cursorPos.y < cursorTarget.y)
				cursorVelocity.y += 0.001f;

			Vector2 newCursorPos;
			newCursorPos.x = cursorPoint.transform.position.x + cursorVelocity.x;
			newCursorPos.y = cursorPoint.transform.position.y + cursorVelocity.y;
			cursorPoint.transform.position = newCursorPos;
		}
	}


	Vector2 getNewCursorTarget() {
		Vector2 target;
		float radius = Random.value*(lrDistance/4);
		float angle = Random.value*2*Mathf.PI;
		target.x = radius * Mathf.Cos(angle) + midPoint.x;
		target.y = radius * Mathf.Sin(angle) + midPoint.y;
		return target;
	}

	bool checkFindCompleted () {
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

	bool checkPaintCompleted () {
		for (int i=0; i<targetObjectsForPaint.Length; i++){
			if (targetObjectsForPaint[i] == "unvisited"&&targetObjects[i] != ""&&targetObjects[i] != null){
				Debug.Log("what is unvisited?: "+targetObjects[i]);
				return false;
			}
		}
		for (int j=0; j<paintedObjects.Length; j++){
			if (paintedObjects[j].transform){
				paintedObjects[j].transform.GetComponent<SpriteRenderer>().color = Color.white;
			}
		}
		Debug.Log("all are visited.");
		targetObjectsForPaint = null;
		return true;
	}

	bool checkTraceCompleted () {
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
	
	bool checkIntersection () {
		targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
		if (mode == "find") {
			if(canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position,
				                                      cursorCollider.position,
				                                      1 << LayerMask.NameToLayer("Object"));
				if (obj) {

					//get properties of the current object
					lastObj = curObj;
					curObj = obj.transform;

					string id = null;
					if (curObj){

						//for getting object id. I don't use "name" is because name is a build-in property of all Unity game objects
						GameItem gi = curObj.GetComponent<GameItem>();
						id = gi.id;
					}

					if (id != null){
					

						//wait for focusTime seconds before determining that players have made their selection
						focusTime += Time.deltaTime;
						if(focusTime > waitOnFocusTime){

							int targetObjectNum = targetObjects.Length;

							//check if the object is indeed the target object
							for (int i=0; i<targetObjectNum; i++){

								//if it is correct
								if (targetObjects[i] == id){
									curObj.GetComponent<SpriteRenderer>().color = Color.green;
									targetObjects[i] = "done";

								//if it's not correct
								}else if(targetObjects[i] == null){

								}else{
									curObj.GetComponent<SpriteRenderer>().color = Color.red;
								}
							}

							if (checkFindCompleted()){;
								GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
								targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
								mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
								Debug.Log("updated curaction[0] is: "+targetObjects[0]);
								focusTime = 0f;
							}
						}
					}
					//if target object changed without targeting empty space
					if(lastObj != null && curObj != null && lastObj != curObj) {
						lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
						lastObj = null;
					}

					return true;
				}
			}
		}

		if (mode == "focus") {
			RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, cursorCollider.position, 1 << LayerMask.NameToLayer("Object"));
			if (obj.transform != null) {
				lastObj = curObj;
				curObj = obj.transform;
				string targetObject = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects[0];
				string id = null;
				if (curObj){
					GameItem gi = curObj.GetComponent<GameItem>();
					id = gi.id;
					print ("Current object id is: " + id);
				}
				if (id != null && id == targetObject){
					//wait for focusTime seconds before determining that players have made their selection
					//TODO: FIX THESE CONDITIONS
					focusTime += Time.deltaTime;
					if(focusTime > waitOnFocusTime){
						curObj.GetComponent<SpriteRenderer>().color = Color.green;
						GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
						targetObject = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects[0];
						mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
						Debug.Log("updated curaction[0] is: "+targetObject);
						focusTime = 0f;
					}
				}else{
					//you haven't found anything, so reset the time
					print ("I haven't found anything...");
					//focusTime = 0f;
				}
				if(lastObj != null && curObj != null && lastObj != curObj) {
					lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
					lastObj = null;
				}
				return true;
			}
		}

		if (mode == "paint") {
			if (targetObjectsForPaint == null){
				targetObjectsForPaint = new string[targetObjects.Length];
				for (int i = 0; i<targetObjects.Length; i++){
					targetObjectsForPaint[i] = "unvisited";
					Debug.Log("-----------init all nodes as unvisited----------");
				}
			}
			if (paintedObjects == null){
				paintedObjects = new RaycastHit2D[targetObjects.Length];
			}
			if (canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, cursorCollider.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj != null) {
					//get properties of the current object
					lastObj = curObj;
					curObj = obj.transform;
					string id = null;
					if (curObj){
						GameItem gi = curObj.GetComponent<GameItem>();
						id = gi.id;
					}
					if (id != null){
						for (int i=0; i<targetObjects.Length; i++){	
							//we are painting an unvisited node, so we set it green, and set it as visited
							if (targetObjects[i] == id && targetObjectsForPaint[i] == "unvisited"){
								curObj.GetComponent<SpriteRenderer>().color = Color.green;
								targetObjectsForPaint[i] = "visited";
								paintedObjects[i] = obj;
								break;
								Debug.Log("-----------this is visiting: "+id+"----------");
							}
							//we are painting a visited node, so we keep it green.
							else if(targetObjects[i] == id && targetObjectsForPaint[i] == "visited"){
								curObj.GetComponent<SpriteRenderer>().color = Color.green;
								Debug.Log("-----------this is visited: "+id+"----------");
								break;
							}else if(targetObjects[i] == null){
									
							}
							//we are painting something that is not inside the painting shape, so we set it red
							else{
								Debug.Log("-----------this makes it red: "+id+"----------");
								curObj.GetComponent<SpriteRenderer>().color = Color.red;
							}
						}
							
						if (checkPaintCompleted()){
							targetObjectsForPaint = null;
							GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
							targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
							mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
							Debug.Log("updated curaction[0] is: "+targetObjects[0]);
							focusTime = 0f;
						}
					}
					return true;
				}
			}
		}

		if (mode == "trace") {
			if (targetObjectsForTrace == null){
				targetObjectsForTrace = new string[targetObjects.Length];
				for (int i = 0; i<targetObjects.Length; i++){
					targetObjectsForTrace[i] = "unvisited";
					Debug.Log("-----------init all nodes as unvisited----------");
				}
			}
			if (invisible == false){
				for (int i = tracedIndex; i<targetObjects.Length; i++){
					string s = "OutlineOldGuyNode"+i.ToString();
					if (GameObject.Find(s)){
						GameObject.Find(s).GetComponent<SpriteRenderer>().enabled = false;
					}
				}
				for (int i = 0; i<= tracedIndex; i++){
					string s1 = "OutlineOldGuyNode"+i.ToString();
					if (GameObject.Find(s1)){
						GameObject.Find(s1).GetComponent<SpriteRenderer>().enabled = true;
					}
				}
				invisible = true;
			}
			if (tracedObjects == null){
				tracedObjects = new RaycastHit2D[targetObjects.Length];
			}
			if (canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, cursorCollider.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj != null) {
					//get properties of the current object
					lastObj = curObj;
					curObj = obj.transform;
					string id = null;
					if (curObj){
						GameItem gi = curObj.GetComponent<GameItem>();
						id = gi.id;
					}
					if (id != null){
						//we are tracing an unvisited node, so we set it green, and set it as visited
						if (targetObjects[tracedIndex] == id && targetObjectsForTrace[tracedIndex] == "unvisited"){
							curObj.GetComponent<SpriteRenderer>().color = Color.green;
							targetObjectsForTrace[tracedIndex] = "visited";
							tracedObjects[tracedIndex] = obj;
							tracedIndex++;
							Debug.Log("-----------this is visiting: "+id+"----------");
							invisible = false;
						}
						//we are tracing a visited node, so we keep it green.
						for (int i=0; i<targetObjects.Length; i++){	
							if(targetObjects[i] == id && targetObjectsForTrace[i] == "visited"){
								curObj.GetComponent<SpriteRenderer>().color = Color.green;
								Debug.Log("-----------this is visited: "+id+"----------");
							}else if(targetObjects[i] == null){

							}
							//we are painting something that is not inside the painting shape, so we set it red
							else{
								Debug.Log("-----------this makes it red: "+id+"----------");
								//curObj.GetComponent<SpriteRenderer>().color = Color.red;
							}
						}
						
						if (checkTraceCompleted()){
							targetObjectsForPaint = null;
							GameObject.Find("ChapterManager").GetComponent<ChapterManager>().updateAction();
							targetObjects = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.targetObjects;
							mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
							Debug.Log("updated curaction[0] is: "+targetObjects[0]);
							focusTime = 0f;
						}
					}
					return true;
				}
			}
		}

		return false;
	}
	
}
