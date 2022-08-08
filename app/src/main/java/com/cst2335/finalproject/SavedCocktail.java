package com.cst2335.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SavedCocktail extends AppCompatActivity {
    private MyOpenHelper_Cocktail myOpener;
    private SQLiteDatabase myDatabase;
    private ListView myList;
    private ArrayList<Cocktail> savedList = new ArrayList<>();
    private MyListAdapter myAdapter;
    private TextView row;
    private DetailsFragment_Cocktail dFragment;
    private Bitmap bitm;
    private ImageView picIV;

    /**
     * the class shows the info that has saved after user saved the search cocktail
     * @param savedInstanceState a bundle used to save instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_cocktail);

        myOpener = new MyOpenHelper_Cocktail(this);
        myDatabase = myOpener.getWritableDatabase();
        Cursor history = myDatabase.rawQuery("Select * from " + MyOpenHelper_Cocktail.TABLE_NAME + ";", null);

        int nameIndex = history.getColumnIndex(MyOpenHelper_Cocktail.COL_Name);
        int picIndex = history.getColumnIndex(MyOpenHelper_Cocktail.COL_Pic);
        int instruIndex = history.getColumnIndex(MyOpenHelper_Cocktail.COL_Instruction);
        int ingre1Index = history.getColumnIndex(MyOpenHelper_Cocktail.COL_Ingredient1);
        int ingre2Index = history.getColumnIndex(MyOpenHelper_Cocktail.COL_Ingredient2);
        int ingre3Index = history.getColumnIndex(MyOpenHelper_Cocktail.COL_Ingredient3);
        int idIndex = history.getColumnIndex((MyOpenHelper_Cocktail.COL_ID));
        while(history.moveToNext()) {
            String name = history.getString(nameIndex);
            String pic = history.getString(picIndex);
            String instru = history.getString(instruIndex);
            String ingre1 = history.getString(ingre1Index);
            String ingre2 = history.getString(ingre2Index);
            String ingre3 = history.getString(ingre3Index);
            long id = history.getInt(idIndex);
            savedList.add(new Cocktail(name,pic,instru,ingre1,ingre2,ingre3,id));
        }
      //  Log.e("index", savedList.toString());

        history.close();
        row = findViewById(R.id.rowCocktail);
        myList = findViewById(R.id.savedList);
        myList.setAdapter(myAdapter = new MyListAdapter());
        myAdapter.notifyDataSetChanged();

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * to show dialog window, and fragment view on tablet and phone
             * @param parent adapterView
             * @param view view
             * @param position position of the saved item
             * @param id id of the saved item
             * @return false
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cocktail whatWasClicked = savedList.get(position);
               // whatWasClicked.getId();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SavedCocktail.this);
                alertDialogBuilder.setTitle("Do you want to delete this ？")
                        .setMessage("The selected row is:" + position + " " + "The database id is:" + whatWasClicked.getId())
                        .setNegativeButton("Negative", (dialog, click1)->{})
                        .setPositiveButton("Positive", (dialog, click2)->{
                            savedList.remove(position);
                            myAdapter.notifyDataSetChanged();
                            //remove data from database
                            myDatabase.delete(MyOpenHelper_Cocktail.TABLE_NAME,MyOpenHelper_Cocktail.COL_ID +" = ?", new String[]{Long.toString(whatWasClicked.getId())});
                        })
                        .create().show();
                return false;
            }
        });

        myList.setOnItemClickListener((list, view, position, id) ->{
            boolean isTablet = findViewById(R.id.fragmentLocation) != null;
            Bundle dataToPass = new Bundle();
            dataToPass.putString("name", savedList.get(position).getName());
            dataToPass.putString("pic",savedList.get(position).getUrl());
            dataToPass.putString("instruction",savedList.get(position).getInstruction());
            dataToPass.putString("ingre1",savedList.get(position).getIngre1());
            dataToPass.putString("ingre2",savedList.get(position).getIngre2());
            dataToPass.putString("ingre3",savedList.get(position).getIngre3());
            if(isTablet){
                dFragment = new DetailsFragment_Cocktail(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            else{ //isPhone
                Intent nextActivity = new Intent(this, EmptyActivity_Cocktail.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });
    }
    /**
     * this class helps user deal with the list view
     */
    public class MyListAdapter extends BaseAdapter {
        /**
         * get method to return number of items in the list
         * @return size of the list
         */
        public int getCount() { return savedList.size();}
        /**
         * get method to return the cocktail in a given position
         * @param position position number of the item
         * @return the cocktail name in the position
         */
        public Cocktail getItem(int position) { return savedList.get(position);}
        /**
         * get method to return the database id in a given position
         * @param position position number of the item
         * @return the database id in the position
         */
        public long getItemId(int position) { return position;}
        /**
         * get method that prepares the position to be displayed in the list view
         * @param position position
         * @return the view in the position
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.cocktailrow_layout, parent, false);
            row = rowView.findViewById(R.id.rowCocktail);
            row.setText(savedList.get(position).getName());
            return rowView;
        }
    }

}