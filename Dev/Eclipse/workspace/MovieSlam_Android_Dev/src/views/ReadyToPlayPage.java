package views;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import models.Config;
import models.Gameplay;
import models.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import tools.AdvElement;
import tools.AdvResponseDelegate;
import tools.AdvRequestHandler;
import tools.DownloadImageTask;

import com.example.movieslam_android_dev.R;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ReadyToPlayPage extends Activity implements AdvResponseDelegate, Config{

	private TextView genreText;
	private TextView userNameText;
	private TextView oppoNameText;
	
	private Thread thread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.readytoplay_page);
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.readytoplayPage);
		ll.setBackgroundResource(R.drawable.genre_logo_and_copy);
		
		String g = Gameplay.getGenre();
        if (g.equals("all")){
        	ll.setBackgroundResource(R.drawable.genre_random_italian);
        }else if(g.equals("promo")){
        	g = Gameplay.get_promo_name();
        }else if(g.equals("scifi")){
        	ll.setBackgroundResource(R.drawable.genre_scifi_italian);
        }else if(g.equals("drama")){
        	ll.setBackgroundResource(R.drawable.genre_drama_italian);
        }else if(g.equals("family")){
        	ll.setBackgroundResource(R.drawable.genre_family_italian);
        }else if(g.equals("comedy")){
        	ll.setBackgroundResource(R.drawable.genre_comedy_italian);
        }else if(g.equals("action")){
        	ll.setBackgroundResource(R.drawable.genre_action_italian);
        }else if(g.equals("horror")){
        	ll.setBackgroundResource(R.drawable.genre_horror_italian);
        }else{
        	ll.setBackgroundResource(R.drawable.genre_logo_and_copy);
        }
        
        String api;
        if (Gameplay.getChallType().equals("self")){
//        	Boolean b = Gameplay.getChallOppoID().equals("");
//        	if (!b){
        	if (!Gameplay.getChallOppoID().equals("")){
//        	if (!Gameplay.getChallOppoID().isEmpty()){
        		api = BASE_URL+ "/service/getMedia.php?by=uid"
        				+"&type="+Gameplay.getGenre()
        				+"&user_id="+User.get_uid()
        				+"&target="+Gameplay.getChallOppoID();
        	
        	}else if (!Gameplay.getChallOppoFID().equals("")){
        		api = BASE_URL+ "/service/getMedia.php?by=fb"
        				+"&type="+Gameplay.getGenre()
        				+"&user_id="+User.get_uid()
        				+"&target="+Gameplay.getChallOppoFID()
        				+"&target_fname="+Gameplay.getOppoFName()
        				+"&target_lname="+Gameplay.getOppoLName();
        	}else{
        		api = BASE_URL+ "/service/getMedia.php?"
        				+"&type="+Gameplay.getGenre()
        				+"&user_id="+User.get_uid();
        	}
        }else{
        	api = BASE_URL+ "/service/getChallenge.php?"
    				+"&challenge_id="+Gameplay.getChallID();
        }
