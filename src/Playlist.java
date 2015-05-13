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
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Playlist {
	
	
	private static String xmlfile;
	
	public void setXMLSource(String path){
		xmlfile = path;
	}
	private static Document docParser(){
		try{
			File file = new File(xmlfile);
			DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docFact.newDocumentBuilder();
			Document document = docBuild.parse(file);
			return document;
		}
		catch (Exception e){
			return null;
		}
	}
	private static void transforming(Document document){
		try{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(new FileOutputStream("/Users/Samuel/Documents/EclipseWS/AndinMP/src/playlists.xml"));
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
		}
		catch (Exception e){
		}
	}

	private static String[] xpathSearch(String expr){
		try{
			Document document = docParser();
			XPath xPath =  XPathFactory.newInstance().newXPath();
			System.out.println(expr);
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
	public static String[] getMediaPaths(String name){
		String expr = "/playlists/playlist[name='"+name+"']/media";
		String[] songPaths = xpathSearch(expr);
		return songPaths;
	}
	public static String[] getPlaylists(){
		String expr = "/playlists/playlist/name";
		String[] playlists = xpathSearch(expr);
		return playlists;
	}
	public static void newPlaylist(String plName){
		//Make sure no duplicates are created!!
		Document document = docParser();
		Element newElement = document.createElement("playlist");
		newElement.setTextContent(plName);
		Element root = document.getDocumentElement();
		root.appendChild(newElement);

		transforming(document);
		//TODO
	}
	public static void addMedia(String playlist, String newSong){
		Document document = docParser();
		
		NodeList plElement = document.getElementsByTagName("playlist");   //IF plElement exists, else do something else. Try stuff.
		
		for (int i=0; i <plElement.getLength();i++){
			Node node = plElement.item(i); //Fadäs??
			System.out.println(node.getChildNodes().toString());
			System.out.println(node.getNodeName());
			Node chNodes = node.getLastChild();

			Element newElement = document.createElement("media");// Element to be inserted
			newElement.setTextContent(newSong);
			chNodes.getParentNode().insertBefore(newElement, chNodes.getNextSibling());
		}
		transforming(document);

	}
	public static void removeMedia(String name){
		//TODO
	}
	public static void renamePlaylist(String name){				
		//TODO
	}
	public static void deletePlaylist(String name){
		//TODO
	}
	
}
