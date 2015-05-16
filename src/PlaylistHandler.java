import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class PlaylistHandler {

	private DocumentBuilderFactory docFact;
	private TransformerFactory transFact;
	private XPathFactory xPathFact;
	private String xmlfile;

	public PlaylistHandler(){
		this.docFact = DocumentBuilderFactory.newInstance();
		this.transFact = TransformerFactory.newInstance();
		this.xPathFact = XPathFactory.newInstance();
	}
	public PlaylistHandler(String path){
		this.docFact = DocumentBuilderFactory.newInstance();
		this.transFact = TransformerFactory.newInstance();
		this.xPathFact = XPathFactory.newInstance();
		this.xmlfile = path;
	}
	public void setXMLSource(String path){
		this.xmlfile = path;
	}

	private Document docParser(){
		try{
			File file = new File(this.xmlfile);
			DocumentBuilder docBuild = this.docFact.newDocumentBuilder();
			Document document = docBuild.parse(file);
			return document;
		}
		catch (Exception e){
			return null;
		}
	}
	private void transforming(Document document){
		try{
			Transformer transformer = this.transFact.newTransformer();
			StreamResult result = new StreamResult(new FileOutputStream(this.xmlfile));
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
		}
		catch (Exception e){
		}
	}
	private String[] xpathSearch(String expr){
		try{
			Document document = docParser();
			XPath xPath = this.xPathFact.newXPath();
			NodeList nodeList = (NodeList) xPath.compile(expr).evaluate(document, XPathConstants.NODESET);
			String[] result = new String[nodeList.getLength()];
			for (int i = 0; i < nodeList.getLength(); i++) {
				result[i] = nodeList.item(i).getFirstChild().getNodeValue();
			}
			return result;
		}
		catch (Exception e){
			return null;
		}
	}
	public String[] getMediaPaths(String name){
		String expr = "/playlists/playlist[name='"+name+"']/media";
		String[] songPaths = xpathSearch(expr);
		return songPaths;
	}
	public String[] getPlaylists(){
		String expr = "/playlists/playlist/name";
		String[] playlists = xpathSearch(expr);
		return playlists;
	}
	public void newPlaylist(String plName){
		//Creates new playlist.
		//If playlist already exists, prints to console and returns without changes.
		Document document = docParser();
		String expr = "/playlists/playlist[name='"+plName+"']";
		String[] prevPL = xpathSearch(expr); //Should be empty.
		if (prevPL.length==0){
			//System.out.println("celebrate on github with the slaskarna");
			Element plElem = document.createElement("playlist");
			Element nameElem = document.createElement("name");
			nameElem.setTextContent(plName);
			plElem.appendChild(nameElem);
			Element root = document.getDocumentElement();
			root.appendChild(plElem);
			transforming(document);
		}
		else{
			//System.out.println("prevPL.length = "+prevPL.length);
			System.out.println("Failed to create playlist, name "+plName+" already taken. ");
			return;
		}
	}
//	public void addMedia(String plName, String newTrack){
//		//TODO Perhaps
//		System.out.println("Perhaps make it simple to add just a single track");
//	}
	public void addMedia(String plName, String[] newTracks){
		try{
			Document document = docParser();
			String expr = "/playlists/playlist[name='"+plName+"']";
			XPath xPath = this.xPathFact.newXPath();
			NodeList nodeList = (NodeList) xPath.compile(expr).evaluate(document, XPathConstants.NODESET);
			int nodeLength = nodeList.getLength();
			int amountToAdd = newTracks.length;
			if(nodeLength==1){
				for (int i=0;i<amountToAdd;i++){
					Element trackPath = document.createElement("media");
					trackPath.setTextContent(newTracks[i]);
					nodeList.item(0).appendChild(trackPath);
					transforming(document);
				}
			}
			else if(nodeLength==0){
				System.out.println("No playlist with name "+plName+".");
			}
			else{
				System.out.println("Duplicate playlists with name "+plName+".");
			}
		}
		catch (Exception e){
			return;
		}
	}
	public void removeMedia(String plName, String mediaName){
		try{
			Document document = docParser();
			String expr = "/playlists/playlist[name='"+plName+"']/media";
			XPath xPath = this.xPathFact.newXPath();
			NodeList nodeList = (NodeList) xPath.compile(expr).evaluate(document, XPathConstants.NODESET);
			//Continuation depends on database buildup
		}
		catch (Exception e){
			return;
		}
		//TODO
	}
	public void deletePlaylist(String plName){
		try{
			Document document = docParser();
			String expr = "/playlists/playlist[name='"+plName+"']";
			XPath xPath = this.xPathFact.newXPath();
			NodeList nodeList = (NodeList) xPath.compile(expr).evaluate(document,XPathConstants.NODESET);
			if (nodeList.getLength()==1){
				System.out.println("noden finns och det finns bara den");
				Node elem = nodeList.item(0);
				System.out.println("name of selected element    "+elem.getNodeName());
				elem.getParentNode().removeChild(elem);
				transforming(document);
			}
		}
		catch (Exception e){
			return;
		}
	}

}







