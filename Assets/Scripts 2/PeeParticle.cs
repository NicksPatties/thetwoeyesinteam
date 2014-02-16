﻿using UnityEngine;
using System.Collections.Generic;

public class PeeParticle : MonoBehaviour {

	private Transform self;
	private Vector2 position;
	public Vector2 targetPosition;
	public float maxVelocity;
	public bool original;
	public GameObject stream;
	public PeeStream ps;
	private bool active;
	//public List<Transform> pee;

	// Use this for initialization
	void Start () {
		stream = GameObject.Find("PeeStream");
		ps = (PeeStream) stream.GetComponent(typeof(PeeStream));
		//pee = ps.pee;
		self = this.transform;
		position.x = 0;
		position.y = 0;
		targetPosition.x = 0;
		targetPosition.y = 0;
		maxVelocity = 0;
		original = false;
		active = true;
	}
	
	// Update is called once per frame
	void Update () {
		//position.x+= velocity.x;
		//position.y+= velocity.y;
		//self.transform.position = position;
		if(!original) {
			if(maxVelocity == 0)
				getVelocity();
			if (targetPosition.x == 0 && targetPosition.y == 0) {
				getTarget();
			}
			if(targetPosition.x == transform.position.x && targetPosition.y == transform.position.y) {
				resetPosition();
				active = false;
				ps.addToQueue(this);
				//getTarget();
			}
			if(active) {
				float step = maxVelocity * Time.deltaTime;
				transform.position = Vector2.MoveTowards(transform.position, targetPosition, step);
			}
		}
	}

	void getVelocity() {
		if(stream != null) {
			PeeStream ps = (PeeStream) stream.GetComponent(typeof(PeeStream));
			if(ps != null)
				maxVelocity = ps.particleVelocity;
		}
	}

	public void getTarget() {
		GameObject target = GameObject.Find("Cursor");
		if(target != null){
			targetPosition.x = target.transform.position.x+(Random.value/2-0.25f);
			targetPosition.y = target.transform.position.y+(Random.value/2-0.25f);
		}
	}

	void resetPosition() {
		if(stream != null) {
			Vector2 pos;
			pos.x = stream.transform.position.x;
			pos.y = stream.transform.position.y;
			transform.position = pos;
		}
	}

	public void setOriginality() {
		original = true;
	}

	public void setActive() {
		active = true;
	}

	public void setPosition(Vector2 pos) {
		position.x = pos.x;
		position.y = pos.y;
		transform.position = position;
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
