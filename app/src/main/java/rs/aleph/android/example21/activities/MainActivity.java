package rs.aleph.android.example21.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.aleph.android.example21.R;
import rs.aleph.android.example21.db.DatabaseHelper;
import rs.aleph.android.example21.db.model.Glumac;
import rs.aleph.android.example21.dialogs.AboutDialog;

public class MainActivity extends AppCompatActivity{



    private AlertDialog dialog;
    //za rad sa bazom
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;




   // private int productId = 0;
    private static int NOTIFICATION_ID = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        //PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean toast = sharedPreferences.getBoolean("pref_checkout_toast",true);
        final boolean notification = sharedPreferences.getBoolean("pref_checkout_notification",false);

       /* //showing notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_stat_buy);
        builder.setSmallIcon(R.drawable.ic_stat_buy);
        builder.setContentTitle("Title");
        builder.setContentText("Content title");
        builder.setLargeIcon(bitmap);

        // Shows notification with the notification manager (notification ID is used to update the notification later on)
        //umesto this aktivnost
        NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());



        //showing AboutDialog
        if (dialog == null){
            dialog = new AboutDialog(MainActivity.this).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog.show();

        //Pristupanje deljenim podesavanjima,primaju samo primitivne tipove
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //default vrednost iz liste "1"
        String s = sharedPreferences.getString("@string/pref_sync","1");
        boolean b = sharedPreferences.getBoolean("@string/pref_sync",false);*/


        //samples of views
      /*  EditText name = (EditText) findViewById(R.id.name);
        name.setText(product.getmName());

        EditText description = (EditText) findViewById(R.id.description);
        description.setText(product.getDescription());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        ratingBar.setRating(product.getRating());

        ImageView imageView = (ImageView) findViewById(R.id.image);
        InputStream is = null;
        try {
            is = getAssets().open(product.getImage());
            Drawable drawable = Drawable.createFromStream(is, null);
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */





        // Enable ActionBar app icon to behave as action to toggle nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }








        List<Glumac> actors = new ArrayList<Glumac>();
        ListAdapter adapter;
        try {
            actors = getDatabaseHelper().getGlumacDao().queryForAll();
            adapter = new ArrayAdapter<Glumac>(MainActivity.this,R.layout.list_item,actors);
            final ListView listView1 = (ListView)findViewById(R.id.actors);
            listView1.setAdapter(adapter);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Glumac glumac = (Glumac)listView1.getItemAtPosition(position);
                    int identifikator = glumac.getmId();
                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                    intent.putExtra("identifikator",(int)identifikator);
                    //intent.putExtra("toast",toast);
                    //intent.putExtra("notification",notification);
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }






     }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.actors);
        if (listview != null){
            ArrayAdapter<Glumac> adapter = (ArrayAdapter<Glumac>) listview.getAdapter();
            if(adapter!= null){
                adapter.clear();
                try {
                    List<Glumac> list = getDatabaseHelper().getGlumacDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        }
    }














    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_item_master, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * Metoda koja je izmenjena da reflektuje rad sa Asinhronim zadacima
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean toast = sharedPreferences.getBoolean("@string/pref_checkout_toast",true);
        final boolean notification = sharedPreferences.getBoolean("@string/pref_checkout_notification",false);

        switch (item.getItemId()) {
            case R.id.action_about:
                if (dialog == null){
                    dialog = new AboutDialog(MainActivity.this).prepareDialog();
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                dialog.show();

                break;
            case R.id.action_add:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("Input an actor");
                final EditText name = (EditText)dialog.findViewById(R.id.actor_name);
                final EditText surname = (EditText)dialog.findViewById(R.id.actor_surname);
                final EditText rating = (EditText)dialog.findViewById(R.id.actor_rating);
                final EditText biography = (EditText)dialog.findViewById(R.id.actor_biography);
                final EditText birthday = (EditText)dialog.findViewById(R.id.actor_birthday);
                Button ok = (Button) dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {



                        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy.");
                        String nameA = name.getText().toString();
                        if (nameA == ""){
                            Toast.makeText(MainActivity.this, "Ime ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;

                        }


                        String surnameA = surname.getText().toString();
                        if (surnameA == ""){
                            Toast.makeText(MainActivity.this, "Prezime ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;

                        }

                        float ratingA = 0;
                        try {
                            ratingA = Float.parseFloat(rating.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(MainActivity.this, "Treba da unesete ocenu u obliku decimalnog broja.",Toast.LENGTH_SHORT).show();

                            //e.printStackTrace();
                        }
                        String biographyA = biography.getText().toString();
                        if (biographyA == ""){
                            Toast.makeText(MainActivity.this, "Biografija ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Date birthdayA = null;
                        try {
                            birthdayA = sdf.parse(birthday.getText().toString());
                        } catch (ParseException e) {
                            Toast.makeText(MainActivity.this, "Treba da unesete datum u obliku dd.mm.yyyy.",Toast.LENGTH_SHORT).show();

                            //e.printStackTrace();
                        }

                        Glumac glumac = new Glumac();
                        glumac.setmName(nameA);
                        glumac.setmSurname(surnameA);
                        glumac.setmRating(ratingA);
                        glumac.setmBiography(biographyA);
                        glumac.setmBirthday(birthdayA);
                        try {
                            getDatabaseHelper().getGlumacDao().create(glumac);
                            if (toast && notification){
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                                builder.setContentTitle("Dodavanje glumca");
                                builder.setContentText("Uspesan unos glumca u bazu.");
                                NotificationManager manager = (NotificationManager)MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(NOTIFICATION_ID, builder.build());

                                Toast.makeText(MainActivity.this, "Dodali ste glumca uspesno u bazu.",Toast.LENGTH_SHORT).show();

                            }else if (!toast && notification){
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                                builder.setContentTitle("Dodavanje glumca");
                                builder.setContentText("Uspesan unos glumca u bazu.");
                                NotificationManager manager = (NotificationManager)MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(NOTIFICATION_ID, builder.build());

                            }else if (toast && !notification){

                                Toast.makeText(MainActivity.this, "Dodali ste glumca uspesno u bazu.",Toast.LENGTH_SHORT).show();

                            }

                            refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    }


                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }*/









   /* @Override
    public void onBackPressed() {

        if (landscapeMode) {
            finish();
        } else if (listShown == true) {
            finish();
        } else if (detailShown == true) {

        }

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}







