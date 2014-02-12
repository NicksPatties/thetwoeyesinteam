using UnityEngine;
using System.Collections;
using SimpleJSON;

public class JSONReader : MonoBehaviour {

	public WWW www;
	private int level = 3;
	private string jsonString;
	public bool loaded = false;
	public ActionList[] actionList = new ActionList[5];

	// Use this for initialization
	void Start () {

		/**what we print:
		 **print("-----------"+Application.dataPath+"-----------");
		 /Users/baneDealer/Dev/Unity/GitHub/thetwoeyesinteam/Assets
		 **/
		www = new WWW("file://"+Application.dataPath+"/ChaptersData/chapter"+level+".json");
		//var jsonData = JSON.Parse(www.text);
		//string jsonString = jsonData;
		//JsonData chapterData = JsonMapper.ToObject(www);
		//print("-----------"+w.data+"-----------");
	}
	
	// Update is called once per frame
	void Update () {
		if (www.isDone&&loaded == false)
		{
			var jsonData = JSON.Parse(www.text);
			ActionList al;
			for(int i = 0; i<jsonData["actions"].Count; i++){
				al = new ActionList();
				string[] objects = new string[jsonData["actions"][i]["objects"].Count+1];
				for(int j = 0; j<jsonData["actions"][i]["objects"].Count; j++){
					objects[j] = jsonData["actions"][i]["objects"][j].Value;
					//al.targetObjects[j] = objects[j];
				}
				al.targetObjects = objects;
				al.actionName = jsonData["actions"][i]["type"].Value;
			}
			loaded = true;
			//print("-----------111"+jsonData["actions"][0]["type"].Value+"-----------");
			//string jsonString = jsonData;
			//print("-----------222"+jsonString+"-----------");
		}
	}
}
