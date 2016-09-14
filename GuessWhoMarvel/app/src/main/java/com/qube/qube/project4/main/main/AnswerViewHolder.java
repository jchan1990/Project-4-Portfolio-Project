package com.qube.qube.project4.main.main;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qube.qube.project4.R;

/**
 * Created by Qube on 8/30/16.
 */
public class AnswerViewHolder extends RecyclerView.ViewHolder {
    public CardView mCardView;
    public TextView mTextView;

    public AnswerViewHolder(View view) {
        super(view);

        mCardView = (CardView) view.findViewById(R.id.cv_answer);
        mTextView = (TextView) view.findViewById(R.id.tv_answer_letter);
    }
}
