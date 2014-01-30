using UnityEngine;
using System.Collections;

public class PeeStream : MonoBehaviour {

	public SpriteRenderer SR;
	public ParticleSystem PS;

	// Use this for initialization
	void Start () {
		//Change Foreground to the layer you want it to display on
		//You could prob. make a public variable for this
		particleSystem.renderer.sortingLayerName = "Player";
		SR = transform.Find("PeeStream").GetComponent<SpriteRenderer>();
		PS = transform.Find("PeeStream").GetComponents<ParticleSystem>()[1];
	}
	
	// Update is called once per frame
	void Update () {
		//if (SR != null && PS != null) {
		//	for(int i = 0; i < PS.GetParticles(); i++) {

	}
}
