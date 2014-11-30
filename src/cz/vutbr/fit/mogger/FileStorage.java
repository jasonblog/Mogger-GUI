package cz.vutbr.fit.mogger;

import android.content.Context;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileStorage {

    private Context context;

    public FileStorage(Context context) {

        this.context = context;
    }

    public void storeGestures(ArrayList<Gesture> gestures) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("gestures");
            doc.appendChild(rootElement);

            for (Gesture gesture : gestures) {
                Element gel = doc.createElement("gesture");
                gel.setAttribute("name", gesture.name);
                gel.setAttribute("file", gesture.fileSound);
                gel.setAttribute("threshold", gesture.getThreshold() + "");

                int[][] coords = gesture.getCoordsArray();
                for (int i = 0; i < gesture.size(); i++) {
                    Element c = doc.createElement("coord");
                    gel.setAttribute("x", coords[0][i] + "");
                    gel.setAttribute("y", coords[1][i] + "");
                    gel.setAttribute("z", coords[2][i] + "");
                    gel.appendChild(c);
                }

                rootElement.appendChild(gel);
            }


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(getFile());

            transformer.transform(source, result);
            Log.d("FileStorage", "File saved!");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Gesture> loadConfig() {
        ArrayList<Gesture> gestures = new ArrayList<Gesture>();
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            Document dom = db.parse("employees.xml");
//            gestures = parseDocument(dom);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return gestures;
    }

    private ArrayList<Gesture> parseDocument(Document dom) {
        ArrayList<Gesture> gestures = new ArrayList<Gesture>();

        //get the root element
        Element docEle = dom.getDocumentElement();

        //get a nodelist of         elements
        NodeList nl = docEle.getElementsByTagName("Gesture");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                Gesture g = getGesture(el);

                //add it to list
                gestures.add(g);
            }
        }

        return gestures;
    }

    private Gesture getGesture(Element el) {
        String name = el.getAttribute("name");
        String file = el.getAttribute("file");
        int threshold = Integer.parseInt(el.getAttribute("threshold"));

        Gesture gesture = new Gesture(name, file, threshold);

        NodeList nl = el.getElementsByTagName("Coords");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element elc = (Element) nl.item(i);
                int x = Integer.parseInt(elc.getAttribute("x"));
                int y = Integer.parseInt(elc.getAttribute("y"));
                int z = Integer.parseInt(elc.getAttribute("z"));

                gesture.addCoords(x, y, z);
            }
        }

        return gesture;
    }

    private File getFile() {
        return new File(context.getFilesDir(), "config.xml");
    }
}
