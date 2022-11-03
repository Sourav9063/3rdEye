package com.example.thirdeye.video_meeting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirdeye.R;
import com.example.thirdeye.user_registration.User;
import com.example.thirdeye.video_meeting.listeners.UserListener;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
    private List<User> users;
    private UserListener userListener;
    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.container_user,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
    holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView textFirstChar,textUserName,textEmail;
        ImageView imageVideoCall;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textFirstChar = itemView.findViewById(R.id.textFirstChar);
            textUserName = itemView.findViewById(R.id.textUsername);
            textEmail = itemView.findViewById(R.id.textEmail);
            imageVideoCall = itemView.findViewById(R.id.imageVideoCall);
        }
        void setUserData(User user)
        {
            textFirstChar.setText(user.fullName.substring(0,1));
            textUserName.setText(user.fullName);
            textEmail.setText(user.email);
            imageVideoCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userListener.initiateVideoMeeting(user);
                }
            });

        }
    }



}
