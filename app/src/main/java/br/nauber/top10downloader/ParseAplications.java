package br.nauber.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naubergois on 5/11/16.
 */
public class ParseAplications {

    private String xmlData;

    private List<Application> applications;

    public ParseAplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<Application>();

    }

    public List<Application> getAplcations() {
        return applications;
    }

    public boolean process() {
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d("ParseApplications", "Start tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentRecord = new Application();

                        }

                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d("ParseApplications", "End tag for " + tagName);
                        if (inEntry) {
                            if (tagName.equalsIgnoreCase("entry")) {

                                applications.add(currentRecord);
                                inEntry=false;


                            }
                            if (tagName.equalsIgnoreCase("name")) {

                                currentRecord.setName(textValue);


                            }
                            if (tagName.equalsIgnoreCase("artist")) {

                                currentRecord.setArtist(textValue);


                            }
                            if (tagName.equalsIgnoreCase("releaseDate")) {

                                currentRecord.setReleaseDate(textValue);


                            }

                        }
                        break;

                    default:

                }
                eventType = xpp.next();
            }


        } catch (Exception e) {
            status = false;
            e.printStackTrace();

        }
        return false;

    }

}
