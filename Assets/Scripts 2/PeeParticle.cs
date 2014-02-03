using UnityEngine;
using System.Collections;

public class PeeParticle : MonoBehaviour {

	private GameObject self;
	private Vector2 position;
	public Vector2 velocity;
	public int blah;

	// Use this for initialization
	void Start () {
		self = this.gameObject;
		position.x = 0;
		position.y = 0;
		velocity.x = 0;
		velocity.y = 0;
		blah = 0;
	}
	
	// Update is called once per frame
	void Update () {
		position.x+= velocity.x;
		position.y+= velocity.y;
		self.transform.position = position;
	}

	public void setPosition(Vector2 pos) {
		position.x = pos.x;
		position.y = pos.y;
	}

	public void setVelocity(Vector2 vel) {
		velocity.x = vel.x;
		velocity.y = vel.y;
	}

	public void correctRotation() {
		Quaternion rotate;
		rotate.x = 0;
		rotate.y = 0;
		rotate.z = 0;
		rotate.w = 0;
		this.transform.rotation = rotate;
	}


}
