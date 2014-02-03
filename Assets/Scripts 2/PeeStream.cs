using UnityEngine;
using System.Collections.Generic;

public class PeeStream : MonoBehaviour {
	
	public int maxParticles;
	public float particleVelocity;
	public Vector2 vel;
	
	private int particleCount;
	private Transform peeParticle;
	private Transform target;
	private List<Transform> pee;

	// Use this for initialization
	void Start () {
		//Change Foreground to the layer you want it to display on
		//You could prob. make a public variable for this
		particleCount = 0;
		peeParticle = transform.Find("PeeParticle");
		target = transform.Find ("Cursor");
		pee = new List<Transform>();
	}
	
	// Update is called once per frame
	void Update () {
		if (particleCount < maxParticles) {
			addParticle();
		}
		//foreach (Transform piss in pee) {

		//}
	}

	void addParticle() {
		Transform newPeeParticle;
		newPeeParticle = (Transform) Instantiate(peeParticle);
		PeeParticle pp = (PeeParticle) newPeeParticle.GetComponent(typeof(PeeParticle));
		Vector2 pos;
		pos.x = peeParticle.transform.position.x;
		pos.y = peeParticle.transform.position.y;
		pp.setPosition(pos);
		vel.x = 0.01f;
		vel.y = 0.01f;
		pp.setVelocity(vel);
		pp.correctRotation();

		pee.Add(newPeeParticle);
		particleCount++;
	}
}
