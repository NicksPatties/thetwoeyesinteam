using UnityEngine;
using System.Collections;

public class FaceColorChangingScript : MonoBehaviour {

	public float outwardAppearance;
	public bool testingEnabled;

	void Start () {
		outwardAppearance = 100;
		testingEnabled = false;
	}

	// changes the color of Steven's face based on his outward appearance,
	// the less outward appearance, the more red he gets
	void setFaceColor(){
		SpriteRenderer renderer = GetComponent<SpriteRenderer>();
		float newFaceColor = outwardAppearance/100;
		renderer.color = new Color(1f, newFaceColor, newFaceColor, 1f);
	}


	void Update () {
		if(Input.GetKeyDown(KeyCode.Space) && testingEnabled){
			outwardAppearance -= 20;
			if(outwardAppearance < 0){
				outwardAppearance = 100;
			}
			setFaceColor ();
		}
	}
}
