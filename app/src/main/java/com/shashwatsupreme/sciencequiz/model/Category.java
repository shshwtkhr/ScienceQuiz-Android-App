package com.shashwatsupreme.sciencequiz.model;

public class Category
{
    private int id;
    private String name,image;
    private int classId;

    public Category()
    {
    }

    public Category(int id, String name, String image, int classId)
    {
        this.id = id;
        this.name = name;
        this.image = image;
        this.classId = classId;
    }

    public int getClassId()
    {
        return classId;
    }

    public void setClassId(int classId)
    {
        this.classId = classId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }
}
