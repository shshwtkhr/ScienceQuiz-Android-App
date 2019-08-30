package com.shashwatsupreme.sciencequiz.model;

import java.util.HashMap;
import java.util.Map;

public class UserData
{
    private int physics6high;
    private int physics7high;
    private int physics8high;
    private int chemistry6high;
    private int chemistry7high;
    private int chemistry8high;
    private int biology6high;
    private int biology7high;
    private int biology8high;
    private int mixed6high;
    private int mixed7high;
    private int mixed8high;

    private String emailid;
    private String name;

    public UserData()
    {
    }

    public UserData(int physics6high, int physics7high, int physics8high, int chemistry6high, int chemistry7high, int chemistry8high, int biology6high, int biology7high, int biology8high, int mixed6high, int mixed7high, int mixed8high, String emailid, String name)
    {
        this.physics6high = physics6high;
        this.physics7high = physics7high;
        this.physics8high = physics8high;
        this.chemistry6high = chemistry6high;
        this.chemistry7high = chemistry7high;
        this.chemistry8high = chemistry8high;
        this.biology6high = biology6high;
        this.biology7high = biology7high;
        this.biology8high = biology8high;
        this.mixed6high = mixed6high;
        this.mixed7high = mixed7high;
        this.mixed8high = mixed8high;
        this.emailid = emailid;
        this.name = name;
    }

    public int getPhysics6high()
    {
        return physics6high;
    }

    public void setPhysics6high(int physics6high)
    {
        this.physics6high = physics6high;
    }

    public int getPhysics7high()
    {
        return physics7high;
    }

    public void setPhysics7high(int physics7high)
    {
        this.physics7high = physics7high;
    }

    public int getPhysics8high()
    {
        return physics8high;
    }

    public void setPhysics8high(int physics8high)
    {
        this.physics8high = physics8high;
    }

    public int getChemistry6high()
    {
        return chemistry6high;
    }

    public void setChemistry6high(int chemistry6high)
    {
        this.chemistry6high = chemistry6high;
    }

    public int getChemistry7high()
    {
        return chemistry7high;
    }

    public void setChemistry7high(int chemistry7high)
    {
        this.chemistry7high = chemistry7high;
    }

    public int getChemistry8high()
    {
        return chemistry8high;
    }

    public void setChemistry8high(int chemistry8high)
    {
        this.chemistry8high = chemistry8high;
    }

    public int getBiology6high()
    {
        return biology6high;
    }

    public void setBiology6high(int biology6high)
    {
        this.biology6high = biology6high;
    }

    public int getBiology7high()
    {
        return biology7high;
    }

    public void setBiology7high(int biology7high)
    {
        this.biology7high = biology7high;
    }

    public int getBiology8high()
    {
        return biology8high;
    }

    public void setBiology8high(int biology8high)
    {
        this.biology8high = biology8high;
    }

    public int getMixed6high()
    {
        return mixed6high;
    }

    public void setMixed6high(int mixed6high)
    {
        this.mixed6high = mixed6high;
    }

    public int getMixed7high()
    {
        return mixed7high;
    }

    public void setMixed7high(int mixed7high)
    {
        this.mixed7high = mixed7high;
    }

    public int getMixed8high()
    {
        return mixed8high;
    }

    public void setMixed8high(int mixed8high)
    {
        this.mixed8high = mixed8high;
    }

    public String getEmailid()
    {
        return emailid;
    }

    public void setEmailid(String emailid)
    {
        this.emailid = emailid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", emailid);
        userMap.put("physics6high", physics6high);
        userMap.put("physics7high", physics7high);
        userMap.put("physics8high", physics8high);
        userMap.put("chemistry6high", chemistry6high);
        userMap.put("chemistry7high", chemistry7high);
        userMap.put("chemistry8high", chemistry8high);
        userMap.put("biology6high", biology6high);
        userMap.put("biology7high", biology7high);
        userMap.put("biology8high", chemistry8high);
        userMap.put("mixed6high", mixed6high);
        userMap.put("mixed7high", mixed7high);
        userMap.put("mixed8high", mixed8high);


        return userMap;
    }
}