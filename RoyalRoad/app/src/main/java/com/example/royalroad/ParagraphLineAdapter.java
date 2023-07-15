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

    public ParagraphLineAdapter(ArrayList<Book.Paragraph> data)
    {
        this.Data = data;
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
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
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

            int DeleteOffset = 0;

            Spannable AlteredParagraph = new SpannableStringBuilder(Paragraph);

            if(Paragraph.contains("<em>"))
            {
                Pattern ItalicPattern = Pattern.compile("<em>");
                Matcher ItalicMatcher = ItalicPattern.matcher(Paragraph);

                int IndexOffset = 0;

                while(ItalicMatcher.find())
                {
                    int Index = Paragraph.indexOf("<em>", IndexOffset);
                    int EndIndex = Paragraph.indexOf("</em>", Index);

                    ((SpannableStringBuilder) AlteredParagraph).delete(Index - DeleteOffset, Index - DeleteOffset + 4);
                    DeleteOffset += 4;

                    ((SpannableStringBuilder) AlteredParagraph).delete(EndIndex - DeleteOffset, EndIndex - DeleteOffset + 5);

                    StyleSpan ItalicSpan = new StyleSpan(Typeface.ITALIC);

                    if(Index > 4)
                    {
                        AlteredParagraph.setSpan(ItalicSpan, Index - DeleteOffset + 4, EndIndex - DeleteOffset, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    else
                    {
                        AlteredParagraph.setSpan(ItalicSpan, Index, EndIndex - DeleteOffset, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    DeleteOffset += 5;
                    IndexOffset = EndIndex;
                }
            }

            if(Paragraph.contains("<strong>"))
            {
                Pattern BoldPattern = Pattern.compile("<strong>");
                Matcher BoldMatcher = BoldPattern.matcher(Paragraph);

                int IndexOffset = 0;

                while(BoldMatcher.find())
                {
                    int Index = Paragraph.indexOf("<strong>", IndexOffset);
                    int EndIndex = Paragraph.indexOf("</strong>", Index);

                    ((SpannableStringBuilder) AlteredParagraph).delete(Index - DeleteOffset, Index - DeleteOffset + 8);
                    DeleteOffset += 8;

                    ((SpannableStringBuilder) AlteredParagraph).delete(EndIndex - DeleteOffset, EndIndex - DeleteOffset + 9);

                    StyleSpan BoldSpan = new StyleSpan(Typeface.BOLD);

                    if(Index > 8)
                    {
                        AlteredParagraph.setSpan(BoldSpan, Index - DeleteOffset + 8, EndIndex - DeleteOffset, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    else
                    {
                        AlteredParagraph.setSpan(BoldSpan, Index, EndIndex - DeleteOffset, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    IndexOffset = EndIndex;
                    DeleteOffset += 9;
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