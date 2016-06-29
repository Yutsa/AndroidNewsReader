package com.openclassroomapp.newsreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * @author Édouard WILLISSECK
 *
 * This is the Adapater for the article list gotten from the Document (DOM).
 */
public class RSSArticleAdapter extends RecyclerView.Adapter<RSSArticleAdapter.ArticleViewHolder>
        implements XMLAsyncTask.DocumentConsumer {

    private ArrayList<Document> documents = new ArrayList<>();

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_layout, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        int documentNumber = 0;
        Document document = documents.get(documentNumber);
        int documentLength = getDocumentLength(document);
        Element element;

        /*
        If we want to display an article not in the current document
        we go to the next document until all document have been displayed.
        */
        while (position > documentLength) {
            position -= documentLength;
            documentNumber++;
            if (documentNumber < documents.size())
                document = documents.get(documentNumber);
            else
                return;
        }
        element = (Element) document.getElementsByTagName("item").item(position);
        if (element != null)
            holder.display(element);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (documents.isEmpty())
            return 0;
        for (Document document : documents) {
            count += document.getElementsByTagName("item").getLength();
        }
        return count;
    }

    public int getDocumentLength(Document document) {
        return document.getElementsByTagName("item").getLength();
    }

    @Override
    public void setXMLDocument(Document document) {
        Log.i("RSSArticleAdapter", "Added a document");
        this.documents.add(document);
        Log.i("RSSArticleAdapter", "Documents:" + documents.toString());
        notifyDataSetChanged();
    }

    public void clearDocuments() {
        documents.clear();
        Log.i("RSSArticleAdapter", "Cleared documents");
    }

    public boolean hasDocuments() {
        return !documents.isEmpty();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final Context context;
        private Element currentElement;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = currentElement.getElementsByTagName("title")
                            .item(0)
                            .getTextContent();
                    String link = currentElement.getElementsByTagName("link")
                            .item(0)
                            .getTextContent();
                    Intent intent = new Intent(context,
                            ArticleActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("link", link);
                    context.startActivity(intent);

                }
            });
        }

        public void display(Element element) {
            currentElement = element;
            title.setText(element.getElementsByTagName("title").item(0).getTextContent());
        }
    }
}
