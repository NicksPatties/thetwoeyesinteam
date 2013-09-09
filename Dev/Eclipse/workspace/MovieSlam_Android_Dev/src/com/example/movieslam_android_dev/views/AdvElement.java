package com.example.movieslam_android_dev.views;
import java.io.IOException;
import java.io.StringReader;

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
			doc_s = doc_s.replace("\n<?xml version=\"1.0\"?>\n", "");
			_document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(doc_s)));
			_document.getDocumentElement().normalize();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}		
	}

	public AdvElement getElement(String tag_s) {	
		return getElement(tag_s, 0);
	}
	
	public AdvElement getElement(String tag_s, int idx) {
		Element e;
		if (_document == null){
			e = (Element) _element.getElementsByTagName(tag_s).item(idx);
		}else{
			e = (Element) _document.getElementsByTagName(tag_s).item(idx);
		}		
		return new AdvElement(e);
	}
	
	public String getValue(String tag_s){
		String r;
		if (_document == null){
			r = _element.getElementsByTagName(tag_s).item(0).getChildNodes().item(0).getNodeValue();
		}else{
			r = _document.getElementsByTagName(tag_s).item(0).getChildNodes().item(0).getNodeValue();
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
