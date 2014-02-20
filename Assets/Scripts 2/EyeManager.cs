using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class EyeManager : MonoBehaviour {

	private Transform leftEye;
	private Transform rightEye;
	private Transform radiusMarker;
	private Transform objectCheck;
	private Transform lastObj;
	private Transform curObj;
	
	public string mode;

	private float R;
	public  float lrDistance;
	private Vector2 midPoint;
	private bool canTarget;
	private bool isOverObject;

	#region Cursor Variables
	private Transform cursorPoint;
	private Vector2 newCursorPos;
	public  Vector2 cursorVelocity;
	public  float cursorToTargetDistance;
	public  float targetRadius;
	public  Vector2 cursorTarget;
	public  float focusTime; //number of seconds the cursor been on an object
	public  float waitOnFocusTime;  //the time in seconds the cursor must be on an object before moving to the next action
	public  float maxCursorVelocity;
	#endregion

	private bool    objectHasIncreasedInSize;
	public  Vector3 oldScale;
	private float   scaleSize;
	private string lastObjName;

	//for all actions
	private string[] targetObjects;

	//for paint
	private string[] targetObjectsForPaint;
	private RaycastHit2D[] paintedObjects;

	//for paint
	private string[] targetObjectsForTrace;
	private RaycastHit2D[] tracedObjects;
	private int tracedIndex;

	// Use this for initialization
	void Start () {
		leftEye = transform.Find("Left Eye");
		rightEye = transform.Find("Right Eye");
		radiusMarker = rightEye.transform.Find("topWallCheck");
		cursorPoint = transform.Find("Cursor");
		objectCheck = cursorPoint.transform.Find("objectCheck");
		mode = "";

		cursorVelocity.x = 0;
		cursorVelocity.y = 0;
		R = 0.5f;
		focusTime = 0f;
		waitOnFocusTime = 2f;
		targetRadius = 0.5f;
		maxCursorVelocity = 5f;

		oldScale = Vector3.zero;
		scaleSize = 1.3f;
	}


	// Update is called once per frame
	void Update () {
		R = Mathf.Abs(rightEye.transform.position.y - radiusMarker.transform.position.y);

		// find distance between the 2 eyes
		lrDistance = Vector2.Distance(rightEye.transform.position, leftEye.transform.position);

		// find midpoint between the 2 eyes to find the cursor's position
		midPoint = (rightEye.transform.position - leftEye.transform.position)*0.5f + leftEye.transform.position;

		// update cursor position with the midPoint
		updateCursor();

		if(lrDistance < R)
			canTarget = true;
		else
			canTarget = false;
		if (mode == "")	{
			mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
		}

		if(Input.GetKey(KeyCode.Alpha1))
			mode = "find";
		if(Input.GetKey(KeyCode.Alpha2))
			mode = "focus";	
		if(Input.GetKey(KeyCode.Alpha3))
			mode = "trace";
		if(Input.GetKey(KeyCode.Alpha4))
			mode = "paint";

		isOverObject = checkIntersection();

		// if cursor leaves targetable object, restart focus time, reset appearance, and remove references
		if(isOverObject == false && lastObj != null) {
			//TODO: how is oldScale getting changed from line 187 to here?
			print ("cursor has left the targetable object.");
			print ("oldScale = " + oldScale);
			print ("lastObj.localScale = " + lastObj.localScale);
			print ("need to assign oldScale to lastObj.localScale");
			focusTime = 0f;
			//objectHasIncreasedInSize = false;
			lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
			lastObj = null;
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
				                                      objectCheck.position,
				                                      1 << LayerMask.NameToLayer("Object"));
				if (obj) {

					//get properties of the current object
					lastObj = curObj;
					curObj = obj.transform;
					oldScale = curObj.localScale;

					string id = null;
					if (curObj){

						//for getting object id. I don't use "name" is because name is a build-in property of all Unity game objects
						GameItem gi = curObj.GetComponent<GameItem>();
						id = gi.id;
						lastObjName = id;
					}

					if (id != null){

						if(id != lastObjName){
							objectHasIncreasedInSize = false;
						}

						if(!objectHasIncreasedInSize){

							//increase the size of the object by a small amount
							//TODO: fix the not being able to reset size problem
							float curObjScaleX = curObj.localScale.x;
							float curObjScaleY = curObj.localScale.y;
							float curObjScaleZ = curObj.localScale.z;

							Vector3 curObjScale = new Vector3(curObjScaleX, curObjScaleY, curObjScaleZ);

							float oldScaleX = curObjScale.x;
							float oldScaleY = curObjScale.y;
							float oldScaleZ = curObjScale.z;

							oldScale = new Vector3(oldScaleX, oldScaleY, oldScaleZ);
							print ("curObj.localScale = " + curObj.localScale + ": saved in oldScale");
						
							float newScaleX = curObjScale.x * scaleSize;
							float newScaleY = curObjScale.y * scaleSize;

							print ("oldScale = " + oldScale);
							curObj.localScale = new Vector3(newScaleX, newScaleY, oldScaleZ);

							objectHasIncreasedInSize = true;
						}

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
			RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
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
						//TODO: place a green check mark for great success!!
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
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj != null) {
					//get properties of the current object
					lastObj = curObj;
					curObj = obj.transform;
					string id = null;
					if (curObj){
						GameItem gi = curObj.GetComponent<GameItem>();
						id = gi.id;
						lastObjName = id;
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
			if (tracedObjects == null){
				tracedObjects = new RaycastHit2D[targetObjects.Length];
			}
			if (canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj != null) {
					//get properties of the current object
					lastObj = curObj;
					curObj = obj.transform;
					string id = null;
					if (curObj){
						GameItem gi = curObj.GetComponent<GameItem>();
						id = gi.id;
						lastObjName = id;
					}
					if (id != null){
						//we are tracing an unvisited node, so we set it green, and set it as visited
						if (targetObjects[tracedIndex] == id && targetObjectsForTrace[tracedIndex] == "unvisited"){
							curObj.GetComponent<SpriteRenderer>().color = Color.green;
							targetObjectsForTrace[tracedIndex] = "visited";
							tracedObjects[tracedIndex] = obj;
							tracedIndex++;
							Debug.Log("-----------this is visiting: "+id+"----------");
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
