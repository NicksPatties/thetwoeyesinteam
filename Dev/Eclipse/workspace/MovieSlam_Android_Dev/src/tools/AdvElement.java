package tools;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;


public class AdvElement {
	private Document _document = null;
	private Element _element = null;
	
	public AdvElement(Element element){
		_element = element;
	}
	
	public AdvElement(String doc_s){
		try {
			// ignore unnecessary xml tags that might break the structure
			Pattern pattern = Pattern.compile("<responseData>(.*?)</responseData>");
			Matcher matcher = pattern.matcher(doc_s);
			if (matcher.find()){
				doc_s = "<responseData>"+matcher.group(1)+"</responseData>";
			}else{
				Log.d("debug", "parsing error");
			}
			// build xml parser element
			_document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(doc_s)));
			_document.getDocumentElement().normalize();
		} catch (SAXException e) {
			Log.e("AdvElement", "AdvElement init Error: SAXException");
		} catch (IOException e) {
			Log.e("AdvElement", "AdvElement init Error: IOException");
		} catch (ParserConfigurationException e) {
			Log.e("AdvElement", "AdvElement init Error: ParserConfigurationException");
		} catch (Exception e){
			Log.e("AdvElement", "AdvElement init Error: Source is not valid");
		}
	}

	public AdvElement getElement(String tag_s) {	
		return getElement(tag_s, 0);
	}
	
	public AdvElement getElement(String tag_s, int idx) {
		Element e;
		try {
			if (_document == null){
				e = (Element) _element.getElementsByTagName(tag_s).item(idx);
			}else{
				e = (Element) _document.getElementsByTagName(tag_s).item(idx);
			}
		} catch (NullPointerException expt){
			Log.e("AdvElement", "AdvElement getElement Error: "+tag_s);
			return null;
		}
		return new AdvElement(e);
	}
	
	public String getValue(String tag_s){
		
		return getValue(tag_s, 0);
	}
	
	public String getValue(String tag_s, int idx){
		String r;
		try {
			if (_document == null){
				r = _element.getElementsByTagName(tag_s).item(idx).getChildNodes().item(0).getNodeValue();
			}else{
				r = _document.getElementsByTagName(tag_s).item(idx).getChildNodes().item(0).getNodeValue();
			}
		} catch (NullPointerException expt){
			Log.e("AdvElement", "AdvElement getValue Error: "+tag_s);
			return "0";
		}
		return r;
	}

	public int getElementLength(String tag_s) {
		int l;
		if (_document == null){
			l = _element.getElementsByTagName(tag_s).getLength();
		}else{
			l = _document.getElementsByTagName(tag_s).getLength();
		}	
		return l;
	}

}
