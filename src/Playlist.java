import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Playlist {
	private static Document docParser(){
		try{
			File file = new File("/Users/Samuel/Documents/EclipseWS/AndinMP/src/playlists.xml");
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

		Element plElement = (Element) document.getElementsByTagName("playlist").item(0);   //IF plElement exists, else do something else. Try stuff.
		Node chNodes = plElement.getLastChild();
		Element newElement = document.createElement("media");// Element to be inserted
		newElement.setTextContent(newSong);
		chNodes.getParentNode().insertBefore(newElement, chNodes.getNextSibling());

		transforming(document);
			
	}
	public static void removeMedia(String name){
		//TODO
	}
	public static void renamePlaylist(String name){
		Document document = docParser();
		
		
		Element plElement = (Element) document.getElementById("My Playlist");
		System.out.println("gfjdklöafjdkölsafjköldjfklöajdfklöasjfklöseda");
		System.out.println(plElement.getTextContent());
		
		//TODO
	}
	public static void deletePlaylist(String name){
		
	}
	
}
