package com.qube.qube.project4.main.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qube.qube.project4.R;

/**
 * Created by Qube on 8/30/16.
 */
public class UserChoiceRecyclerViewAdapter extends RecyclerView.Adapter<UserChoiceViewHolder> {
    private Context mContext;
    private char[] mCharacterNameToChar;
    private UserChoiceCallBack mCallback;

    public interface UserChoiceCallBack {
        void onClick(int position, char charactherChar);
    }

    public UserChoiceRecyclerViewAdapter(Context context, String name, UserChoiceCallBack listener) {
        mContext = context;
        mCharacterNameToChar = name.toUpperCase().toCharArray();
        mCallback = listener;
    }

    public void userUpdateName(String name) {
        mCharacterNameToChar = name.toUpperCase().toCharArray();
        for (int i = 0; i < mCharacterNameToChar.length; i++) {
            int randomIndex = (int) (Math.random() * mCharacterNameToChar.length);
            char temp = mCharacterNameToChar[i];
            mCharacterNameToChar[i] = mCharacterNameToChar[randomIndex];
            mCharacterNameToChar[randomIndex] = temp;
        }
        notifyDataSetChanged();
    }

    @Override
    public UserChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_user_choice, parent, false);
        UserChoiceViewHolder viewHolder = new UserChoiceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserChoiceViewHolder holder, final int position) {
        holder.mTextView.setText(mCharacterNameToChar[position] + "");
        final char characterName = mCharacterNameToChar[position];
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onClick(position, characterName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCharacterNameToChar.length;
    }
}
