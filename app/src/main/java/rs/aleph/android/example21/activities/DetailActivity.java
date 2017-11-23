package rs.aleph.android.example21.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.aleph.android.example21.R;
import rs.aleph.android.example21.db.DatabaseHelper;
import rs.aleph.android.example21.db.model.Film;
import rs.aleph.android.example21.db.model.Glumac;

/**
 * Created by KaraklicDM on 21.11.2017.
 */

public class DetailActivity extends AppCompatActivity {

  DatabaseHelper databaseHelper;




    private int identifikator;
    private  Glumac glumac;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);


        Intent intent = getIntent();
        identifikator = intent.getExtras().getInt("identifikator");

        try {
            glumac= getDatabaseHelper().getGlumacDao().queryForId((int)identifikator);
            String name = glumac.getmName();
            String surname = glumac.getmSurname();
            float rating = glumac.getmRating();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy.");
            String birthday = sdf.format(glumac.getmBirthday());
            String biography = glumac.getmBiography();

            TextView tv_name = (TextView)findViewById(R.id.name);
            tv_name.setText(name);
            TextView tv_surname = (TextView)findViewById(R.id.surname);
            tv_surname.setText(surname);
            RatingBar tv_rating = (RatingBar) findViewById(R.id.rating);
            tv_rating.setRating(rating);
            TextView tv_birthday = (TextView)findViewById(R.id.birthday);
            tv_birthday.setText(birthday);
            TextView tv_biography = (TextView)findViewById(R.id.biography);
            tv_biography.setText(biography);

            ForeignCollection<Film> filmovi =  glumac.getmFilms();
          /*  List<Film> films = getDatabaseHelper().getFilmDao().queryForAll();
            List<Film> actorFilm = new ArrayList<>();
            for (Film f:films) {
                if (f.getGlumac().getmId() == glumac.getmId()){
                    actorFilm.add(f);
                }
            }*/
            List<String> filmoviString = new ArrayList<String>();
           /* for (Film f:actorFilm) {
                filmoviString.add(f.getmName());
            }*/


            CloseableIterator<Film> iterator = filmovi.closeableIterator();

            try {

                while (iterator.hasNext()) {
                    Film g=iterator.next();
                    if(g.getGlumac().getmId() == glumac.getmId()) {
                        filmoviString.add(g.getmName());
                    }

                }
            } catch(Exception e)
            {
                Toast.makeText(DetailActivity.this, "Greska prilikom iteracije.",Toast.LENGTH_SHORT).show();

            }
            finally {
                iterator.close();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailActivity.this,R.layout.list_item,filmoviString);
            ListView listView = (ListView)findViewById(R.id.lv_films);
            listView.setAdapter(adapter);






         /*   FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.buy);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Creates notification with the notification builder
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                    Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_stat_buy);
                    builder.setSmallIcon(R.drawable.ic_stat_buy);
                    builder.setContentTitle(getActivity().getString(R.string.notification_title));
                    builder.setContentText(getActivity().getString(R.string.notification_text));
                    builder.setLargeIcon(bitmap);

                    // Shows notification with the notification manager (notification ID is used to update the notification later on)
                    NotificationManager manager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(NOTIFICATION_ID, builder.build());
                }
            });*/


        } catch (SQLException e) {
            e.printStackTrace();
        }








        // Enable ActionBar app icon to behave as action to toggle nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }



        if (savedInstanceState == null) {

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
        getMenuInflater().inflate(R.menu.activity_item_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * Metoda koja je izmenjena da reflektuje rad sa Asinhronim zadacima
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //Toast.makeText(DetailActivity.this, "",Toast.LENGTH_SHORT).show();
                final Dialog dialog1 = new Dialog(DetailActivity.this);
                dialog1.setContentView(R.layout.dialog_layout);
                dialog1.setTitle("Update an actor");
                final EditText name1 = (EditText)dialog1.findViewById(R.id.actor_name);
                final EditText surname = (EditText)dialog1.findViewById(R.id.actor_surname);
                final EditText rating = (EditText)dialog1.findViewById(R.id.actor_rating);
                final EditText biography = (EditText)dialog1.findViewById(R.id.actor_biography);
                final EditText birthday = (EditText)dialog1.findViewById(R.id.actor_birthday);
                Button ok1 = (Button) dialog1.findViewById(R.id.ok);
                ok1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {



                        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy.");
                        String nameA = name1.getText().toString();
                       /* if (nameA == ""){
                            Toast.makeText(DetailActivity.this, "Ime ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;

                        }*/


                        String surnameA = surname.getText().toString();
                        /*if (surnameA == ""){
                            Toast.makeText(DetailActivity.this, "Prezime ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;

                        }*/

                        float ratingA = 0;
                        try {
                            ratingA = Float.parseFloat(rating.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(DetailActivity.this, "Treba da unesete ocenu u obliku decimalnog broja.",Toast.LENGTH_SHORT).show();

                            //e.printStackTrace();
                        }
                        String biographyA = biography.getText().toString();
                        /*if (biographyA == ""){
                            Toast.makeText(DetailActivity.this, "Biografija ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;
                        }*/

                        Date birthdayA = null;
                        try {
                            birthdayA = sdf.parse(birthday.getText().toString());
                        } catch (ParseException e) {
                            Toast.makeText(DetailActivity.this, "Treba da unesete datum u obliku dd.mm.yyyy.",Toast.LENGTH_SHORT).show();

                            //e.printStackTrace();
                        }

                        Glumac glumac = new Glumac();
                        if (nameA != null) {
                            glumac.setmName(nameA);
                        }
                        if (surnameA != null) {
                            glumac.setmSurname(surnameA);
                        }
                        glumac.setmRating(ratingA);
                        if (biographyA != null) {
                            glumac.setmBiography(biographyA);
                        }
                        if (birthdayA != null) {
                            glumac.setmBirthday(birthdayA);
                        }
                        try {
                            getDatabaseHelper().getGlumacDao().update(glumac);
                            //refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        dialog1.dismiss();

                    }


                });

                Button cancel1 = (Button) dialog1.findViewById(R.id.cancel);
                cancel1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                dialog1.show();




                break;
            case R.id.action_add:
                final Dialog dialog = new Dialog(DetailActivity.this);
                dialog.setContentView(R.layout.dialog_detail);
                dialog.setTitle("Input a film");



                final EditText name = (EditText)dialog.findViewById(R.id.film_name);
                final EditText genre = (EditText)dialog.findViewById(R.id.film_genre);
                final EditText year = (EditText)dialog.findViewById(R.id.film_year);

                Button ok = (Button) dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {




                        String nameA = name.getText().toString();
                        if (nameA == ""){
                            Toast.makeText(DetailActivity.this, "Ime ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;

                        }


                        String genreA = genre.getText().toString();
                        if (genreA == ""){
                            Toast.makeText(DetailActivity.this, "Zanr ne moze biti prazan string",Toast.LENGTH_SHORT).show();
                            return;

                        }
                        int yearA = 0;
                        try {
                            yearA = Integer.parseInt(year.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(DetailActivity.this, "Unesite broj koji je ceo.",Toast.LENGTH_SHORT).show();

                        }

                        Film film = new Film();
                        film.setmName(nameA);
                        film.setmZanr(genreA);
                        film.setmGodinaIzlaska(yearA);
                        try {
                            getDatabaseHelper().getFilmDao().create(film);

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



                    //Toast.makeText(DetailActivity.this, "",Toast.LENGTH_SHORT).show();

                break;
            case R.id.action_delete:

                try {

                    if (glumac != null)
                        getDatabaseHelper().getGlumacDao().delete(glumac);
                } catch (SQLException e) {
                    e.printStackTrace();
                }



                //Toast.makeText(DetailActivity.this, "",Toast.LENGTH_SHORT).show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.lv_films);
        if (listview != null){
            ArrayAdapter<Film> adapter = (ArrayAdapter<Film>) listview.getAdapter();
            if(adapter!= null){
                adapter.clear();
              /*  ForeignCollection<Film> filmovi =  glumac.getmFilms();
                CloseableIterator<Film> iterator = filmovi.closeableIterator();*/
                List<Film> list = new ArrayList<Film>();
                try {
                    List<Film> films = getDatabaseHelper().getFilmDao().queryForAll();
                    for (Film f:films) {
                        if (f.getGlumac().equals(glumac)){
                            list.add(f);
                        }
                    }
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                /*try {

                    while (iterator.hasNext()) {
                        Film g=iterator.next();


                       //list.add(g);
                        //getDatabaseHelper().getFilmDao().queryForId(g.getmId())

                    }
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch(Exception e)
                {
                    Toast.makeText(DetailActivity.this, "Greska prilikom iteracije.",Toast.LENGTH_SHORT).show();

                }
                finally {
                    try {
                        iterator.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/




            }
        }
    }

   /* @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