//      new XmlRequestHandler(this, BASE_URL+"/service/getMedia.php?&type=drama&user_id=8").execute();
        new AdvRequestHandler(this, api).execute();
        thread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(3000);
                        startActivity(new Intent(getApplicationContext(), GamePlayPage.class));
                    }
                }
                catch(InterruptedException ex){                    
                }
            }
        };
	}

	@Override
	public void responseLoaded(String response) {
		// could use regx for better result
		if (Gameplay.getChallType().equals("self")){
			response = response.replace("<?xml version=1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\n<?xml version=\"1.0\"?>\n","");
        }else{
        	response = response.replace("<?xml version=\"1.0\"?>\n","");
        }
		
		Document doc;
		String[] questions = new String[5];
		String[] mediaURLs = new String[5];
		String[] mediaLegals = new String[5];
		String[] mediaNames = new String[5];
		String[] mediaIDs = new String[5];
		String[] mediaEtailers = new String[5];
		String[][] anwsers = new String[5][4];
		String[] mediaTN = new String[5];
		
		AdvElement ade = new AdvElement(response);
		AdvElement opponent = ade.getElement("player");
		Gameplay.setChallOppoID(opponent.getValue("player_user_id"));
		Gameplay.setChallOppoFID(opponent.getValue("player_user_fid"));
		Gameplay.setOppoFName(opponent.getValue("player_user_fname"));
		Gameplay.setOppoLName(opponent.getValue("player_user_lname"));
		Gameplay.setOppoImageURL(opponent.getValue("player_user_thumbnail"));
		Gameplay.setOppoScore(opponent.getValue("player_user_score"));
		
		if (Gameplay.getChallType().equals("challenge")){
			Gameplay.oppoScoreThisGame = Integer.parseInt(opponent.getValue("player_game_score"));
        }
		
		try {
			for (int i=0; i<5; i++){
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response)));
				doc.getDocumentElement().normalize();
				
				NodeList nodeList = doc.getElementsByTagName("media");
				Node mediaNode = nodeList.item(i);
				
				NodeList mediaUrlList = ((Element)mediaNode).getElementsByTagName("media_url");
				Element mediaUrlElement = (Element) mediaUrlList.item(0);
				mediaUrlList = mediaUrlElement.getChildNodes();
				
				NodeList mediaNameList = ((Element)mediaNode).getElementsByTagName("media_name");
				Element mediaNameElement = (Element) mediaNameList.item(0);
				mediaNameList = mediaNameElement.getChildNodes();
				
				NodeList mediaIDList = ((Element)mediaNode).getElementsByTagName("media_id");
				Element mediaIDElement = (Element) mediaIDList.item(0);
				mediaIDList = mediaIDElement.getChildNodes();
				
				NodeList mediaLegalList = ((Element)mediaNode).getElementsByTagName("media_legal");
				Element mediaLegalElement = (Element) mediaLegalList.item(0);
				mediaLegalList = mediaLegalElement.getChildNodes();
				
				NodeList questionList = ((Element)mediaNode).getElementsByTagName("choice_var");
				Element questionElement = (Element) questionList.item(0);
				questionList = questionElement.getChildNodes();
				
				NodeList mediaEtailerList = ((Element)mediaNode).getElementsByTagName("media_etailer");
				Element mediaEtailerElement = (Element) mediaEtailerList.item(0);
				mediaEtailerList = mediaEtailerElement.getChildNodes();
				
				NodeList mediaTNList = ((Element)mediaNode).getElementsByTagName("media_thumbnail");
				Element mediaTNElement = (Element) mediaTNList.item(0);
				mediaTNList = mediaTNElement.getChildNodes();
				
				NodeList anwsersList = ((Element)mediaNode).getElementsByTagName("choice_value");
				Element anwsersElement0 = (Element) anwsersList.item(0);
				Element anwsersElement1 = (Element) anwsersList.item(1);
				Element anwsersElement2 = (Element) anwsersList.item(2);
				Element anwsersElement3 = (Element) anwsersList.item(3);
				NodeList anwsersList0 = anwsersElement0.getChildNodes();
				NodeList anwsersList1 = anwsersElement1.getChildNodes();
				NodeList anwsersList2 = anwsersElement2.getChildNodes();
				NodeList anwsersList3 = anwsersElement3.getChildNodes();
				anwsers[i][0] = ((Node) anwsersList0.item(0)).getNodeValue();
				anwsers[i][1] = ((Node) anwsersList1.item(0)).getNodeValue();
				anwsers[i][2] = ((Node) anwsersList2.item(0)).getNodeValue();
				anwsers[i][3] = ((Node) anwsersList3.item(0)).getNodeValue();
				
				questions[i] = ((Node) questionList.item(0)).getNodeValue();
				mediaURLs[i] = ((Node) mediaUrlList.item(0)).getNodeValue();
				mediaLegals[i] = ((Node) mediaLegalList.item(0)).getNodeValue();
				mediaNames[i] = ((Node) mediaNameList.item(0)).getNodeValue();
				mediaIDs[i] = ((Node) mediaIDList.item(0)).getNodeValue();
				mediaEtailers[i] = ((Node) mediaEtailerList.item(0)).getNodeValue();
				mediaTN[i] = ((Node) mediaTNList.item(0)).getNodeValue();
				
				System.out.println("movie question "+i+": "+questions[i]);
			}
			
			//set properties for model
			Gameplay.setQuestion(questions);
			Gameplay.setAnswers(anwsers);
			Gameplay.setMediaEtailers(mediaEtailers);
			Gameplay.setMediaURLs(mediaURLs);
			Gameplay.setMediaLegals(mediaURLs);
			Gameplay.setMediaNames(mediaNames);
			Gameplay.setMediaTN(mediaTN);
			Gameplay.setMediaIDs(mediaIDs);
			
