package com.shashwatsupreme.sciencequiz.Interface;

import com.shashwatsupreme.sciencequiz.model.CurrentQuestion;

public interface IQuestion
{
    CurrentQuestion getSelectedAnswer(); //Get selected answer from user select
    void showCorrectAnswer(); //Bold correct answer text
    void disableAnswer(); //Disable all check boxes
    void resetQuestion(); //Reset all function on question
}
