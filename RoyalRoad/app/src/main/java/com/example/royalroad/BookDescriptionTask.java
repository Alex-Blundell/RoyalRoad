package com.example.royalroad;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BookDescriptionTask extends AsyncTask<Void, Void, String>
{
    Document StoryDoc;

    public BookDescriptionTask(Document Doc)
    {
        this.StoryDoc = Doc;
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        String Description = "";
        Elements DescriptionElements = this.StoryDoc.getElementsByClass("description");
        Elements DescriptionLines = DescriptionElements.get(0).getElementsByTag("p");

        for(int i = 0; i < DescriptionLines.size(); i++)
        {
            if(!DescriptionLines.get(i).text().isEmpty())
            {
                if(DescriptionLines.get(i).getElementsByTag("strong").size() > 0 || DescriptionLines.get(i).getElementsByTag("b").size() > 0)
                {
                    if(i != DescriptionLines.size() - 1);
                    {
                        Description += DescriptionLines.get(i).text() + "IAMBOLD" + "\n\n";
                    }
                }
                else
                {
                    if(DescriptionLines.get(i).getElementsByTag("i").size() > 0)
                    {
                        // Italicize Word.
                        Elements GetItalizedWords = DescriptionLines.get(i).getElementsByTag("span");

                        for(int j = 0; j < GetItalizedWords.size(); i++)
                        {
                            if(GetItalizedWords.get(j).getElementsByTag("i").size() > 0)
                            {
                                if(j != GetItalizedWords.size() - 1)
                                {
                                    Description += GetItalizedWords.get(j).text() + "ITALICIZED";
                                }
                                else
                                {
                                    Description += GetItalizedWords.get(j).text() + "ITALICIZED" + "\n\n";
                                }
                            }
                            else
                            {
                                if(j != GetItalizedWords.size() - 1)
                                {
                                    Description += GetItalizedWords.get(j).text();
                                }
                                else
                                {
                                    Description += GetItalizedWords.get(j).text() + "\n\n";
                                }
                            }
                        }
                    }
                    else
                    {
                        if(i != DescriptionLines.size() - 1)
                        {
                            Description += DescriptionLines.get(i).text() + "\n\n";
                        }
                        else
                        {
                            Description += DescriptionLines.get(i).text();
                        }
                    }
                }
            }
        }

        this.StoryDoc = null;
        return Description;
    }
}
