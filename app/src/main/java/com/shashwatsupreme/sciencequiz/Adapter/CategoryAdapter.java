package com.shashwatsupreme.sciencequiz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shashwatsupreme.sciencequiz.Common.Common;
import com.shashwatsupreme.sciencequiz.QuestionActivity;
import com.shashwatsupreme.sciencequiz.R;
import com.shashwatsupreme.sciencequiz.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>
{
    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories)
    {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_category_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {
        myViewHolder.text_category_name.setText(categories.get(i).getName());
    }



    @Override
    public int getItemCount()
    {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CardView card_category;
        TextView text_category_name;
        RadioGroup radio_card_category_class;
        RadioButton rdo_class_selected;


        public MyViewHolder(@NonNull final View itemView)
        {
            super(itemView);
            card_category = (CardView) itemView.findViewById(R.id.card_category);
            text_category_name = itemView.findViewById(R.id.text_category_name);
            card_category.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    radio_card_category_class = itemView.findViewById(R.id.radio_class);
                    int selectedId = radio_card_category_class.getCheckedRadioButtonId();
                    rdo_class_selected = itemView.findViewById(selectedId);


                    if(selectedId!=-1)
                    {
                        Common.selectedCategory = categories.get(getAdapterPosition()); // Assign current category
                        Common.selectedCategory.setClassId(Integer.parseInt(rdo_class_selected.getText().toString()));
                        Intent intent = new Intent(context, QuestionActivity.class);
                        context.startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(context, "First select a class!", Toast.LENGTH_SHORT).show();
                    }
                    //Add the code(using MaterialStyledDialog.Builder to ask for class of the user here


                }
            });
        }
    }
}
