package com.example.movieslam_android_dev.views;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.movieslam_android_dev.R;
import com.example.movieslam_android_dev.models.TempModel;
import com.example.movieslam_android_dev.tools.ResponseDelegate;
import com.example.movieslam_android_dev.tools.XmlRequestHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ReadyToPlayPage extends Activity implements ResponseDelegate{

	private Thread thread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.readytoplay_page);
        
        thread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(1000);
                        Intent intent = new Intent();
            			intent.setClass(ReadyToPlayPage.this, GamePlayPage.class);
            			ReadyToPlayPage.this.startActivity(intent);
                    }
                }
                catch(InterruptedException ex){                    
                }

                // TODO              
            }
        };
		
		new XmlRequestHandler(this,"http://screenslam.foxfilm.com/service/getMedia.php?&type=drama&user_id=8").execute();
        
	}

	@Override
	public void responseLoaded(String response) {
		// could use regx for better result
		response = response.replace("<?xml version=1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\n<?xml version=\"1.0\"?>\n","");
		
		Document doc;
		String[] questions = new String[5];
		String[] mediaURLs = new String[5];
		String[] mediaLegals = new String[5];
		String[] mediaNames = new String[5];
		String[] mediaIDs = new String[5];
		String[] mediaEtailers = new String[5];
		String[][] anwsers = new String[5][4];
		
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
				
				mediaEtailers[i] = ((Node) mediaEtailerList.item(0)).getNodeValue();
				questions[i] = ((Node) questionList.item(0)).getNodeValue();
				mediaURLs[i] = ((Node) mediaUrlList.item(0)).getNodeValue();
				mediaLegals[i] = ((Node) mediaLegalList.item(0)).getNodeValue();
				mediaNames[i] = ((Node) mediaNameList.item(0)).getNodeValue();
				mediaIDs[i] = ((Node) mediaIDList.item(0)).getNodeValue();
				
				System.out.println("movie question "+i+": "+questions[i]);
			}
			
			
			//set properties for model
			TempModel.setQuestion(questions);
			TempModel.setAnswers(anwsers);
			TempModel.setMediaEtailers(mediaEtailers);
			TempModel.setMediaURLs(mediaURLs);
			TempModel.setMediaLegals(mediaURLs);
			TempModel.setMediaNames(mediaNames);
			TempModel.setMediaIDs(mediaIDs);
			
			thread.start();
			
		} catch (Exception e) {
			Log.d("debug", "Exception");
		}
	}
}
