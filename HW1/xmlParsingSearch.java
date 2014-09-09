// HW1
import javax.xml.parsers.*;
import java.io.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class xmlParsingSearch {
    
    public static void main(String[] args) {
        
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String filePath = System.getProperty("user.home") + "/Documents/GitHub/Text-Analytics-Class-Fall2014/HW1";
            Document document = db.parse(new FileInputStream(filePath + "/j-questions.xml"));
            XPath xp = XPathFactory.newInstance().newXPath();
            
            String expression1 = "/jeopardy/clue[category = 'COMMON BONDS']/id";
            String expression2 = "/jeopardy/clue[category = 'COMMON BONDS']/category";
            String expression3 = "/jeopardy/clue[category = 'COMMON BONDS']/question";
            String expression4 = "/jeopardy/clue[category = 'COMMON BONDS']/answer";
                        
            NodeList nodes1 = (NodeList) xp.compile(expression1).evaluate(document,XPathConstants.NODESET);
            NodeList nodes2 = (NodeList) xp.compile(expression2).evaluate(document,XPathConstants.NODESET);
            NodeList nodes3 = (NodeList) xp.compile(expression3).evaluate(document,XPathConstants.NODESET);
            NodeList nodes4 = (NodeList) xp.compile(expression4).evaluate(document,XPathConstants.NODESET);

            for(int i=0; null != nodes2 && i< nodes2.getLength(); i++) {
                Node node1 = nodes1.item(i);
                Node node2 = nodes2.item(i);
                Node node3 = nodes3.item(i);
                Node node4 = nodes4.item(i);

                if(node2.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println(nodes1.item(i).getNodeName() + ": " + node1.getFirstChild().getNodeValue());
                        System.out.println(nodes2.item(i).getNodeName() + ": " + node2.getFirstChild().getNodeValue());
                        System.out.println(nodes3.item(i).getNodeName() + ": " + node3.getFirstChild().getNodeValue());
                        System.out.println(nodes4.item(i).getNodeName() + ": " + node4.getFirstChild().getNodeValue() + "\n\n");
                }
            }
        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
        }

    }    
}
