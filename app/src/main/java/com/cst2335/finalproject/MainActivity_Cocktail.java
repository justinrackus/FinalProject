package com.cst2335.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_Cocktail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private ListView myList;
    private ArrayList<Cocktail> cocktailList;
    private MyListAdapter_Cocktail adapter;
    private TextView row;
    private EditText searchText;
    private DetailsFragment_Cocktail dFragment;
    //private String input;
    private String name;
    private String pic;
    private String instruction;
    private String ingredient1;
    private String ingredient2;
    private String ingredient3;
    private ProgressBar pb;
    private Button searchCocktail;
    private Bitmap bitmap;

    /**
     * onCreate method of the activity, will be called when the activity starts
     * it will initialize the activity
     * @param savedInstanceState is the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cocktail);

        SharedPreferences sp = getSharedPreferences("userInput", MODE_PRIVATE);
        searchText = findViewById(R.id.searchName);
        searchText.setText(sp.getString("input",""));
        //input = searchText.getText().toString();
        row = findViewById(R.id.rowCocktail);
        cocktailList = new ArrayList<>();
        myList = findViewById(R.id.cocktailLV);
        adapter = new MyListAdapter_Cocktail(this, cocktailList);
        myList.setAdapter(adapter);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
//*************************************************************************************************
        searchCocktail = findViewById(R.id.searchBtn);
        searchCocktail.setOnClickListener(new View.OnClickListener() {
            /**
             * set click listener on search button to search for the cocktail from
             * database
             * @param v the view of search result
             */
            @Override
            public void onClick(View v) {
                if (!searchText.getText().toString().isEmpty()) {
                    String input = searchText.getText().toString();
                    Log.e("search", input);
                    MyHTTPRequest_Cocktail req = new MyHTTPRequest_Cocktail();
                    req.execute("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + input);
                    Toast.makeText(getApplicationContext(), "searching.... ", Toast.LENGTH_SHORT).show();
                }
            }
        });
//*************************************************************************************************
        Button savedBtn = findViewById(R.id.savedBtn);
        savedBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * save button will lead the user goes to the saved cocktail page
             * @param v of save result
             */
            @Override
            public void onClick(View v) {
                Intent goToSaved = new Intent(MainActivity_Cocktail.this, SavedCocktail.class);
                startActivity(goToSaved);
            }
        });
//*************************************************************************************************
        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //For bottomNavigationBar
        BottomNavigationView bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);
