using UnityEngine;
using System.Collections;

/// MouseLook rotates the transform based on the mouse delta.
/// Minimum and Maximum values can be used to constrain the possible rotation

/// To make an FPS style character:
/// - Create a capsule.
/// - Add the MouseLook script to the capsule.
///   -> Set the mouse look to use LookX. (You want to only turn character but not tilt it)
/// - Add FPSInputController script to the capsule
///   -> A CharacterMotor and a CharacterController component will be automatically added.

/// - Create a camera. Make the camera a child of the capsule. Reset it's transform.
/// - Add a MouseLook script to the camera.
///   -> Set the mouse look to use LookY. (You want the camera to tilt up and down like a head. The character already turns.)
[AddComponentMenu("Camera-Control/Mouse Look")]
public class EyeLook : MonoBehaviour {

	public bool left = false; //Used to determine the control scheme, either WASD or LRUP Arrows

	public float velocityX = 0F;
	public float velocityY = 0F;
	
	private float posX;
	private float posY;
	private float temp;

	private Transform topWallCheck;
	private bool topWall = false;
	private Transform floorWallCheck;
	private bool floorWall = false;
	private Transform leftWallCheck;
	private bool leftWall = false;
	private Transform rightWallCheck;
	private bool rightWall = false;

	void Awake () {
		topWallCheck = transform.Find("topWallCheck");
		floorWallCheck = transform.Find("floorWallCheck");
		leftWallCheck = transform.Find("leftWallCheck");
		rightWallCheck = transform.Find("rightWallCheck");
	}

	void Update ()
	{
		topWall = Physics2D.Linecast(transform.position, topWallCheck.position, 1 << LayerMask.NameToLayer("Wall"));
		floorWall = Physics2D.Linecast(transform.position, floorWallCheck.position, 1 << LayerMask.NameToLayer("Wall"));
		leftWall = Physics2D.Linecast(transform.position, leftWallCheck.position, 1 << LayerMask.NameToLayer("Wall"));
		rightWall = Physics2D.Linecast(transform.position, rightWallCheck.position, 1 << LayerMask.NameToLayer("Wall"));
		
		if(topWall | floorWall)
			velocityY = 0f;

		if(leftWall | rightWall)
			velocityX = 0f;

		if (left) {
			if(Input.GetKey(KeyCode.A))
				velocityX -= 0.05f;
			if(Input.GetKey(KeyCode.D))
				velocityX += 0.05f;
			
			if(Input.GetKey(KeyCode.S))
				velocityY -= 0.05f;
			if(Input.GetKey(KeyCode.W))
				velocityY += 0.05f;

		}
		else {
			if(Input.GetKey(KeyCode.K))
				velocityX -= 0.05f;
			if(Input.GetKey(KeyCode.Semicolon))
				velocityX += 0.05f;
			if(Input.GetKey(KeyCode.L))
				velocityY -= 0.05f;
			if(Input.GetKey(KeyCode.O))
				velocityY += 0.05f;
		}
		


		Vector2 viewPos = transform.position;
		viewPos.x = viewPos.x + velocityX/50;
		viewPos.y = viewPos.y + velocityY/50;
		transform.position = viewPos;
	}
	
	void Start ()
	{
		//positionX = transform.position.x;
		//positionY = transform.position.y;
		// Make the rigid body not change rotation
		if (rigidbody)
			rigidbody.freezeRotation = true;
	}
}