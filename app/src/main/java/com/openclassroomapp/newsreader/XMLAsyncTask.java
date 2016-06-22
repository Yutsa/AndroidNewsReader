package com.openclassroomapp.newsreader;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream stream = connection.getInputStream();

            try {
                return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            }
            finally {
              stream.close();
            }

        }
        catch (Exception e) {
            Log.e("XMLAsyncTask", "Erreur lors de la connection.");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(Document document) {
        Log.i("XMLAsyncTask", "Téléchargement du XML terminé.");
        _documentConsumer.setXMLDocument(document);
    }
}
