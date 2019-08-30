package com.shashwatsupreme.sciencequiz.model;

public class Question
{
    private int ID;
    private String QuestionText, QuestionImage, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer;
    private boolean IsImageQuestion;
    private int CategoryID;
    private int ClassID;



    public Question()
    {
    }

    public Question(int ID, String questionText, String questionImage, String answerA, String answerB, String answerC, String answerD, String correctAnswer, boolean isImageQuestion, int categoryID, int classID)
    {
        this.ID = ID;
        this.QuestionText = questionText;
        this.QuestionImage = questionImage;
        this.AnswerA = answerA;
        this.AnswerB = answerB;
        this.AnswerC = answerC;
        this.AnswerD = answerD;
        this.CorrectAnswer = correctAnswer;
        this.IsImageQuestion = isImageQuestion;
        this.CategoryID = categoryID;
        this.ClassID = classID;
    }

//    public Question(int id, String QuestionText, String QuestionImage, String AnswerA, String AnswerB, String AnswerC, String AnswerD, String CorrectAnswer, boolean IsImageQuestion, int CategoryID)
//    {
//        this.id = id;
//        this.QuestionText = QuestionText;
//        this.QuestionImage = QuestionImage;
//        this.AnswerA = AnswerA;
//        this.AnswerB = AnswerB;
//        this.AnswerC = AnswerC;
//        this.AnswerD = AnswerD;
//        this.CorrectAnswer = CorrectAnswer;
//        this.IsImageQuestion = IsImageQuestion;
//        this.CategoryID = CategoryID;
//    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public int getClassID()
    {
        return ClassID;
    }

    public void setClassID(int classID)
    {
        this.ClassID = classID;
    }

    public String getQuestionText()
    {
        return QuestionText;
    }

    public void setQuestionText(String questionText)
    {
        this.QuestionText = questionText;
    }

    public String getQuestionImage()
    {
        return QuestionImage;
    }

    public void setQuestionImage(String questionImage)
    {
        this.QuestionImage = questionImage;
    }

    public String getAnswerA()
    {
        return AnswerA;
    }

    public void setAnswerA(String answerA)
    {
        this.AnswerA = answerA;
    }

    public String getAnswerB()
    {
        return AnswerB;
    }

    public void setAnswerB(String answerB)
    {
        this.AnswerB = answerB;
    }

    public String getAnswerC()
    {
        return AnswerC;
    }

    public void setAnswerC(String answerC)
    {
        this.AnswerC = answerC;
    }

    public String getAnswerD()
    {
        return AnswerD;
    }

    public void setAnswerD(String answerD)
    {
        this.AnswerD = answerD;
    }

    public String getCorrectAnswer()
    {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer)
    {
        this.CorrectAnswer = correctAnswer;
    }

    public boolean isImageQuestion()
    {
        return IsImageQuestion;
    }

    public void setImageQuestion(boolean imageQuestion)
    {
        IsImageQuestion = imageQuestion;
    }

    public int getCategoryID()
    {
        return CategoryID;
    }

    public void setCategoryID(int categoryID)
    {
        this.CategoryID = categoryID;
    }
}
