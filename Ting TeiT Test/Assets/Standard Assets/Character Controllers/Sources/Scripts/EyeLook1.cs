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
public class EyeLook1 : MonoBehaviour {

	//public enum RotationAxes { MouseXAndY = 0, MouseX = 1, MouseY = 2 }
	//public RotationAxes axes = RotationAxes.MouseXAndY;mainCamera
	//public float sensitivityX = 15F;
	//public float sensitivityY = 15F;
	
	public Camera mainCamera;
	
	//public float minimumX = -360F;
	//public float maximumX = 360F;

	//public float minimumY = -60F;
	//public float maximumY = 60F;
	
	public float velocityX = 0F;
	public float velocityY = 0F;
	
	float MN1 = 1.27F;//Magic Number: Roughly the amount of units from center of camera to the edge?
	float xBorder = 0.02F;//Magic Number: Roughly the amount of X units to keep whole square within viewport.
	float yBorder = 0.05F;//Magic Number: Roughly the amount of X units to keep whole square within viewport.
	
	public float posX;
	public float posY;
	public float posZ;
	public float temp;
	

	void Update ()
	{
		//if(transform.position.x > minimumX && transform.position.x < minimumX )
			//positionX = transform.position.x + velocityX;
		//if(transform.position.y > minimumY && transform.position.y < minimumY )
			//positionY = transform.position.y + velocityY;
		//positionZ = transform.position.z;
		
		//Tried to transform based on Camera position, Failed: cannot add bounding.
		//position = mainCamera.transform.position + mainCamera.transform.forward + mainCamera.transform.right*(velocityX);
		
		//if(transform.position + mainCamera.transform.right*(velocityX)< mainCamera.transform.position + mainCamera.transform.right*(float)(velocityX*MN1))
		//	position = transform.position + mainCamera.transform.right*(velocityX/1000);
		
		if(Input.GetKey(KeyCode.A))
			velocityX -= 0.05f;
		if(Input.GetKey(KeyCode.D))
			velocityX += 0.05f;
		
		if(Input.GetKey(KeyCode.S))
			velocityY -= 0.05f;
		if(Input.GetKey(KeyCode.W))
			velocityY += 0.05f;
		
		//Tried to transform using viewspace. Convert 3Dvector to viewport 2D vector, transform, then convert back.
		Vector3 viewPos = mainCamera.WorldToViewportPoint(transform.position);

		if(viewPos.x + velocityX/1000 < 1 - xBorder && viewPos.x + velocityX/1000 > 0 + xBorder)
			viewPos.x = viewPos.x + velocityX/1000;
		if(viewPos.y + velocityY/1000 < 1 - yBorder && viewPos.y + velocityY/1000 > 0 + yBorder)
			viewPos.y = viewPos.y + velocityY/1000;

		transform.position = mainCamera.ViewportToWorldPoint(viewPos);
		//transform.position = position;
		
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