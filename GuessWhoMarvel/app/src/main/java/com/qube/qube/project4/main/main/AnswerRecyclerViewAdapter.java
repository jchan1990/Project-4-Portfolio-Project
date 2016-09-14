package com.qube.qube.project4.main.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qube.qube.project4.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Qube on 8/30/16.
 */
public class AnswerRecyclerViewAdapter extends RecyclerView.Adapter<AnswerViewHolder> implements UserChoiceRecyclerViewAdapter.UserChoiceCallBack {
    private Context mContext;
    public char[] mCharacterNameToChar;
    public List<Character> mUserInput;
    private int characterCount = 0;

    public AnswerRecyclerViewAdapter(Context context, String name) {
        mContext = context;
        mCharacterNameToChar = name.toUpperCase().toCharArray();
        mUserInput = new ArrayList<>();
    }

    public void answerUpdateName(String name) {
        mCharacterNameToChar = name.toUpperCase().toCharArray();
        for (char character : mCharacterNameToChar){
            mUserInput.add(' ');
        }
        notifyDataSetChanged();
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_answer, parent, false);
        AnswerViewHolder viewHolder = new AnswerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        holder.mTextView.setText(mUserInput.get(position)  + "");
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserInput != null ? mUserInput.size() : 0;
    }

    @Override
    public void onClick(int position, char charactherChar) {

            mUserInput.set(characterCount, charactherChar);
            characterCount++;
            notifyDataSetChanged();
    }

}