//**************************************************************************************************
        //fragment part
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        /**
         * set item click listener on the list to show detail info. of the selected, the info. will
         * be shown in the fragment
         */
        myList.setOnItemClickListener((list, view, position, id) ->{
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString("name", cocktailList.get(position).getName());
            dataToPass.putString("pic",cocktailList.get(position).getUrl());
            dataToPass.putString("instruction",cocktailList.get(position).getInstruction());
            dataToPass.putString("ingre1",cocktailList.get(position).getIngre1());
            dataToPass.putString("ingre2",cocktailList.get(position).getIngre2());
            dataToPass.putString("ingre3",cocktailList.get(position).getIngre3());

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
    /*
    Bitmap bitmap;
    public Bitmap returnBitmap(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapHelper.getBitmap(url, MainActivity_Cocktail.this);
            }
        }).start();
        return bitmap;
    }
    *//////////
//**************************************************************************************************

    /**
     * this class will start the AsyncTask and pull the cocktail info from the given website
     */
    private class MyHTTPRequest_Cocktail extends AsyncTask<String, Integer, ArrayList<Cocktail>> {
        protected void onPreExecute(){
            super.onPreExecute();
            cocktailList.clear();
            String input = searchText.getText().toString();
        }

        /**
         * doInBackground will pull all cocktail info. from the website
         * @param args arg list of the method
         * @return returns a string telling the user task is complete
         */
        public ArrayList<Cocktail> doInBackground(String ... args){
            try{
                //create a URL object of what server to contact
                URL url = new URL(args[0]);
                publishProgress(10);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(20);
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                publishProgress(30);
                //Jason reading
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                publishProgress(45);
                StringBuilder sb = new StringBuilder();
                publishProgress(50);
                String line = null;
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                publishProgress(60);
                String result = sb.toString();
                JSONObject cocktailItems = new JSONObject(result);
                JSONArray cocktailArr = cocktailItems.getJSONArray("drinks");
                publishProgress(70);
                for(int i = 0; i < cocktailArr.length(); i++){
                    JSONObject objFromArr = cocktailArr.getJSONObject(i);
                    name = objFromArr.getString("strDrink");
                    pic = objFromArr.getString("strDrinkThumb");
                    instruction = objFromArr.getString("strInstructions");
                    ingredient1 = objFromArr.getString("strIngredient1");
                    ingredient2 = objFromArr.getString("strIngredient2");
                    ingredient3 = objFromArr.getString("strIngredient3");
                    Cocktail cocktail = new Cocktail(name,pic,instruction,ingredient1,ingredient2,ingredient3);
                    cocktail.setPic(bitmap);
                    cocktailList.add(cocktail);
                    publishProgress(80);
                }
                publishProgress(100);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return cocktailList;
        }

        /**
         * this method will update the progress bar
         * @param args parameter list of the method
         */
        public void onProgressUpdate(Integer ... args){
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(args[0]);
        }

        /**
         * this method will show the list view, hide the progress bar
         * @param result parameter list of the method
         */
        public void onPostExecute(ArrayList<Cocktail> result) {
            pb.setVisibility(View.INVISIBLE);
            myList.setAdapter(adapter);
        }

    }
    //**************************************************************************************************

    /**
     * this class helps user deal with the list view
     */
    public class MyListAdapter_Cocktail extends ArrayAdapter<Cocktail> {
        private ArrayList<Cocktail> list = new ArrayList<>();
        private Context context;
        private TextView iv;

        public MyListAdapter_Cocktail(Context context, ArrayList<Cocktail> cocktail) {
            super(context, 0, cocktail);
            this.context = context;
        }

        /**
         * get method to return number of items in the list
         * @return size of the list
         */
        public int getCount() { return cocktailList.size();}

        /**
         * get method to return the cocktail in a given position
         * @param position position number of the item
         * @return the cocktail name in the position
         */
        public Cocktail getItem(int position) { return cocktailList.get(position);}

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
            /*
            Cocktail cocktail = getItem(position);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cocktailrow_layout, parent, false);
            TextView tv = convertView.findViewById(R.id.rowCocktail);
            ImageView iv = convertView.findViewById((R.id.icon));
            tv.setText(cocktail.name);
            iv.setImageBitmap(cocktail.pic);
            return convertView;
             */
            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.cocktailrow_layout, parent, false);
            row = rowView.findViewById(R.id.rowCocktail);
            row.setText(cocktailList.get(position).getName());
            return rowView;
        }
    }
    //**************************************************************************************************

    /**
     * alert dialog for the instruction
     * @return dialogbuiler
     */
    public Dialog instruction() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Help API")
                .setMessage("Type in the cocktail name and click search to search cocktails;" + "\n" +
                        "Check cocktail details by click on the item;" + "\n" +
                        "Save details by click the save button on details page;" + "\n" +
                        "Check saved cocktails by click on Check Saved button;" + "\n" +
                        "Remove item from the list by long click;" + "\n" +
                        "Click icons for additional actions.");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }});
        return alertDialogBuilder.create();
    }
    //**************************************************************************************************

    /**
     * to set menu inflater
     * @param menu menu list
     * @return menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cocktail_menu, menu);
        return true;
    }

    /**
     * set items in menu, use switch and cases to show different toast
     * messages when click on different items under menu
     * @param item in menu
     * @return menu items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.home:
                message = "Go back to homepage";
                Intent goToHome = new Intent(MainActivity_Cocktail.this, MainActivity.class);
                startActivity(goToHome);
                break;
            case R.id.search:
                message = "Search for cocktail";
                break;
            case R.id.help:
                instruction().show();
                break;
            case R.id.email:
                message = "Contack me by email: shan0268@algonquinlive.com";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * Needed for the OnNavigationItemSelected interface:
     * @param item under menu
     * @return items under menu
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            case R.id.home:
                message = "Go back to homepage";
                Intent goToHome = new Intent(MainActivity_Cocktail.this, MainActivity.class);
                startActivity(goToHome);
                break;
            case R.id.search:
                message = "You clicked on the search";
                break;
            case R.id.help:
                instruction().show();
                message = "Yu Shan";
                break;
            case R.id.email:
                message = "Contact me by email: shan0268@algonquinlive.com";
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
    //******************************************************************************************

    /**
     * use sharedPreferences to save the input
     */
    @Override
    public void onPause(){
        super.onPause();
        EditText searchName = findViewById(R.id.searchName);
        SharedPreferences sp = getSharedPreferences("userInput", MODE_PRIVATE);
        SharedPreferences.Editor spE = sp.edit();
        spE.putString("input", searchName.getText().toString());
        spE.commit();
    }

}