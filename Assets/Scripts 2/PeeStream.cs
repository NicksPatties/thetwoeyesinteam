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
	private List<Transform> pee;

	// Use this for initialization
	void Start () {
		//Change Foreground to the layer you want it to display on
		//You could prob. make a public variable for this
		emissionWaitTimer = 0f;
		particleCount = 0;
		originalParticle = transform.Find("PeeParticle");
		target = GameObject.Find("Cursor");
		pee = new List<Transform>();
	}
	
	// Update is called once per frame
	void Update () {
		if (particleCount < maxParticles) {
			addParticle();
		}
		PeeParticle temp = (PeeParticle) originalParticle.GetComponent (typeof(PeeParticle));
		temp.setOriginality();

		foreach (Transform piss in pee) {
			PeeParticle pp = (PeeParticle) piss.GetComponent(typeof(PeeParticle));
			/*if (pp.maxVelocity == 0)
				pp.maxVelocity = particleVelocity;
			if (pp.targetPosition.x == 0 && pp.targetPosition.y == 0) {
				pp.targetPosition.x = target.transform.position.x;
				pp.targetPosition.y = target.transform.position.y;
			}*/
		}
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

		pee.Add(newPeeParticle);
		particleCount++;
	}
}
