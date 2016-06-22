package com.openclassroomapp.newsreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Édouard WILLISSECK
 *
 * This is the Adapater for the article list gotten from the Document (DOM).
 */
public class RSSArticleAdapter extends RecyclerView.Adapter<RSSArticleAdapter.ArticleViewHolder>
        implements XMLAsyncTask.DocumentConsumer {

    private Document document = null;

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_layout, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Element element = (Element) document.getElementsByTagName("item").item(position);
        holder.display(element);
    }

    @Override
    public int getItemCount() {
        if (document == null)
            return 0;
        return document.getElementsByTagName("item").getLength();
    }

    @Override
    public void setXMLDocument(Document document) {
        this.document = document;
        notifyDataSetChanged();
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
                    String title = currentElement.getElementsByTagName("title").item(0)
                            .getTextContent();
                    String link = currentElement.getElementsByTagName("link").item(0)
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
