package com.example.amankassahun;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aman on 10/1/2018.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {
    public List<Comments> commentsList;
    public List<Commentor> commentorList;
    public Context context;
    public CommentsRecyclerAdapter(List<Comments> commentsList,List<Commentor> commentorList){
        this.commentsList = commentsList;
        this.commentorList= commentorList;
    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item,parent,false);
        context= parent.getContext();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsRecyclerAdapter.ViewHolder holder, int position) {
        String commentMessage=null,commentorImage=null,commentorName=null;
holder.setIsRecyclable(false);
        if(commentsList.get(position)!=null){
         commentMessage= commentsList.get(position).getMessage();}
        if(commentorList.get(position)!=null){
            commentorImage= commentorList.get(position).getImage();
         commentorName= commentorList.get(position).getName();
        }
        holder.setComment_message(commentMessage);
        holder.setUserData(commentorName,commentorImage);
    }

    @Override
    public int getItemCount() {
        if(commentsList !=null){
            return commentsList.size();
        }else{
        return 0;}
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView comment_message;
        private TextView usernameView;
        private CircleImageView profileImage;
        public ViewHolder(View itemView){
        super(itemView);
            mView= itemView;
        }
        public  void setComment_message(String message){
            comment_message= mView.findViewById(R.id.comment_message);
            comment_message.setText(message);
        }
        public void setUserData(String userName,String downloadUrl){
            usernameView= mView.findViewById(R.id.comment_user_name);
            profileImage=mView.findViewById(R.id.comment_image);

            if(userName!=null) {

                    usernameView.setText(userName);
                RequestOptions placeHolderRequest = new RequestOptions();
                placeHolderRequest = placeHolderRequest.placeholder(R.drawable.default_image);
                Glide.with(context).setDefaultRequestOptions(placeHolderRequest).load(downloadUrl).into(profileImage);
            }
            else{
                Glide.with(context).load(R.drawable.default_image).into(profileImage);
                usernameView.setText("Unknown");
            }

        }

    }

}
