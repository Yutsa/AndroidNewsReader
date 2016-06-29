package com.openclassroomapp.newsreader;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Édouard WILLISSECK
 *
 * AsyncTask to fetch the Document (DOM) from the URL of a RSS feed.
 */
public class XMLAsyncTask extends AsyncTask<String, Void, ArrayList<RSSArticle>> {

    interface DocumentConsumer {
        void addArticle(ArrayList<RSSArticle> article);
    }

    private DocumentConsumer _documentConsumer;

    public XMLAsyncTask(DocumentConsumer consumer) {
        setDocumentConsumer(consumer);
    }

    public void setDocumentConsumer(DocumentConsumer consumer) {
        if (consumer == null)
            throw new IllegalArgumentException();
        _documentConsumer = consumer;
    }
    @Override
    protected ArrayList<RSSArticle> doInBackground(String... params) {
        ArrayList<RSSArticle> articleArrayList = new ArrayList<>();
        try {
            // Necessary to show the progress bar because otherwise the loading is instantaneous.
            Thread.sleep(2000);

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document document = documentBuilder.parse(params[0]);

            /* Gets the title of the RSS */
            String channelTitle = document.getElementsByTagName("title").item(0).getTextContent();

            NodeList nodeList = document.getElementsByTagName("item");

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Element element = (Element) nodeList.item(i);
                String title = element.getElementsByTagName("title").item(0).getTextContent();
                String link = element.getElementsByTagName("link").item(0).getTextContent();
                String date = element.getElementsByTagName("pubDate").item(0).getTextContent();
                articleArrayList.add(new RSSArticle(title, link, date, channelTitle));
            }
            return articleArrayList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<RSSArticle> rssArticles) {
        super.onPostExecute(rssArticles);

        _documentConsumer.addArticle(rssArticles);
    }
}
