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
//import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Playlist {
	private DocumentBuilderFactory docFact;
	private TransformerFactory transFact;
	private XPathFactory xPathFact;
	private String xmlfile;
	
	public Playlist(){
		this.docFact = DocumentBuilderFactory.newInstance();
		this.transFact = TransformerFactory.newInstance();
		this.xPathFact = XPathFactory.newInstance();
	}
	public Playlist(String path){
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
			//DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
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
			//Transformer transformer = TransformerFactory.newInstance().newTransformer();
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
			//XPath xPath =  XPathFactory.newInstance().newXPath();
			XPath xPath = this.xPathFact.newXPath();
			//System.out.println(expr);
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
		//Make sure no duplicates are created!!
		Document document = docParser();
		String expr = "/playlists/playlist[name='"+plName+"']";
		String[] prevPL = xpathSearch(expr); //Should be empty.
		if (prevPL.length==0){
			System.out.println("celebrate on github with the slaskarna");
		}
		else{
			System.out.println("prevPL.length = "+prevPL.length);
		}
		Element newElement = document.createElement("playlist");
		newElement.setTextContent(plName);
		Element root = document.getDocumentElement();
		root.appendChild(newElement);
		transforming(document);
	}
	
	
	public void addMedia(String playlist, String newSong){
		Document document = docParser();
		
		NodeList plElement = document.getElementsByTagName("playlist");   //IF plElement exists, else do something else. Try stuff.
		int plElemLength = plElement.getLength();
		for (int i=0; i <plElemLength;i++){
			Node node = plElement.item(i); 
			System.out.println(node.getChildNodes().toString());
			System.out.println(node.getNodeName());
			Node chNodes = node.getLastChild();

			Element newElement = document.createElement("media");// Element to be inserted
			newElement.setTextContent(newSong);
			chNodes.getParentNode().insertBefore(newElement, chNodes.getNextSibling());
		}
		transforming(document);

	}
	public void removeMedia(String name){
		//TODO
	}
	public void deletePlaylist(String name){
		//TODO
	}
	
}
