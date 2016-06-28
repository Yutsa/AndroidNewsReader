package com.openclassroomapp.newsreader;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Édouard WILLISSECK
 *
 * AsyncTask to fetch the Document (DOM) from the URL of a RSS feed.
 */
public class XMLAsyncTask extends AsyncTask<String, Void, Document>{

    interface DocumentConsumer {
        void setXMLDocument(Document document);
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
    protected Document doInBackground(String... params) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            return documentBuilder.parse(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(Document document) {
        Log.i("XMLAsyncTask", "Téléchargement du XML terminé.");
        _documentConsumer.setXMLDocument(document);
    }
}
