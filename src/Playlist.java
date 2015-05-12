import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Playlist {
	
	public static void newPlaylist(String name){
		//TODO
	}
	public static void addMedia(String playlist, String newSong){
		File file = new File("/Users/Samuel/Documents/EclipseWS/AndinMP/src/playlists.xml");
		DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docFact.newDocumentBuilder();
		Document document = docBuild.parse(file);
		
		Element plElement = (Element) document.getElementsByTagName(playlist).item(0);
		//IF plElement exists, else do something else. Try stuff.
		Node chNodes = plElement.getLastChild();
		
		Element newElement = document .createElement("media"); // Element to be inserted 
		chNodes.getParentNode().insertBefore(newElement, chNodes.getNextSibling());
		
		//TODO
	}
	public static void removeMedia(String name){
		//TODO
	}
	public static void renamePlaylist(String name){
		//TODO
	}
	public static void deletePlaylist(String name){
		
	}
	
}
