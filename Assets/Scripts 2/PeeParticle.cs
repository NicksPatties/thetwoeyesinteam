using UnityEngine;
using System.Collections;

public class PeeParticle : MonoBehaviour {

	private Vector2 pos;
	private GameObject self;

	// Use this for initialization
	void Start () {
		self = this.gameObject;
	}
	
	// Update is called once per frame
	void Update () {
		self.transform.position = pos;
	}

	public void setPosition(Vector2 position) {
		pos.x = position.x;
		pos.y = position.y;
	}

	public void setVelocity() {

	}


}
