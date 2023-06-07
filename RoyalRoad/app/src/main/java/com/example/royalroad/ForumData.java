package com.example.royalroad;

public class ForumData
{
    private String Title;
    private String Description;
    private int PostCount;
    private int TopicsCount;

    public ForumData(String title, String description, int postCount, int topicsCount)
    {
        this.Title = title;
        this.Description = description;
        this.PostCount = postCount;
        this.TopicsCount = topicsCount;
    }

    public String GetTitle()
    {
        return this.Title;
    }

    public void SetTitle(String NewTitle)
    {
        this.Title = NewTitle;
    }

    public String GetDescription()
    {
        return this.Description;
    }

    public void SetDescription(String NewDescription)
    {
        this.Description = NewDescription;
    }

    public int GetPostCount()
    {
        return this.PostCount;
    }

    public void SetPostCount(int NewPostCount)
    {
        this.PostCount = NewPostCount;
    }

    public int GetTopicsCount()
    {
        return this.TopicsCount;
    }

    public void SetTopicsCount(int NewTopicsCount)
    {
        this.TopicsCount = NewTopicsCount;
    }
}