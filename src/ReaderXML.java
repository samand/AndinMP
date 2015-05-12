import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;



public class ReaderXML{
	public static void main(String[] args) {
		
	    try {
	    	File fXmlFile = new File("/Users/Samuel/Documents/EclipseWS/AndinMP/src/playlists.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fXmlFile);
		 
			document.getDocumentElement().normalize();//Kanske är onödigt. Bara dokumentet är strukturerat så.
		 
			NodeList nodes = document.getElementsByTagName("playlist");
		 
			System.out.println("nodesgetlength = "+nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++) {
		 
				Node cNode = nodes.item(i);
		 
				if (cNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) cNode;
					System.out.println("media   " + eElement.getElementsByTagName("media").item(0).getTextContent());
				}
			}
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}


