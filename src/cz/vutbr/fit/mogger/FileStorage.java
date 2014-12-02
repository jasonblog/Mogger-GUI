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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileStorage {

    private Context context;
    private static final String GESTURE_LIST_TAG = "Gestures";
    private static final String GESTURE_TAG = "Gesture";
    private static final String GESTURE_ATTR_NAME = "name";
    private static final String GESTURE_ATTR_THRESHOLD = "threshold";
    private static final String GESTURE_ATTR_FILE = "file";
    private static final String COORDS_TAG = "Coords";

    public FileStorage(Context context) {

        this.context = context;
    }

    public void storeGestures(ArrayList<Gesture> gestures) {
        try {
            Document doc = generateDocument(gestures);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(getFile());
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    private Document generateDocument(ArrayList<Gesture> gestures) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(GESTURE_LIST_TAG);
        doc.appendChild(rootElement);

        for (Gesture gesture : gestures) {
            Element gel = doc.createElement(GESTURE_TAG);
            gel.setAttribute(GESTURE_ATTR_NAME, gesture.name);
            gel.setAttribute(GESTURE_ATTR_FILE, gesture.fileSound);
            gel.setAttribute(GESTURE_ATTR_THRESHOLD, gesture.getThreshold() + "");

            int[][] coords = gesture.getCoordsArray();
            for (int i = 0; i < gesture.size(); i++) {
                Element c = doc.createElement(COORDS_TAG);
                gel.setAttribute("x", coords[0][i] + "");
                gel.setAttribute("y", coords[1][i] + "");
                gel.setAttribute("z", coords[2][i] + "");
                gel.appendChild(c);
            }

            rootElement.appendChild(gel);
        }

        return doc;
    }


    public ArrayList<Gesture> loadConfig() {
        ArrayList<Gesture> gestures = new ArrayList<Gesture>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse(getFile());
            gestures = parseDocument(dom);
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

        Element docEle = dom.getDocumentElement();
        NodeList nl = docEle.getElementsByTagName(GESTURE_TAG);

        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                Gesture g = getGesture(el);
                gestures.add(g);
            }
        }

        return gestures;
    }

    private Gesture getGesture(Element el) {
        String name = el.getAttribute(GESTURE_ATTR_NAME);
        String file = el.getAttribute(GESTURE_ATTR_FILE);
        int threshold = Integer.parseInt(el.getAttribute(GESTURE_ATTR_THRESHOLD));

        Gesture gesture = new Gesture(name, file, threshold);

        NodeList nl = el.getElementsByTagName(COORDS_TAG);
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
