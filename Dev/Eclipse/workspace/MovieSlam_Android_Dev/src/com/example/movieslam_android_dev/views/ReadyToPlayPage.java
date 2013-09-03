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
import com.example.movieslam_android_dev.views.SplashPage.MyButtonLisiener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ReadyToPlayPage extends Activity  implements ResponseDelegate, ImgRequestDelegate{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.readytoplay_page);
        
 
        XmlRequestHandler xrh = new XmlRequestHandler();
        xrh.delegate = this;
        // hardcode api to test
		xrh.setURL("http://postpcmarketing.com/movieslam/intl/it/service/getGameInfo.php?user_id=0&fid=100000855108534");
		xrh.execute();
        
	}

	@Override
	public void responseLoaded(String response) {
		// could use regx for better result
		response = response.replace("\n<?xml version=\"1.0\"?>\n", "");
		
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response)));
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("media");
			Node mediaNode = nodeList.item(0);
			
			NodeList mediaUrlList = ((Element)mediaNode).getElementsByTagName("media_url");
			Element mediaUrlElement = (Element) mediaUrlList.item(0);
			mediaUrlList = mediaUrlElement.getChildNodes();
			
			NodeList mediaNameList = ((Element)mediaNode).getElementsByTagName("media_name");
			Element mediaNameElement = (Element) mediaNameList.item(0);
			mediaNameList = mediaNameElement.getChildNodes();
			
			NodeList mediaLegalList = ((Element)mediaNode).getElementsByTagName("media_legal");
			Element mediaLegalElement = (Element) mediaLegalList.item(0);
			mediaLegalList = mediaLegalElement.getChildNodes();
			
			NodeList questionList = ((Element)mediaNode).getElementsByTagName("choice_var");
			Element questionElement = (Element) questionList.item(0);
			questionList = questionElement.getChildNodes();
			
			String[] questions = new String[5];
			
//			TextView userName_txt = (TextView)findViewById(R.id.userName_txt); 
//			userName_txt.setText(((Node) userFnameList.item(0)).getNodeValue() + " " + ((Node) userLnameList.item(0)).getNodeValue());
//			
//			TextView userID_txt = (TextView)findViewById(R.id.userID_txt); 
//			userID_txt.setText(((Node) userIDList.item(0)).getNodeValue());
//			
//			TextView userScore_txt = (TextView)findViewById(R.id.userScore_txt); 
//			userScore_txt.setText(((Node) userScoreList.item(0)).getNodeValue());
			
			// get img from URL using another thread
//			ImgRequestHandler irh = new ImgRequestHandler();
//			irh.delegate = this;
//			irh.setURL(((Node) userThumbnailList.item(0)).getNodeValue());
//			irh.execute();       
			
		} catch (Exception e) {
			Log.d("debug", "Exception");
		}
	}

	@Override
	public void imgLoaded(Bitmap bitmap) {
		// TODO Auto-generated method stub
		
	}
}
