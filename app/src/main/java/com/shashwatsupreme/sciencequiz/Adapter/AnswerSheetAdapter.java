package com.shashwatsupreme.sciencequiz.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shashwatsupreme.sciencequiz.Common.Common;
import com.shashwatsupreme.sciencequiz.R;
import com.shashwatsupreme.sciencequiz.model.CurrentQuestion;

import java.util.List;

public class AnswerSheetAdapter extends RecyclerView.Adapter<AnswerSheetAdapter.MyViewHolder>
{
    Context context;
    List<CurrentQuestion> currentQuestionList;

    public AnswerSheetAdapter(Context context, List<CurrentQuestion> currentQuestionList)
    {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_answer_sheet_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {
        if((currentQuestionList.get(i)!=null)&&(myViewHolder.question_item!=null))
        {
            if (currentQuestionList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
                myViewHolder.question_item.setBackgroundResource(R.drawable.grid_question_right_answer);
            else if (currentQuestionList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
                myViewHolder.question_item.setBackgroundResource(R.drawable.grid_question_wrong_answer);
            else
                myViewHolder.question_item.setBackgroundResource(R.drawable.grid_question_no_answer);
        }

    }

    @Override
    public int getItemCount()
    {
        return currentQuestionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        View question_item;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            question_item = itemView.findViewById(R.id.question_item);
        }
    }
}