//	        switch (g) {
//            case "all":  
//            		 ll.setBackgroundResource(R.drawable.genre_random_italian);
//                     break;
//            case "scifi":  
//            		 ll.setBackgroundResource(R.drawable.genre_scifi_italian);
//                     break;
//            case "drama":
//            		 ll.setBackgroundResource(R.drawable.genre_drama_italian);
//            		 break;
//		    case "comedy":  
//		    		 ll.setBackgroundResource(R.drawable.genre_comedy_italian);
//		             break;
//		    case "family":  
//		    		 ll.setBackgroundResource(R.drawable.genre_family_italian);
//		             break;
//		    case "action":  
//		    		 ll.setBackgroundResource(R.drawable.genre_action_italian);
//		             break;
//		    case "horror": 
//		    		 ll.setBackgroundResource(R.drawable.genre_horror_italian);
//		             break;
//		    default: 
//			   		 ll.setBackgroundResource(R.drawable.genre_logo_and_copy);
//		             break;
			
			LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    TableLayout result_table = (TableLayout) findViewById(R.id.ready_table);
		    
		    View ready_cell = layoutInflater.inflate(R.layout.ready_info_cell, result_table, false);
	     	result_table.addView(ready_cell);
	     	
	     	TextView s = (TextView) ready_cell.findViewById(R.id.names);
	     	s.setTextColor(Color.parseColor("#1f426e"));
	     	String userFName = "Lana";
	     	String userLName = "Oskoui";
	     	String userName = userFName+" "+userLName;
	     	String offset1 = "";
	     	if (userName.length() <= 52){
	     		for (int i = 0; i < 52-userName.length(); i++) {
	    			offset1 += " "; 
	    		}
	     	}
	     	userName = "    "+userName+offset1;
	     	String oppoFName = "App";
	     	String oppoLName = "Oskoui";
	     	String oppoName = "    "+oppoFName+" "+oppoLName;
	     	s.setText(userName+oppoName);
	     	
//	     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.user_image)).execute("http://screenslam.foxfilm.com/image/title_thumbnail_default.jpg");
	     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.user_image_ready)).execute(User.get_thumbnail());
	     	
	     	TextView s1 = (TextView) ready_cell.findViewById(R.id.user_point_ready);
	     	s1.setTextColor(Color.parseColor("#ffffff"));
	     	s1.setText("10000\n"+"Points");
	     	
	     	if (Gameplay.getOppoImageURL().equals("")){
	     		new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image_ready)).execute(Gameplay.getOppoImageURL());
	     	}else{
	     		new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image_ready)).execute(Gameplay.getOppoImageURL());
	     	}
//	     	new DownloadImageTask((ImageView) ready_cell.findViewById(R.id.oppo_image)).execute("https://graph.facebook.com/100002538660677/picture");
	     	
	     	TextView s2 = (TextView) ready_cell.findViewById(R.id.oppo_point_ready);
	     	s2.setTextColor(Color.parseColor("#ffffff"));
	     	s2.setText("20000\n"+"Points");
			
			thread.start();
			
		} catch (Exception e) {
			Log.d("debug", "Exception");
		}
	}
}
