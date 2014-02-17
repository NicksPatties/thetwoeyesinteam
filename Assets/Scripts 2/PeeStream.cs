using UnityEngine;
using System.Collections.Generic;

public class PeeStream : MonoBehaviour {
	
	public int maxParticles;
	public float particleVelocity;
	public float emissionSpeed;

	private float emissionWaitTimer;
	public int particleCount;
	public Transform originalParticle;
	public GameObject target;
	public List<PeeParticle> pee;
	public int listSize;
	public PeeParticle temp;

	// Use this for initialization
	void Start () {
		//Change Foreground to the layer you want it to display on
		//You could prob. make a public variable for this
		emissionWaitTimer = 0f;
		particleCount = 0;
		originalParticle = transform.Find("PeeParticle");
		target = GameObject.Find("Cursor");
		pee = new List<PeeParticle>();

	}
	
	// Update is called once per frame
	void Update () {
		if (particleCount < maxParticles) {
			addParticle();
		}
		if(pee.Count > 2){
			popFromQueue();
			popFromQueue();
		}
		listSize = pee.Count;

		temp = (PeeParticle) originalParticle.GetComponent (typeof(PeeParticle));
		temp.setOriginality();
	}

	void addParticle() {
		Transform newPeeParticle;
		newPeeParticle = (Transform) Instantiate(originalParticle);
		PeeParticle pp = (PeeParticle) newPeeParticle.GetComponent(typeof(PeeParticle));
		Vector2 pos;
		pos.x = transform.position.x;
		pos.y = transform.position.y;
		pp.setPosition(pos);
		pp.correctRotation();

		//pee.Add(newPeeParticle);
		particleCount++;
	}

	public void addToQueue(PeeParticle pp) {
		pee.Add(pp);
	}

	public void popFromQueue() {
		pee[0].setActive();
		pee[0].getTarget();
		pee.Remove(pee[0]);
	}

}
