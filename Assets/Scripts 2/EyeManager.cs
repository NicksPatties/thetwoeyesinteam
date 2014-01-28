using UnityEngine;
using System.Collections;

public class EyeManager : MonoBehaviour {

	private Transform leftEye;
	private Transform rightEye;
	private Transform radiusMarker;
	private Transform objectCheck;
	private Transform lastObj;
	private Transform curObj;
	
	public int mode;

	private float R;
	public float lrDistance;
	private Vector2 midPoint;
	private bool canTarget;
	private bool isOverObject;

	#region Cursor Variables
	private Transform cursorPoint;
	private Vector2 newCursorPos;
	public Vector2 cursorVelocity;
	public float D1;
	public float R2;
	public Vector2 cursorTarget;
	public float focusTime; //number of seconds the eyes have been on an object together
	public float waitTime;  //the time in seconds the eyes must be on an object before moving to the next action
	#endregion

	//this varable refers to the curAction in taskmanager
	private string[] curaction;

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
		R2 = 0.5f;
		focusTime = 0f;
		waitTime  = 2f;
	}
	
	// Update is called once per frame
	void Update () {
		if (curaction == null){
			curaction = GameObject.Find("TaskManager").GetComponent<TaskManagerChp3>().curAction;
			Debug.Log("curaction[0] is: "+curaction[0]);
		}

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

		/*TODO:check if an eye is hovering over an object
		RaycastHit2D obj = Physics2D.Linecast(rightEye.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
		if(obj != null){
			print ("The right eye is over an object!");
			obj.transform.GetComponent<SpriteRenderer>().color = Color.yellow;
		}
		*/

		// if cursor leaves targetable object, undo highlighting, remove references
		if(isOverObject == false && lastObj != null) {
			lastObj.transform.GetComponent<SpriteRenderer>().color = Color.white;
			lastObj = null;
		}

	}

	void updateCursor() {
		Vector2 cursorPos;
		cursorPos.x = cursorPoint.transform.position.x;
		cursorPos.y = cursorPoint.transform.position.y;

		if(canTarget) //if players are focused
			cursorPoint.transform.position = midPoint;
		else { //if players are not focused, wander
			//check if a new target needs to be acquired
			if(cursorTarget == null) 
				cursorTarget = getNewCursorTarget();
			if(Vector2.Distance(cursorPos, cursorTarget) < R2 || Vector2.Distance(cursorPos, cursorTarget) > lrDistance)
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

	bool checkIntersection () {
		if(mode == 1) { //if in find mode
			if(canTarget) {
				RaycastHit2D obj = Physics2D.Linecast(cursorPoint.transform.position, objectCheck.position, 1 << LayerMask.NameToLayer("Object"));
				if (obj != null) {
					lastObj = curObj;
					curObj = obj.transform;
					//for getting object id. I don't use "name" is because name is a build-in property of all Unity game objects
					//TODO: figure out why some game objects that have the "GameItem" script attatched still return null here...
					GameItem gi = curObj.GetComponent<GameItem>();
					string id = gi.id;
					Debug.Log("name is: "+id);
					if (id != null && id == curaction[0]){
						//wait for focusTime seconds before determining that players have made their selection
						focusTime += Time.deltaTime;
						if(focusTime > waitTime){

							//TODO: place a green check mark for great success!!
							curObj.GetComponent<SpriteRenderer>().color = Color.red;
							GameObject.Find("TaskManager").GetComponent<TaskManagerChp3>().updateAction();
							curaction = GameObject.Find("TaskManager").GetComponent<TaskManagerChp3>().curAction;
							Debug.Log("updated curaction[0] is: "+curaction[0]);

							//reset focus time
							focusTime = 0f;
						}
					}
					//curObj.GetComponent<SpriteRenderer>().color = Color.red;
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
