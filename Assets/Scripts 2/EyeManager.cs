using UnityEngine;
using System.Collections;

public class EyeManager : MonoBehaviour {

	private Transform leftEye;
	private Transform rightEye;
	private Transform radiusMarker;
	private Transform cursorPoint;
	private Transform objectCheck;
	private Transform lastObj;
	private Transform curObj;

	public int mode;

	private float R;
	private float lrDistance;
	private Vector2 midPoint;
	private bool canTarget;
	private bool isOverObject;

	private Vector2 newCursorPos;
	public Vector2 cursorVelocity;

	// Use this for initialization
	void Start () {
		leftEye = transform.Find("Left Eye");
		rightEye = transform.Find("Right Eye");
		radiusMarker = rightEye.transform.Find("topWallCheck");
		cursorPoint = transform.Find("Cursor");
		objectCheck = cursorPoint.transform.Find("objectCheck");
		mode = 1;

		cursorVelocity.x = 0;
		cursorVelocity.y = 0;
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
			mode = 1;
		if(Input.GetKey(KeyCode.Alpha2))
			mode = 2;
		
		if(Input.GetKey(KeyCode.Alpha3))
			mode = 3;
		if(Input.GetKey(KeyCode.Alpha4))
			mode = 4;

		isOverObject = checkIntersection();

		// if cursor leaves targetable object, undo highlighting, remove references
		if(isOverObject == false && lastObj != null) {
			lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
			lastObj = null;
		}

	}

	void updateCursor() {
		if(canTarget)
			cursorPoint.transform.position = midPoint;
		else {
			cursorVelocity.x += (Random.value-0.5f)/1000;
			cursorVelocity.y += (Random.value-0.5f)/1000;

			newCursorPos.x = cursorPoint.transform.position.x + cursorVelocity.x;
			newCursorPos.y = cursorPoint.transform.position.y + cursorVelocity.y;
			cursorPoint.transform.position = newCursorPos;
		}
	}

	bool checkIntersection () {
		if(mode == 1) { //if in find mode
			if(canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj != null) {
					lastObj = curObj;
					curObj = obj.transform;
					curObj.GetComponent<SpriteRenderer>().color = Color.red;
					//if target object changed without targeting empty space
					if(lastObj != null && curObj != null && lastObj != curObj) {
						lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
						lastObj = null;
					}
					return true;
				}
			}
		}
		if (mode == 2) { //if in focus mode
			RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
			if (obj.transform != null) {
				lastObj = curObj;
				curObj = obj.transform;
				curObj.GetComponent<SpriteRenderer>().color = Color.red;
				return true;
			}
		}
				
		return false;
	}
	
}
