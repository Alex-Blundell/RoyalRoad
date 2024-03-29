package com.example.royalroad;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firestore.admin.v1.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParagraphLineAdapter extends RecyclerView.Adapter<ParagraphLineAdapter.ParagraphLineViewHolder>
{
    ArrayList<Book.Paragraph> Data;
    ArrayList<ParagraphLineViewHolder> LineHolders;
    int ChapterID;

    SharedPreferences Pref;

    public ParagraphLineAdapter(ArrayList<Book.Paragraph> data, int ChapterID)
    {
        this.Data = data;
        LineHolders = new ArrayList<>();
        this.ChapterID = ChapterID;
    }

    @NonNull
    @Override
    public ParagraphLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paragraph_line_item, parent, false);
        return new ParagraphLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParagraphLineViewHolder holder, int position)
    {
        Log.println(Log.INFO, "Hi", "Chapter: " + this.ChapterID + " Paragraph: " + Data.get(position).ParagraphID);

        Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("ReadingTheme", false);

        if(IsDarkMode)
        {
            holder.Line.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.Divider.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkBorder));
        }
        else
        {
            holder.Line.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        Resources res = holder.itemView.getResources();
        int ParagraphSpacing = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
        int StartSpacing = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, res.getDisplayMetrics());

        if(Data.get(position).Content.contains("<img src="))
        {
            String URL = Data.get(position).Content.split("\"")[1];
            holder.Image.setVisibility(View.VISIBLE);

            Glide.with(holder.itemView.getContext())
                    .load(URL)
                    .into(holder.Image);

            holder.Image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ((ReadActivity)holder.itemView.getContext()).HideToolbars();
                }
            });

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)holder.Image.getLayoutParams();

            if(position == 0)
            {
                params.setMargins(0, StartSpacing, 0, ParagraphSpacing);
            }
            else
            {
                params.setMargins(0, 0, 0, ParagraphSpacing);
            }

            holder.Image.setLayoutParams(params);
        }
        else if(Data.get(position).Content.contains("<img style="))
        {
            String URL = Data.get(position).Content.split("\"")[3];
            holder.Image.setVisibility(View.VISIBLE);

            Glide.with(holder.itemView.getContext())
                    .load(URL)
                    .into(holder.Image);

            holder.Image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ((ReadActivity)holder.itemView.getContext()).HideToolbars();
                }
            });

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)holder.Image.getLayoutParams();

            if(position == 0)
            {
                params.setMargins(0, StartSpacing, 0, ParagraphSpacing);
            }
            else
            {
                params.setMargins(0, 0, 0, ParagraphSpacing);
            }

            holder.Image.setLayoutParams(params);
        }
        else if(Data.get(position).Content.contains("<hr>"))
        {
            holder.Divider.setVisibility(View.VISIBLE);
            int MarginSpacing = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, res.getDisplayMetrics());

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)holder.Divider.getLayoutParams();

            if(position == 0)
            {
                params.setMargins(MarginSpacing, StartSpacing, MarginSpacing, ParagraphSpacing);
            }
            else
            {
                params.setMargins(MarginSpacing, 0, MarginSpacing, ParagraphSpacing);
            }
        }
        else
        {
            LineHolders.add(holder);

            int MarginSpacing = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, res.getDisplayMetrics());
            int LineSpacing = 5;

            // If Paragraph Type is any type of Text.
            holder.Line.setVisibility(View.VISIBLE);
            holder.Line.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ((ReadActivity)holder.itemView.getContext()).HideToolbars();
                }
            });

            int FontSize = Pref.getInt("FontSize", 14);
            int FontType = Pref.getInt("ReadingFont", BaseSettingsFragment.FontStyle.Open_Sans.ordinal());

            Typeface tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.open_sans);

            switch (FontType)
            {
                case 0:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.arial);
                    break;

                case 1:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.atkinson_hyperlegible);
                    break;

                case 2:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.caslon);
                    break;

                case 3:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.comic_sans);
                    break;

                case 4:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.franklin_gothic);
                    break;

                case 5:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.garamond);
                    break;

                case 6:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.lucida);
                    break;

                case 7:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.minion);
                    break;

                case 8:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.open_dyslexic);
                    break;

                case 9:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.open_sans);
                    break;

                case 10:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.roboto);
                    break;

                case 11:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.sans_serif);
                    break;

                case 12:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.verdana);
                    break;

                case 13:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.ubuntu);
                    break;

                case 14:
                    tf = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.ubuntu_condensed_regular);
                    break;
            }

            holder.Line.setTypeface(tf);

            String Paragraph = Data.get(position).Content;

            if(Paragraph.contains("<RR_APP_PARAGRAPH_ALIGN_CENTER>"))
            {
                Paragraph = Paragraph.replace("<RR_APP_PARAGRAPH_ALIGN_CENTER>", "");
                holder.Line.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            else if(Paragraph.contains("<RR_APP_PARAGRAPH_ALIGN_LEFT>"))
            {
                Paragraph = Paragraph.replace("<RR_APP_PARAGRAPH_ALIGN_LEFT>", "");
                holder.Line.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
            else if(Paragraph.contains("<RR_APP_PARAGRAPH_ALIGN_RIGHT>"))
            {
                Paragraph = Paragraph.replace("<RR_APP_PARAGRAPH_ALIGN_RIGHT>", "");
                holder.Line.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
            else if(Paragraph.contains("<RR_APP_PARAGRAPH_UNDERLINE>"))
            {
                Paragraph = Paragraph.replace("<RR_APP_PARAGRAPH_UNDERLINE>", "");
            }

            //Paragraph = Paragraph.replace("<strong><em>", "");
            //Paragraph = Paragraph.replace("</em></strong>", "");

            Spannable AlteredParagraph = new SpannableStringBuilder(Paragraph);

            if(AlteredParagraph.toString().contains("<strong><em>"))
            {
                Pattern ItalicBoldPattern = Pattern.compile("<strong><em>");
                Matcher ItalicBoldMatcher = ItalicBoldPattern.matcher(AlteredParagraph.toString());

                while(ItalicBoldMatcher.find())
                {
                    int Index = AlteredParagraph.toString().indexOf("<strong><em>");
                    int EndIndex = AlteredParagraph.toString().indexOf("</em></strong>");

                    ((SpannableStringBuilder) AlteredParagraph).delete(Index, Index + 12);
                    EndIndex -= 12;

                    ((SpannableStringBuilder) AlteredParagraph).delete(EndIndex, EndIndex + 14);

                    StyleSpan ItalicSpan = new StyleSpan(Typeface.BOLD_ITALIC);

                    AlteredParagraph.setSpan(ItalicSpan, Index, EndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }

            if(AlteredParagraph.toString().contains("<em>"))
            {
                Pattern ItalicPattern = Pattern.compile("<em>");
                Matcher ItalicMatcher = ItalicPattern.matcher(AlteredParagraph.toString());

                while(ItalicMatcher.find())
                {
                    int Index = AlteredParagraph.toString().indexOf("<em>");
                    int EndIndex = AlteredParagraph.toString().indexOf("</em>");

                    ((SpannableStringBuilder) AlteredParagraph).delete(Index, Index + 4);
                    EndIndex -= 4;

                    ((SpannableStringBuilder) AlteredParagraph).delete(EndIndex, EndIndex + 5);

                    StyleSpan ItalicSpan = new StyleSpan(Typeface.ITALIC);

                    AlteredParagraph.setSpan(ItalicSpan, Index, EndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }

            if(AlteredParagraph.toString().contains("<strong>"))
            {
                Pattern BoldPattern = Pattern.compile("<strong>");
                Matcher BoldMatcher = BoldPattern.matcher(AlteredParagraph.toString());

                while(BoldMatcher.find())
                {
                    int Index = AlteredParagraph.toString().indexOf("<strong>");
                    int EndIndex = AlteredParagraph.toString().indexOf("</strong>");

                    ((SpannableStringBuilder) AlteredParagraph).delete(Index, Index + 8);
                    EndIndex -= 8;

                    ((SpannableStringBuilder) AlteredParagraph).delete(EndIndex, EndIndex + 9);

                    StyleSpan ItalicSpan = new StyleSpan(Typeface.BOLD);

                    AlteredParagraph.setSpan(ItalicSpan, Index, EndIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }

            if(AlteredParagraph.toString().length() > 0)
            {
                holder.Line.setText(AlteredParagraph);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)holder.Line.getLayoutParams();

                if(position == 0)
                {
                    params.setMargins(MarginSpacing, StartSpacing, MarginSpacing, ParagraphSpacing);
                }
                else if(position == Data.size())
                {
                    params.setMargins(MarginSpacing, 0, MarginSpacing, StartSpacing);
                }
                else
                {
                    params.setMargins(MarginSpacing, 0, MarginSpacing, ParagraphSpacing);
                }

                holder.Line.setLayoutParams(params);
                holder.Line.setTextSize(FontSize);
            }
            else
            {
                holder.Line.setVisibility(View.GONE);
            }
        }
    }

    public void AlterFont(BaseSettingsFragment.FontStyle NewFont)
    {
        Typeface Font = null;

        switch (NewFont)
        {
            case Arial:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.arial);
                break;

            case Atkinson_Hyperlegible:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.atkinson_hyperlegible);
                break;

            case Caslon:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.caslon);
                break;

            case Comic_Sans:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.comic_sans);
                break;

            case Franklin_Gothic:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.franklin_gothic);
                break;

            case Garamond:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.garamond);
                break;

            case Lucida:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.lucida);
                break;

            case Minion:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.minion);
                break;

            case Open_Dyslexic:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.open_dyslexic);
                break;

            case Open_Sans:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.open_sans);
                break;

            case Roboto:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.roboto);
                break;

            case Sans_Serif:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.sans_serif);
                break;

            case Ubuntu:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.ubuntu);
                break;

            case Ubuntu_Condensed:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.ubuntu_condensed_regular);
                break;

            case Verdanda:
                Font = ResourcesCompat.getFont(LineHolders.get(0).itemView.getContext(), R.font.verdana);
                break;
        }

        for(ParagraphLineViewHolder holder : LineHolders)
        {
            holder.Line.setTypeface(Font);
        }
    }

    public void SetFontSize()
    {
        for (ParagraphLineViewHolder CurrentHolder: LineHolders)
        {
            int FontSize = Pref.getInt("FontSize", 14);
            CurrentHolder.Line.setTextSize(FontSize);
        }
    }

    @Override
    public int getItemCount()
    {
        return Data.size();
    }

    public class ParagraphLineViewHolder extends RecyclerView.ViewHolder
    {
        View Divider;
        ShapeableImageView Image;
        TextView Line;

        public ParagraphLineViewHolder(@NonNull View itemView)
        {
            super(itemView);

            Divider = itemView.findViewById(R.id.DividerLine);
            Image = itemView.findViewById(R.id.ImageLine);
            Line = itemView.findViewById(R.id.TextLine);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}