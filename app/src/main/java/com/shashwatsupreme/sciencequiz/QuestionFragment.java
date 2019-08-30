package com.shashwatsupreme.sciencequiz;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shashwatsupreme.sciencequiz.Common.Common;
import com.shashwatsupreme.sciencequiz.Interface.IQuestion;
import com.shashwatsupreme.sciencequiz.model.CurrentQuestion;
import com.shashwatsupreme.sciencequiz.model.Question;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements IQuestion
{

    TextView txt_question_text;
    CheckBox ckbA, ckbB, ckbC, ckbD;
    FrameLayout layout_image;
    ProgressBar progressBar;

    Context context;
    Question question;
    int questionIndex=-1;

    public QuestionFragment()
    {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_question, container, false);

        //Get question
        questionIndex = getArguments().getInt("index", -1);
        if(Common.questionList.size() > 0)
        {
            question = Common.questionList.get(questionIndex);
//            Toast.makeText(getActivity(), "questionIndex="+questionIndex, Toast.LENGTH_SHORT).show();
        }

        if(question != null)
        {
            layout_image = itemView.findViewById(R.id.layout_image);
            progressBar = itemView.findViewById(R.id.progress_bar);
            if(question.isImageQuestion())
            {
                ImageView img_question = itemView.findViewById(R.id.img_question);
                Picasso.get().load(question.getQuestionImage()).into(img_question, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
                layout_image.setVisibility(View.GONE);
            //View
            txt_question_text = itemView.findViewById(R.id.txt_question_text);
            txt_question_text.setText(question.getQuestionText());

            ckbA = itemView.findViewById(R.id.ckbA);
            ckbA.setText(question.getAnswerA());
            ckbA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                        Common.selected_values.add(ckbA.getText().toString());
                    else
                        Common.selected_values.remove(ckbA.getText().toString());

                }
            });

            ckbB = itemView.findViewById(R.id.ckbB);
            ckbB.setText(question.getAnswerB());
            ckbB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                        Common.selected_values.add(ckbB.getText().toString());
                    else
                        Common.selected_values.remove(ckbB.getText().toString());

                }
            });

            ckbC = itemView.findViewById(R.id.ckbC);
            ckbC.setText(question.getAnswerC());
            ckbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                        Common.selected_values.add(ckbC.getText().toString());
                    else
                        Common.selected_values.remove(ckbC.getText().toString());

                }
            });

            ckbD = itemView.findViewById(R.id.ckbD);
            ckbD.setText(question.getAnswerD());
            ckbD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                        Common.selected_values.add(ckbD.getText().toString());
                    else
                        Common.selected_values.remove(ckbD.getText().toString());

                }
            });



        }


        return  itemView;
    }

    @Override
    public CurrentQuestion getSelectedAnswer()
    {
        //This function will return state of the answer
        //Right, wrong or normal
        CurrentQuestion currentQuestion = new CurrentQuestion(questionIndex, Common.ANSWER_TYPE.NO_ANSWER); //Default no answer
        StringBuilder result = new StringBuilder();
        if(Common.selected_values.size() > 1)
        {
            //If multichoice
            //Split answer to array
            //Ex. arr[0] = A, New York
            //Ex. arr[1] = B, Paris
            Object[] answerArray = Common.selected_values.toArray();
            for(int i=0; i<answerArray.length; i++)
                if(i<answerArray.length-1)
                    result.append(new StringBuilder(((String)answerArray[i]).substring(0, 1)).append(",")); //Take first letter of answer: Ex: arr[0] = A. New York, we will take letter 'A'
                else
                    result.append(new StringBuilder((String)answerArray[i]).substring(0, 1)); //Too

//            Toast.makeText(getActivity(), "result="+result, Toast.LENGTH_SHORT).show();
        }
        else if(Common.selected_values.size() == 1)
        {
            //If only one choice
            Object[] arrayAnswer = Common.selected_values.toArray();
            result.append(((String)arrayAnswer[0]).substring(0, 1));
//            Toast.makeText(getActivity(), "result="+result, Toast.LENGTH_SHORT).show();
        }
        if(question!=null)
        {
            //Compare correctAnswer with user answer
            if (!(TextUtils.isEmpty(result)))
            {
                if (result.toString().equalsIgnoreCase(question.getCorrectAnswer()))
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                else
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
            }
            else
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        else
        {
//            Toast.makeText(getContext(), "Cannot get question", Toast.LENGTH_SHORT).show();
            currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        Common.selected_values.clear(); //Always clear selected values when comparing done
        return currentQuestion;
    }

    @Override
    public void showCorrectAnswer()
    {
        //Show correct answer
        //Pattern : A,B
//        questionIndex = getArguments().getInt("index", -1);
//        if(Common.questionList.size() > 0)
//        {
//            question = Common.questionList.get(questionIndex);
////            Toast.makeText(getActivity(), "questionIndex="+questionIndex, Toast.LENGTH_SHORT).show();
//        }



        String[] correctAnswer = question.getCorrectAnswer().split(",");
//        Toast.makeText(getActivity(), ""+question.getCorrectAnswer(), Toast.LENGTH_SHORT).show();

//        View itemView = inflater.inflate(R.layout.fragment_question, container, false);
//        if(question != null)
//        {
//            layout_image = itemView.findViewById(R.id.layout_image);
//            progressBar = itemView.findViewById(R.id.progress_bar);
//            if(question.isImageQuestion())
//            {
//                ImageView img_question = itemView.findViewById(R.id.img_question);
//                Picasso.get().load(question.getQuestionImage()).into(img_question, new Callback()
//                {
//                    @Override
//                    public void onSuccess()
//                    {
//                        progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Exception e)
//                    {
//                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            else
//                layout_image.setVisibility(View.GONE);
//            //View
//            txt_question_text = itemView.findViewById(R.id.txt_question_text);
//            txt_question_text.setText(question.getQuestionText());
//
//            ckbA = itemView.findViewById(R.id.ckbA);
//            ckbA.setText(question.getAnswerA());
//
//            ckbB = itemView.findViewById(R.id.ckbB);
//            ckbB.setText(question.getAnswerB());
//
//
//            ckbC = itemView.findViewById(R.id.ckbC);
//            ckbC.setText(question.getAnswerC());
//
//
//            ckbD = itemView.findViewById(R.id.ckbD);
//            ckbD.setText(question.getAnswerD());
//
//
//
//
//        }
//
        for(String answer:correctAnswer)
        {
            if(answer.equalsIgnoreCase("A"))
            {
                ckbA.setTypeface(null, Typeface.BOLD);
                ckbA.setTextColor(Color.GREEN);
            }
            else if(answer.equalsIgnoreCase("B"))
            {
                ckbB.setTypeface(null, Typeface.BOLD);
                ckbB.setTextColor(Color.GREEN);
            }
            else if(answer.equalsIgnoreCase("C"))
            {
                ckbC.setTypeface(null, Typeface.BOLD);
                ckbC.setTextColor(Color.GREEN);
            }
            else if(answer.equalsIgnoreCase("D"))
            {
                ckbD.setTypeface(null, Typeface.BOLD);
                ckbD.setTextColor(Color.GREEN);
            }
        }

    }

    @Override
    public void disableAnswer()
    {
        ckbA.setEnabled(false);
        ckbB.setEnabled(false);
        ckbC.setEnabled(false);
        ckbD.setEnabled(false);
    }

    @Override
    public void resetQuestion()
    {
        //Enable Checkbox
        ckbA.setEnabled(true);
        ckbB.setEnabled(true);
        ckbC.setEnabled(true);
        ckbD.setEnabled(true);

        //Remove all selected
        ckbA.setChecked(false);
        ckbB.setChecked(false);
        ckbC.setChecked(false);
        ckbD.setChecked(false);

        //Remove all bold on text
        ckbA.setTypeface(null, Typeface.NORMAL);
        ckbA.setTextColor(Color.BLACK);
        ckbB.setTypeface(null, Typeface.NORMAL);
        ckbB.setTextColor(Color.BLACK);
        ckbC.setTypeface(null, Typeface.NORMAL);
        ckbC.setTextColor(Color.BLACK);
        ckbD.setTypeface(null, Typeface.NORMAL);
        ckbD.setTextColor(Color.BLACK);


    }
}
