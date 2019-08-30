package com.shashwatsupreme.sciencequiz.DBHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashwatsupreme.sciencequiz.Common.Common;
import com.shashwatsupreme.sciencequiz.Interface.HighScore;
import com.shashwatsupreme.sciencequiz.Interface.MyCallback;
import com.shashwatsupreme.sciencequiz.model.Question;
import com.shashwatsupreme.sciencequiz.model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dmax.dialog.SpotsDialog;

public class OnlineDBHelper
{
    FirebaseDatabase firebaseDatabase;
    Context context;

    DatabaseReference reference;

    public OnlineDBHelper(Context context, FirebaseDatabase firebaseDatabase)
    {
        this.firebaseDatabase = firebaseDatabase;
        this.context = context;
        reference = this.firebaseDatabase.getReference("ScienceQuiz");
    }

    private static OnlineDBHelper instance;

    public static synchronized OnlineDBHelper getInstance(Context context, FirebaseDatabase firebaseDatabase)
    {
        if(instance == null)
        {
            instance = new OnlineDBHelper(context, firebaseDatabase);
        }
        return instance;
    }

    public void readData(final MyCallback myCallback, String category, String classId)
    {
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setCancelable(false)
                .build();

        if(!dialog.isShowing())
            dialog.show();

        reference.child(category)
                .child("question")
                .child(classId)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        List<Question> questionList = new ArrayList<>();
                        for(DataSnapshot questionSnapshot:dataSnapshot.getChildren())
                            questionList.add(questionSnapshot.getValue(Question.class));
                        myCallback.setQuestionList(questionList);

                        if(dialog.isShowing())
                            dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    UserData userData = new UserData();
    public UserData readUserData(String category, String uid, String subject, String classid)
    {

        String path = Common.email;
        path = path.replace(".", "dot");
        path = path.replace("@", "attherateof");
        path = path.replace("_", "underscore");
        path = path.replace("#", "hashtag");
        path = path.replace("*", "asterisk");
        path = path.replace("-", "dash");

        reference.child(category)
                .child(path)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        userData = dataSnapshot.getValue(UserData.class);
                        Common.userData = userData;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        return userData;
    }
    public void writeUserData(String category, String uid, String subject, String classid, int newhighscore, boolean new_user)
    {
        String path = Common.email;
        path = path.replace(".", "dot");
        path = path.replace("@", "attherateof");
        path = path.replace("_", "underscore");
        path = path.replace("#", "hashtag");
        path = path.replace("*", "asterisk");
        path = path.replace("-", "dash");

        UserData newuser = new UserData();

        if(!new_user)
        {
            reference.child(category)
                    .child(path)
                    .child(subject.toLowerCase() + classid + "high")
                    .setValue(newhighscore);

            switch (Common.selectedCategory.getClassId())
            {
                case 6:
                    if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                        newuser = new UserData(newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                        newuser = new UserData(0, 0, 0, newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, Common.email, Common.name);
                    break;
                case 7:
                    if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                        newuser = new UserData(0, newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                        newuser = new UserData(0, 0, 0, 0, newhighscore, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                        newuser = new UserData(newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, newhighscore, 0, Common.email, Common.name);
                    break;
                case 8:
                    if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                        newuser = new UserData(0, 0, newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                        newuser = new UserData(0, 0, 0, 0, 0, newhighscore, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, newhighscore, Common.email, Common.name);
                    break;

            }
        }

        else
        {

            switch (Common.selectedCategory.getClassId())
            {
                case 6:
                    if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                        newuser = new UserData(newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                        newuser = new UserData(0, 0, 0, newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, Common.email, Common.name);
                    break;
                case 7:
                    if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                        newuser = new UserData(0, newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                        newuser = new UserData(0, 0, 0, 0, newhighscore, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                        newuser = new UserData(newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, newhighscore, 0, Common.email, Common.name);
                    break;
                case 8:
                    if(Common.selectedCategory.getName().equalsIgnoreCase("physics"))
                        newuser = new UserData(0, 0, newhighscore, 0, 0, 0, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("chemistry"))
                        newuser = new UserData(0, 0, 0, 0, 0, newhighscore, 0, 0, 0, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("biology"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, 0, newhighscore, 0, 0, 0, Common.email, Common.name);
                    if(Common.selectedCategory.getName().equalsIgnoreCase("mixed"))
                        newuser = new UserData(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, newhighscore, Common.email, Common.name);
                    break;

            }
            Common.userData = newuser;
//            Map<String, Object> uid_map = new HashMap<>();
//            uid_map.put("/Users/", path);
//            reference.push().updateChildren(uid_map);



            Map<String, Object> userDataMap = newuser.toMap();
            Map<String, Object> finalUserDataMap = new HashMap<>();
            finalUserDataMap.put("/Users/"+path, userDataMap);
            reference.updateChildren(finalUserDataMap);


        }
    }



}
