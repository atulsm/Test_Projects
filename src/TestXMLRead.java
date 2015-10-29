import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TestXMLRead {
	
	public static void main(String[] args) {
		try {

	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse (new File("event_schema.xml"));
	        doc.getDocumentElement ().normalize ();
	        NodeList attributes = doc.getElementsByTagName("Attribute");
	        
	        for(int s=0; s<attributes.getLength() ; s++){
	            Node attr = attributes.item(s);
	            NamedNodeMap map =  attr.getAttributes();
	            
	           // System.out.print(map.getNamedItem("binarytag").getNodeValue() + ",");
	            Node node1 = map.getNamedItem("shortname");
	            if(node1 == null){
	            	node1 = map.getNamedItem("name");
	            }
	            String fieldName = node1.getNodeValue();
	            
	           // String field = (fieldName.subSequence(0, 1)+"").toUpperCase() + fieldName.substring(1);
	            
	            System.out.print("event.set"+ fieldName + "(");
	            String type = map.getNamedItem("type").getNodeValue();
	            if("String".equals(type)){
	            	System.out.println("\"Data\");");
	            }else if("Int".equals(type)){
	            	System.out.println("1);");
	            }else if("Uuid".equals(type)){
	            	System.out.println("UUIDGenerator.generate());");
	            }else if("Ip".equals(type)){
	            	System.out.println("\"164.99.135.40\");");
	            }else if("Mac".equals(type)){
	            	System.out.println("\"00:1C:B3:09:85:15\");");
	            }else if("Number".equals(type)){
	            	System.out.println("1);");
	            }else if("Date".equals(type)){
	            	System.out.println("new Date());");
	            }

	        }


		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
