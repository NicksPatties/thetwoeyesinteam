using UnityEngine;
using System.Collections.Generic;

public class PeeStream : MonoBehaviour {
	
	public int maxParticles;
	public float particleVelocity;
	public float emissionSpeed;
	public int particleDensity;
	public Sprite[] peeSprites;
	public Sprite[] peeSprites2;
	public bool circularParticle;
	
	public int particleCount;
	private Transform originalParticle;
	private GameObject target;
	private List<PeeParticle> pee;
	public int listSize;
	private PeeParticle originalPee;

	private SpriteRenderer sr;
	
	// Use this for initialization
	void Start () {
		//Change Foreground to the layer you want it to display on
		//You could prob. make a public variable for this
		particleCount = 0;
		originalParticle = transform.Find("PeeParticle");
		target = GameObject.Find("Cursor");
		pee = new List<PeeParticle>();
	}
	
	// Update is called once per frame
	void Update () {
		if (particleCount < maxParticles && pee.Count < particleDensity) {
			addParticle();
		}

		if(pee.Count > particleDensity) {
			for(int i=0; i<particleDensity; i++) {
				popFromQueue();
			}
		}
		listSize = pee.Count;

		originalPee = (PeeParticle) originalParticle.GetComponent (typeof(PeeParticle));
		originalPee.setOriginality();
	}

	void addParticle() {
		Transform newPeeParticle = (Transform) Instantiate(originalParticle);
		PeeParticle pp = (PeeParticle) newPeeParticle.GetComponent(typeof(PeeParticle));
		Vector2 pos;
		pos.x = transform.position.x;
		pos.y = transform.position.y;
		pp.setPosition(pos);
		pp.correctRotation();

		sr = (SpriteRenderer) newPeeParticle.GetComponent(typeof(SpriteRenderer));
		if(circularParticle)
			sr.sprite = peeSprites[(int) (Random.value*peeSprites.Length)];
		else
			sr.sprite = peeSprites2[(int) (Random.value*peeSprites2.Length)];

		particleCount++;
	}

	public void addToQueue(PeeParticle pp) {
		pee.Add(pp);
	}

	public void popFromQueue() {
		pee[0].getTarget();
		pee[0].setActive();
		pee.Remove(pee[0]);
	}

}
