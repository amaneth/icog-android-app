package com.example.amankassahun;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

private ImageView makersBtn;
private ImageView accBtn;
private ImageView solveItBtn;
private ImageView dieBtn;
private ImageView icogBtn;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advertisment_view, container, false);
        makersBtn= view.findViewById(R.id.makers_btn);
        accBtn= view.findViewById(R.id.acc_btn);
        solveItBtn= view.findViewById(R.id.solveit_btn);
        dieBtn= view.findViewById(R.id.die_btn);
        icogBtn= view.findViewById(R.id.icoggers_btn);
        makersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= DepartmentsActivity.newIntent(getActivity(),"Makers",0,null);
                startActivity(intent);
            }
        });
        solveItBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= DepartmentsActivity.newIntent(getActivity(),"SolveIt",2,null);
                startActivity(intent);
            }
        });
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= DepartmentsActivity.newIntent(getActivity(),"ACC",1,null);
                startActivity(intent);
            }
        });
        dieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= DepartmentsActivity.newIntent(getActivity(),"Design in Ethiopia",3,null);
                startActivity(intent);
            }
        });
        icogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("buff",QueryPreferences.getDept(getActivity()));
                if(QueryPreferences.getDept(getActivity()).equals("public")||QueryPreferences.getDept(getActivity()).equals("publican")){
                    Toast.makeText(getActivity(),"Sorry,this page is only for iCogger's",Toast.LENGTH_LONG).show();
                }else{
                Intent intent= DepartmentsActivity.newIntent(getActivity(),"icoggers",4,null);
                startActivity(intent);}
            }
        });


        return view;
    }

}
