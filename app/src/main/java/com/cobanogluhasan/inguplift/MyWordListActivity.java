package com.cobanogluhasan.inguplift;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.SharedElementCallback;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cobanogluhasan.inguplift.Adapter.VocabularyListAdapter;
import com.cobanogluhasan.inguplift.Helper.MyButtonClickListener;
import com.cobanogluhasan.inguplift.Helper.SwipeHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MyWordListActivity extends AppCompatActivity {


    private static final String TAG = "MyWordListActivity";

    private ArrayList<Word> wordList = new ArrayList<>();
    FloatingActionButton floatingActionButton;

    RecyclerView vocabularyListR;
    VocabularyListAdapter adapter;

    CoordinatorLayout coordinatorLayout;
    private Word deletedWord = null;
    int position;

    DatabaseHelper mDatabaseHelper;
    SharedPreferences langPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word_list);

        loadBanner(this);


        langPreferences = this.getSharedPreferences("com.cobanogluhasan.inguplift", Context.MODE_PRIVATE);

        String defaultLang = langPreferences.getString("language", "");


        if (defaultLang.equals("turkish")) {

            getSupportActionBar().setTitle("Kelime Listem");

        } else {
            getSupportActionBar().setTitle("My Word List");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDatabaseHelper = new DatabaseHelper(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        initRecyclerView();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent vocabIntent = new Intent(getApplicationContext(), VocabularyListActivity.class);
                startActivity(vocabIntent);

            }
        });


    }

    private void initRecyclerView() {
        Cursor data = mDatabaseHelper.getData();

        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList

            Word mWord = new Word();
            mWord.setId(data.getLong(0));
            mWord.setWord(data.getString(1));
            mWord.setSynonym(data.getString(2));
            mWord.setDefinition(data.getString(3));
            mWord.setExample(data.getString(4));
            wordList.add(mWord);

        }

        Log.e("WordList", "size" + wordList.size());


        vocabularyListR = (RecyclerView) findViewById(R.id.vocabularyListRecycler);
        adapter = new VocabularyListAdapter(this, wordList);

        vocabularyListR.setAdapter(adapter);
        vocabularyListR.setLayoutManager(new LinearLayoutManager(this));

      //  new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(vocabularyListR);
        initSwipeHelper();




    }



 public void initSwipeHelper() {
     SwipeHelper swipeHelper = new SwipeHelper(this, vocabularyListR, 200) {
         @Override
         public void instantiateMyButton(final RecyclerView.ViewHolder viewHolder, List<SwipeHelper.MyButton> buffer) {

             buffer.add(new MyButton(MyWordListActivity.this,
                     "Delete",
                     35,
                     R.drawable.ic_delete,
                     Color.TRANSPARENT,
                     new MyButtonClickListener() {
                         @Override
                         public void onClick(int pos) {
                             position = viewHolder.getAdapterPosition();
                             Log.e("onSwiped:  ", " " + position);
                             deletedWord = wordList.get(position);


                             wordList.remove(position);
                          //   adapter.notifyDataSetChanged();
                             adapter.notifyItemRemoved(position);


                             showSnackbar(deletedWord.getId());

                             Log.i("onclick:delete", Integer.toString(position));

                         }
                     }));
             buffer.add(new MyButton(MyWordListActivity.this,
                     "Edit",
                     35,
                     R.drawable.ic_edit,
                     Color.TRANSPARENT,
                     new MyButtonClickListener() {
                         @Override
                         public void onClick(int pos) {
                             position = viewHolder.getAdapterPosition();
                            Intent intent = new Intent(MyWordListActivity.this, UpdateActivity.class);
                             intent.putExtra("id" , wordList.get(position).getId());
                             intent.putExtra("word" , wordList.get(position).getWord());
                             intent.putExtra("synonym" , wordList.get(position).getSynonym());
                             intent.putExtra("definition" , wordList.get(position).getDefinition());
                             intent.putExtra("example" , wordList.get(position).getExample());

                             startActivity(intent);

                         }
                     }));



         }

     };


    }


/*
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            position = viewHolder.getAdapterPosition();
            Log.e("onSwiped:  ", " " + position);
            deletedWord = wordList.get(position);


            wordList.remove(position);
            adapter.notifyDataSetChanged();

            showSnackbar(deletedWord.getId());

            Log.i("onswiped", Integer.toString(position));

        }
    };

*/




    public void showSnackbar(final long id) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item Deleted.", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {



                    @Override
                    public void onClick(View v) {
                        if(deletedWord!=null){
                            wordList.add(deletedWord);
                        }

                        adapter.notifyDataSetChanged();

                    }

                });

        snackbar.show();

        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);

                Log.e("Snackbar","onshown");
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                Log.e("Snackbar","ondismissed");

                mDatabaseHelper.deleteData(id);
            }
        });



    }


    public void loadBanner(Context context) {
        //noinspection deprecation
        MobileAds.initialize(context, context.getResources().getString(R.string.admob_app_id));

        AdView mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

    }


}