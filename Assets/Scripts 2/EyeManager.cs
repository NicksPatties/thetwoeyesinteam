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

		//this is moved to actionmanager, but we still need a reference here
		if (mode == "")	{
			mode = GameObject.Find("ChapterManager").GetComponent<ChapterManager>().curAction.actionName;
		}

		//TODO: EVERYTHING
		isOverObject = checkIntersection();

		// if cursor leaves targetable object, restart focus time, reset appearance, and remove references
		if(isOverObject == false && lastObj != null && mode != "scan" && mode != "trace") {
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

	bool checkIntersection () {
		if(canTarget) {
			RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position,
				                                      cursorCollider.position,
				                                      1 << LayerMask.NameToLayer("Object"));
			if (obj) {	
				lastObj = curObj;
				curObj = obj.transform;

				string objectName = null;
				if (curObj){
					GameItem gameItem = curObj.GetComponent<GameItem>();
					objectName = gameItem.id;
				}
					
				focusTime += Time.deltaTime;
				bool isRightObject = false;
				bool isActionComplete = false;
				if(focusTime > waitOnFocusTime){
					isRightObject = GameObject.Find("ActionManager").GetComponent<ActionManager>().checkCorrectness(objectName,curObj);
					if (isRightObject){
						focusTime = 0f;
					}
				}
				return true;
			}
		}
		return false;
	}
}