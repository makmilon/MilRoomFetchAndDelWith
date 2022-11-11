package com.milon.paythroughproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>
{
    List<User> users;

    public myadapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @NotNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myadapter.myviewholder holder, int position) {
        holder.recid.setText(String.valueOf(users.get(position).getUid()));
        holder.recname.setText(users.get(position).getName());
        holder.recemail.setText(users.get(position).getEmail());
        holder.recage.setText(users.get(position).getAge());
        holder.recgender.setText(users.get(position).getGender());
        holder.recphno.setText(users.get(position).getPhone());
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase db = Room.databaseBuilder(holder.recid.getContext(),
                        AppDatabase.class, "room_db").allowMainThreadQueries().build();
                UserDao userDao = db.userDao();
                // this is to delete the record from room database
                userDao.deleteById(users.get(position).getUid());
                // this is to delete the record from Array List which is the source of recview data
                users.remove(position);

                //update the fresh list of ArrayList data to recview
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView recid,recname, recemail, recage, recgender, recphno;
        ImageView delbtn;
        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            recid=itemView.findViewById(R.id.recid);
            recname=itemView.findViewById(R.id.recName);
            recemail=itemView.findViewById(R.id.recEmail);
            recage=itemView.findViewById(R.id.recAge);
            recgender=itemView.findViewById(R.id.recGender);
            recphno=itemView.findViewById(R.id.recPhone);
            delbtn=itemView.findViewById(R.id.delBtn);
        }
    }
}

