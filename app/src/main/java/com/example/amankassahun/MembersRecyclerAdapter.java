package com.example.amankassahun;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Aman on 10/1/2018.
 */

public class MembersRecyclerAdapter extends RecyclerView.Adapter<MembersRecyclerAdapter.ViewHolder> {
    public List<User> membersList;
    public Context context;
    private String authorityBull;
     Set<String> current_user_authority;
    List<String> blogger_authority ;
    String departmentata;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    String current_user_id = null;
    private boolean departmentUpdate;

    public MembersRecyclerAdapter(List<User> membersList){
        this.membersList = membersList;
    }

    @Override
    public MembersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_item,parent,false);
        context= parent.getContext();
        return new MembersRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MembersRecyclerAdapter.ViewHolder holder, final int position) {
        firebaseFirestore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        holder.setIsRecyclable(false);

        final String name= membersList.get(position).getName();
        String email= membersList.get(position).getEmail();
        String department = membersList.get(position).getDepartment();
        List<String> authorityOfMember=membersList.get(position).getAuthority();
        String authorityofMember="";
        for(String sa:authorityOfMember){
            switch (sa){
                case "main admin":
                    sa="Admin";
                    break;
                case "acc admin":
                    sa="AA";
                    break;
                case "makers admin":
                    sa="MA";
                    break;
                case "solvit admin":
                    sa="SA";
                    break;
                case "die admin":
                    sa="DA";
                    break;
                case "x":
                    sa="";
                    break;
                default:
                    sa="";
                    break;

            }
            authorityofMember=authorityofMember+sa+",";

        }

        final String user_id= membersList.get(position).getId();
        current_user_id=mAuth.getCurrentUser().getUid();


        holder.setMembers(name,email,department,authorityofMember,position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id!=null){
                    if(!QueryPreferences.getDept(context).equals("public")){

                        final String blogger_name = membersList.get(position).getName();



                                    current_user_authority = QueryPreferences.getAuthority(context);


                                 blogger_authority=membersList.get(position).getAuthority();
                                  departmentata=membersList.get(position).getDepartment();

                                    boolean aman = current_user_authority.contains("x")&&current_user_authority.size()<=1 ;
                                    if (blogger_authority == null) {
                                        blogger_authority = new ArrayList<>();
                                        blogger_authority.add("x");
                                    }

                            if (!aman) {
                                showAddAdminDialog(current_user_authority, blogger_name, user_id,blogger_authority,departmentata);
                            }


                    }
            }}
        });
        if(membersList.get(position).getDepartment().equals("-")){
        holder.mView.setBackgroundColor(ContextCompat.getColor(context,R.color.icogGreen));}
        else{
            holder.mView.setBackgroundColor(ContextCompat.getColor(context,R.color.icogOrange));
        }
    }
    public void showAddAdminDialog(final Set<String> authority, final String person, final String user_id, List<String> member_authority,String deorta) {
        View checkBoxView=null;
        final List<String> authorityEdit= member_authority;
            checkBoxView = View.inflate(context, R.layout.activity_admin_choice, null);
            final CheckBox admincheckBox = checkBoxView.findViewById(R.id.admin_radio_btn);
            final CheckBox acc = checkBoxView.findViewById(R.id.acc_admin);
            final CheckBox makers = checkBoxView.findViewById(R.id.makers_admin);
            final CheckBox solveIt = checkBoxView.findViewById(R.id.solveit_admin);
            final CheckBox die = checkBoxView.findViewById(R.id.die_admin);
        final CheckBox icogger= checkBoxView.findViewById(R.id.icogger_check_box);
        Log.d("kassahun","author= "+authority);
        for(String s:authority){
            switch (s){
                case "main admin":
                    Log.d("kassahun","author= "+"exc");
                    admincheckBox.setVisibility(View.VISIBLE);
                    acc.setVisibility(View.VISIBLE);
                    makers.setVisibility(View.VISIBLE);
                    solveIt.setVisibility(View.VISIBLE);
                    die.setVisibility(View.VISIBLE);
                    icogger.setVisibility(View.VISIBLE);
                    break;
                case "acc admin":
                    acc.setVisibility(View.VISIBLE);
                    break;
                case "makers admin":
                    makers.setVisibility(View.VISIBLE);
                    break;
                case "solvit admin":
                    solveIt.setVisibility(View.VISIBLE);
                    break;
                case "die admin":
                    die.setVisibility(View.VISIBLE);
                    break;
                default:
                    Log.d("kassahun","author= "+"def");
                    break;

            }
        }
        if(deorta.equals("iCogger")){
            icogger.setChecked(true);
        }
 for(String s:member_authority){
     Log.d("kassahun","authority="+s+",");
     switch (s){
         case "main admin":
             admincheckBox.setChecked(true);
             acc.setChecked(true);
             makers.setChecked(true);
             solveIt.setChecked(true);
             die.setChecked(true);
             break;
         case "acc admin":
             acc.setChecked(true);
             break;
         case "makers admin":
             Log.d("kassahun","authority="+"excuted");
             makers.setChecked(true);
             break;
         case "solvit admin":
             solveIt.setChecked(true);
             break;
         case "die admin":
             die.setChecked(true);
             break;
         default:
             admincheckBox.setChecked(false);
             acc.setChecked(false);
             makers.setChecked(false);
             solveIt.setChecked(false);
             die.setChecked(false);
             break;

     }
 }

            admincheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        authorityEdit.add("main admin");
                        acc.setChecked(true);
                        makers.setChecked(true);
                        solveIt.setChecked(true);
                        die.setChecked(true);
                    }else {
                        authorityEdit.remove("main admin");
                        acc.setChecked(false);
                        makers.setChecked(false);
                        solveIt.setChecked(false);
                        die.setChecked(false);
                    }

                }
            });
            acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        authorityEdit.add("acc admin");
                    }else {
                        authorityEdit.remove("acc admin");
                    }
                }
            });
            makers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        authorityEdit.add("makers admin");
                    }else {
                        authorityEdit.remove("makers admin");
                    }
                }
            });
            solveIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        authorityEdit.add("solvit admin");
                    }else {
                        authorityEdit.remove("solvit admin");
                    }
                }
            });
            die.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        authorityEdit.add("die admin");
                    }else {
                        authorityEdit.remove("die admin");
                    }
                }
            });
        icogger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    departmentUpdate=true;
                }
                else{
                    departmentUpdate=false;
                }
            }
        });
