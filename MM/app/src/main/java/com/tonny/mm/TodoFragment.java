package com.tonny.mm;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tonny.mm.Adapters.TaskAdapter;
import com.tonny.mm.Models.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Task> taskList;
    TaskAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    Dialog taskDialog;
    Spinner spinner;

    EditText task, task_date;
    Button addButton;

    String taskStatus;

    FloatingActionButton taskFab;

    private FirebaseFirestore db;


    public TodoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        taskFab = view.findViewById(R.id.tdFab);


        taskDialog  = new Dialog(getContext());
        taskDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        taskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTasks();
            }
        });

        //Initializing to do list components
        recyclerView = view.findViewById(R.id.toDoRecycler);
        swipeRefreshLayout = view.findViewById(R.id.taskSwipe);

        //Task RecyclerView population
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Swipe Refresh for news and latest posts
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);
                getTasks();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getTasks();

            }
        });
        return view;
    }

    private void addTasks(){

        taskDialog.setContentView(R.layout.add_task_dialog);
        spinner = taskDialog.findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.task_status,
                android.R.layout.simple_dropdown_item_1line);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskStatus =(String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        task = taskDialog.findViewById(R.id.editTitle);
        task_date = taskDialog.findViewById(R.id.editDate);
        addButton = taskDialog.findViewById(R.id.btnAdd);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDataToFirestore(task.getText().toString(), task_date.getText().toString(), taskStatus);
                taskDialog.dismiss();
                getTasks();

            }
        });

        taskDialog.show();

    }



    private void addDataToFirestore(String title, String task_date, String status) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference dbTasks = db.collection("To_Do_list");

        // adding our data to our courses object class.
        Task task = new Task(title, task_date, status);

        // below method is use to add data to Firebase Firestore.
        dbTasks.add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(getContext(), "Your task has been added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(getContext(), "Fail to add task \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTasks(){

        swipeRefreshLayout.setRefreshing(true);

        db.collection("To_Do_list").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            swipeRefreshLayout.setRefreshing(false);
                            taskList.clear();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Task t = d.toObject(Task.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                taskList.add(t);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifuDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            adapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
                Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
