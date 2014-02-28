using UnityEngine;
using System.Collections;

public class AudioManager : MonoBehaviour {

	// gameObjects that play audio, assigned in Unity Editor
	public GameObject background;
	public GameObject background2;
	public GameObject checkmark;
	public GameObject focusTimer;
	public bool focusTimerIsPlaying;
	public GameObject cross;
	public GameObject player;

	AudioSource[] playerVO; //all of the VO in the player object
	AudioSource[] stevenFindsCorrectObject;
	AudioSource[] stevenFindsIncorrectObject;
	AudioSource[] stevenLooksAtObject;

	void Start () {

		playerVO = player.GetComponents<AudioSource>();
		assignVOBanks();

		//audio sources should be in the gameObject themselves, or in here?
		//one way to do it:
		//		have all the VO go to the player
		//		have all the feedback sounds go to the UI
		//		have the ambiance stick to the background
		//		
		//playing audio:
		//		if moments are composed of multiple sounds, then have functions
		//		call them that are placed in here, like "playFoundObject()".
		//		the sources will be in the objects, but controlling when they do
		//		or don't play, and thier timing will be easier in here

		//begin playing the background ambiance
		background.audio.Play();
		focusTimerIsPlaying = false;
	}


	//must be careful when doing this!
	//order of items in playerVO dependent on their placement in the editor!
	void assignVOBanks(){
		
		stevenFindsCorrectObject = new AudioSource[2];
		stevenFindsCorrectObject[0] = playerVO[0];
		stevenFindsCorrectObject[1] = playerVO[1];
		
		stevenFindsIncorrectObject = new AudioSource[2];
		stevenFindsIncorrectObject[0] = playerVO[2];
		stevenFindsIncorrectObject[1] = playerVO[3];

		stevenLooksAtObject = new AudioSource[2];
		stevenLooksAtObject[0] = playerVO[4];
		stevenLooksAtObject[1] = playerVO[5];
	}


	//TODO: Lark suggests these functions should also be able to pass in a sound
	//that we want to play, which is probably a good idea as well
	public void playFoundCorrectObject(){
		checkmark.audio.Play();
	
		//random index
		int i = Random.Range(0, stevenFindsCorrectObject.Length);

		//play the random VO clip after the checkmark sound plays
		float d = checkmark.audio.clip.length;
		stevenFindsCorrectObject[i].PlayDelayed(d * 0.33f);
	}


	public void playFoundIncorrectObject(){
		cross.audio.Play();

		//random index
		int i = Random.Range(0, stevenFindsIncorrectObject.Length);

		//play the random VO clip after the cross sound plays
		float d = cross.audio.clip.length;
		stevenFindsIncorrectObject[i].PlayDelayed(d * 0.33f);

	}


	public void playLookingAtObject(){
		int i = Random.Range(0, stevenLooksAtObject.Length);
		stevenLooksAtObject[i].Play();
	}


	// probably should cange this to a fadein/fadeout, since this function assumes that
	// both audio files have the same volume
	public void changeAmbiance(GameObject oldBackground, GameObject newBackground){

		//set the new background ambiance volume to 0 and begin playing
		oldBackground.audio.Stop();
		newBackground.audio.Play();

	}


	void Update () {
	}
}



