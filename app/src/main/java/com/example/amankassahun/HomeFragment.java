package com.example.amankassahun;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.CONNECTIVITY_SERVICE;


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
        final FirebaseUser firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        final boolean aman= QueryPreferences.getDept(getActivity()).equals("public");
        makersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= DepartmentsActivity.newIntent(getActivity(),"Makers",0,null);

                if(isNetworkAvailableAndConnected()){
                startActivity(intent);}
                else{
                  showDialog(intent);
                }
            }
        });
        solveItBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firebaseuser==null||aman)
                { AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("What you are Looking for, SolveIt competition or SolvIt page?")
                            .setCancelable(false)
                            .setNeutralButton("Competition", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getActivity(),SolvitCompetationActivity.class));

                                }
                            }).setNegativeButton("Page", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent= DepartmentsActivity.newIntent(getActivity(),"SolveIt",2,null);
                            startActivity(intent);
                        }
                    })
                            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();

                }else{
                    Intent intent2= DepartmentsActivity.newIntent(getActivity(),"SolveIt",2,null);
                    if(isNetworkAvailableAndConnected()){
                        startActivity(intent2);}
                    else{
                        showDialog(intent2);
                    }
                }

            }
        });
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3= DepartmentsActivity.newIntent(getActivity(),"ACC",1,null);
                if(isNetworkAvailableAndConnected()){
                    startActivity(intent3);}
                else{
                    showDialog(intent3);
                }
            }
        });
        dieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4= DepartmentsActivity.newIntent(getActivity(),"Design in Ethiopia",3,null);
                if(isNetworkAvailableAndConnected()){
                    startActivity(intent4);}
                else{
                   showDialog(intent4);
                }
            }
        });
        icogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(QueryPreferences.getDept(getActivity()).equals("public")||QueryPreferences.getDept(getActivity()).equals("publican")){
                    Toast.makeText(getActivity(),"Sorry,this page is only for iCogger's",Toast.LENGTH_LONG).show();
                }else{
                Intent intent5= DepartmentsActivity.newIntent(getActivity(),"icoggers",4,null);
                    if(isNetworkAvailableAndConnected()){
                        startActivity(intent5);}
                   else{
                        showDialog(intent5);
                    }}
            }
        });


        return view;
    }
    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
    private void showDialog(final Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Check your internet connection please!")
                .setCancelable(false)
                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isNetworkAvailableAndConnected()){
                            startActivity(intent);}
                        else{
                            showDialog(intent);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }

}