String message;
        Log.d("kassahun","/"+user_id+"vs"+current_user_id);
        if(current_user_id.equals(user_id)){
            message="Demote yourself to admin";
        }
        else {
            message="Promote or demote "+person+" to admin";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setView(checkBoxView)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final DocumentReference docRef=firebaseFirestore.collection("Users").document(user_id);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        Map<String,Object> updates= new HashMap<>();

if(acc.isChecked()&&makers.isChecked()&&solveIt.isChecked()&&die.isChecked()){
    Log.d("kassahun",authorityEdit.toString());
    if(!authorityEdit.contains("main admin")){
    authorityEdit.add("main admin");}
    authorityEdit.remove("solvit admin");
    authorityEdit.remove("acc admin");

    authorityEdit.remove("makers admin");
    authorityEdit.remove("die admin");
}
                                        updates.put("authority", authorityEdit);
                                        if(departmentUpdate){
                                            updates.put("department","iCogger");
                                        }else{
                                            updates.put("department","-");
                                        }


                                        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(current_user_id.equals(user_id)){
                                                    Set<String> set= new HashSet<>();
                                                    set.addAll(authorityEdit);
                                                 QueryPreferences.setAuthority(context,set);
                                                    if(departmentUpdate){
                                                        QueryPreferences.setDept(context,"iCogger");
                                                    }
                                                    else{
                                                        QueryPreferences.setDept(context,"-");
                                                    }
                                                }
                                                Toast.makeText(context,"Promotion or demotion successfully updated!",Toast.LENGTH_LONG).show();
                                                notifyDataSetChanged();
                                            }
                                        });




                                    }
                                }
                                else{
                                    String error= task.getException().getMessage();
                                    Toast.makeText(context,"ERROR: "+error,Toast.LENGTH_LONG).show();
                                }

                            }
                        });                         }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }
    @Override
    public int getItemCount() {
        if(membersList !=null){
            return membersList.size();
        }else{
            return 0;}
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView nameField;
        private TextView phoneField;
        private TextView deptField;
        private TextView authField;
        private TextView numField;
        public ViewHolder(View itemView){
            super(itemView);
            mView= itemView;
        }
        public  void setMembers(String name,String phone,String dept,String autho,int positiond){
            nameField= mView.findViewById(R.id.member_name);
            phoneField=mView.findViewById(R.id.member_phone_no);
            deptField= mView.findViewById(R.id.member_department);
            authField= mView.findViewById(R.id.member_authority);
            numField= mView.findViewById(R.id.listNum);
            positiond=positiond+1;
            numField.setText(""+positiond);
            nameField.setText(name);
            phoneField.setText(phone);
            deptField.setText(dept);

            authField.setText(autho);

        }

    }

}
