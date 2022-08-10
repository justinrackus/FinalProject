package com.cst2335.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


public class DetailsFragment_Cocktail extends Fragment {
    MyOpenHelper_Cocktail myOpener;
    SQLiteDatabase myDatabase;
    Bitmap bitm;
    View result;
    ImageView picIV;
    String picF;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * lll
     * @return result
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle dataFromActivity = getArguments();
        //  long id = dataFromActivity.getLong("id");

        result = inflater.inflate(R.layout.fragment_details_cocktail, container, false);

        myOpener = new MyOpenHelper_Cocktail(result.getContext());
        myDatabase = myOpener.getWritableDatabase();

        TextView name = result.findViewById(R.id.cocktailName_frag);
        String nameF = dataFromActivity.getString("name");
        name.setText(nameF);

        picIV = result.findViewById(R.id.cocktailPic_frag);
        picF = dataFromActivity.getString("pic");
        Log.e("rr",picF);
        DetailsCocktail dt = new DetailsCocktail();
        dt.execute(picF);

        TextView instruction = result.findViewById(R.id.instruction_frag);
        String instructionF = dataFromActivity.getString("instruction");
        instruction.setText("Instruction: " + instructionF);

        TextView ingre1 = result.findViewById(R.id.ingre1_frag);
        String ingre1F = dataFromActivity.getString("ingre1");
        ingre1.setText("1." + ingre1F);

        TextView ingre2 = result.findViewById(R.id.ingre2_frag);
        String ingre2F = dataFromActivity.getString("ingre2");
        ingre2.setText("2." + ingre2F);

        TextView ingre3 = result.findViewById(R.id.ingre3_frag);
        String ingre3F = dataFromActivity.getString("ingre3");
        ingre3.setText("3." + ingre3F);

        Button backBtn = result.findViewById(R.id.backBtn_frag);
        backBtn.setOnClickListener(clk -> {
            Intent goBack = new Intent(getActivity(), MainActivity_Cocktail.class);
            startActivity(goBack);
        });
        Button saveBtn = result.findViewById(R.id.saveBtn_frag);
        saveBtn.setOnClickListener((View clk) -> {
            String[] args = {nameF};
            Cursor c = myDatabase.rawQuery("SELECT * FROM MyData WHERE Name = ?", args);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            if(c.getCount() != 0){
                alertDialogBuilder.setTitle("Item already saved")
                        .setMessage("Please check saved list")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            /**
                             *
                             * @param dialog
                             * @param which
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }})
                        .create().show();
            }
            else {
                //save to database
                ContentValues newRow = new ContentValues();
                newRow.put(MyOpenHelper_Cocktail.COL_Name, nameF);
                newRow.put(MyOpenHelper_Cocktail.COL_Pic, picF);
                newRow.put(MyOpenHelper_Cocktail.COL_Instruction, instructionF);
                newRow.put(MyOpenHelper_Cocktail.COL_Ingredient1, ingre1F);
                newRow.put(MyOpenHelper_Cocktail.COL_Ingredient2, ingre2F);
                newRow.put(MyOpenHelper_Cocktail.COL_Ingredient3, ingre3F);
                long id = myDatabase.insert(MyOpenHelper_Cocktail.TABLE_NAME, null, newRow);
                Snackbar snackbar1 = Snackbar.make(saveBtn, "Item Saved", Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.snackbar_undo), new View.OnClickListener() {
                            /**
                             *
                             * @param v .
                             */
                            @Override
                            public void onClick(View v) {
                                myDatabase.delete(MyOpenHelper_Cocktail.TABLE_NAME,
                                        MyOpenHelper_Cocktail.COL_ID +" = ?",
                                        new String[]{Long.toString(id)});
                                Toast.makeText(getActivity(),"Item Removed",Toast.LENGTH_LONG).show();
                            }
                        });
                snackbar1.show();
            }
        });
        // Inflate the layout for this fragment
        return result;
    }

/*
    RequestListener mRequestListener = new RequestListener() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            Log.d("TAG", "onException: " + e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
            picIV.setImageResource(R.mipmap.ic_launcher);
            return false;
        }
        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            Log.d("TAG2",  "model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }
    };
*/


    class DetailsCocktail extends AsyncTask<String, Integer, String> {
        /**
         *
         * @param urls
         * @return
         */
        public String doInBackground(String... urls) {
            try {
                URL picUrl = new URL(urls[0]);
                HttpURLConnection urlConn1 = (HttpURLConnection) picUrl.openConnection();
                // urlConn1.setDoInput(true);
                urlConn1.connect();
                // urlConn1.setReadTimeout(60000);
                // urlConn1.setConnectTimeout(60000);
                String filename = picF.replaceAll("-","").replaceAll("/","_").replaceAll(":","_").toUpperCase(Locale.ROOT);
                InputStream inputStream = urlConn1.getInputStream();;
                bitm = BitmapFactory.decodeStream(inputStream);
                FileOutputStream outputStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                bitm.compress(Bitmap.CompressFormat.PNG,80,outputStream);
                outputStream.flush();
                outputStream.close();
                //urlConn1.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Done";
        }

        /**
         *
         * @param string
         */
        public void onPostExecute(String string) {
            super.onPostExecute(string);
            picIV.setImageBitmap(bitm);
        }
    }

}
