using UnityEngine;
using System.Collections;

public class EyeManager : MonoBehaviour {

	private Transform leftEye;
	private Transform rightEye;
	private Transform radiusMarker;
	private Transform objectCheck;
	private Transform lastObj;
	private Transform curObj;

	//I feel like string is better here, since
	//1.string is not less comvenient than int for this case
	//2. u don't need comments for different modes to explain what action it corresponds
	//TODO: but what about enums?
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
	#endregion

	private bool    objectHasIncreasedInSize;
	public Vector3 oldScale;
	private float   scaleSize;

	private string[] targetObjects;

	// Use this for initialization
	void Start () {
		leftEye = transform.Find("Left Eye");
		rightEye = transform.Find("Right Eye");
		radiusMarker = rightEye.transform.Find("topWallCheck");
		cursorPoint = transform.Find("Cursor");
		objectCheck = cursorPoint.transform.Find("objectCheck");
		mode = "find";

		cursorVelocity.x = 0;
		cursorVelocity.y = 0;
		R = 0.5f;
		focusTime = 0f;
		waitOnFocusTime = 2f;
		targetRadius = 0.5f;

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
			/*TODO: how is oldScale getting changed from line 187 to here?
			print ("cursor has left the targetable object.");
			print ("oldScale = " + oldScale);
			print ("lastObj.localScale = " + lastObj.localScale);
			print ("need to assign oldScale to lastObj.localScale");
			*/
			focusTime = 0f;
			objectHasIncreasedInSize = false;
			lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
			lastObj = null;
		}
	}


	void updateCursor() {
		Vector2 cursorPos;
		cursorPos.x = cursorPoint.transform.position.x;
		cursorPos.y = cursorPoint.transform.position.y;

		if(canTarget){ //if players are focused
			cursorPoint.transform.position = midPoint;
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

	bool checkActionCompleted () {
		//string[] targetObjs = GameObject.Find("TaskManager").GetComponent<ChapterManager>().curAction.targetObjects;
		int targetObjectNum = targetObjects.Length;
		for (int i=0; i<targetObjectNum; i++){
			if (targetObjects[i] != "done"&&targetObjects[i] != null){
				Debug.Log("what is not done?: "+targetObjects[i]);
				return false;
			}
		}
		return true;
	}
	
	bool checkIntersection () {
		if(mode == "find") {
			if(canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj) {

					//get properties of the current object
					lastObj = curObj;
					curObj = obj.transform;
					oldScale = curObj.localScale;

					//find the target object
					targetObjects = GameObject.Find("TaskManager").GetComponent<ChapterManager>().curAction.targetObjects;
					string id = null;
					if (curObj){

						//for getting object id. I don't use "name" is because name is a build-in property of all Unity game objects
						GameItem gi = curObj.GetComponent<GameItem>();
						id = gi.id;
					}

					if (id != null){
						if(!objectHasIncreasedInSize){

							//increase the size of the object by a small amount
							//TODO: fix the not being able to reset size problem
							/*float curObjScaleX = curObj.localScale.x;
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
							curObj.localScale = new Vector3(newScaleX, newScaleY, oldScaleZ);*/
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

							if (checkActionCompleted()){;
								GameObject.Find("TaskManager").GetComponent<ChapterManager>().updateAction();
								targetObjects = GameObject.Find("TaskManager").GetComponent<ChapterManager>().curAction.targetObjects;
								mode = GameObject.Find("TaskManager").GetComponent<ChapterManager>().curAction.actionName;
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
				//string targetObjec = GameObject.Find("TaskManager").GetComponent<TaskManagerChp3>().curAction[0];
				string targetObject = GameObject.Find("TaskManager").GetComponent<ChapterManager>().curAction.targetObjects[0];
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
						/**old code for action list read
						//GameObject.Find("TaskManager").GetComponent<TaskManagerChp3>().updateAction();
						//targetObject = GameObject.Find("TaskManager").GetComponent<TaskManagerChp3>().curAction[0];
						//mode = GameObject.Find("TaskManager").GetComponent<TaskManagerChp3>().curAction[1];
						**/
						GameObject.Find("TaskManager").GetComponent<ChapterManager>().updateAction();
						targetObject = GameObject.Find("TaskManager").GetComponent<ChapterManager>().curAction.targetObjects[0];
						mode = GameObject.Find("TaskManager").GetComponent<ChapterManager>().curAction.actionName;
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
			if(canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj != null) {
					lastObj = curObj;
					curObj = obj.transform;
					curObj.GetComponent<SpriteRenderer>().color = Color.blue;
					if(lastObj != null && curObj != null && lastObj != curObj) {
						lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
						lastObj = null;
					}
					return true;
				}
			}
		}
		return false;
	}
	
}
