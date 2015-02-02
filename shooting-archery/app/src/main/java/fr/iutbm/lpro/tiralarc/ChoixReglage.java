package fr.iutbm.lpro.tiralarc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fr.iutbm.lpro.tiralarc.dbman.Arc;
import fr.iutbm.lpro.tiralarc.dbman.DBHelper;
import fr.iutbm.lpro.tiralarc.dbman.Graduation;
import fr.iutbm.lpro.tiralarc.dbman.TypeArc;


public class ChoixReglage extends Activity {
    private DBHelper db;
    EditText Editdistance;
    EditText Edithorizontal;
    EditText Editvertical;
    EditText Editprofondeur;
    EditText Editremarque;

    String nomArc;
    String nomTypeArc;
    Arc arcSelect = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        nomArc = myIntent.getStringExtra("nomArc");

        setContentView(R.layout.activity_choix_reglage);
        db = new DBHelper(this);

        setupActionBar();
        setupPage();
    }

    private void setupPage() {
        arcSelect = db.getArcFromName(nomArc);
        TypeArc typeArcIn = db.getTypeArc(arcSelect.getIdTypeArc());
        nomTypeArc = String.valueOf(typeArcIn.getNomType());
        ListreglageFragments(arcSelect.getIdArc());
        getActionBar().setTitle("Réglages de "+nomArc);
        getActionBar().setSubtitle(nomTypeArc);

    }

    private void ListreglageFragments(int id) {
        List<Graduation> listgrad = db.getListReglageByArc(id);
        if (listgrad.size() == 0) {
            ((TextView) findViewById(R.id.reglage_noDataText)).setVisibility(View.VISIBLE);
            return;
        }

        ((TextView) findViewById(R.id.reglage_noDataText)).setVisibility(View.GONE);
        LinearLayout ll = (LinearLayout) findViewById(R.id.listreglage);
        ll.removeAllViews();

        for (Graduation grad : listgrad) {

            float dp = getResources().getDisplayMetrics().density;
            String remarque = grad.getRemarque();
            int Distance = grad.getDistance();
            String text = "Distance : " + Distance+"m <br/><small>Remarque : "+ remarque+"</small>";
            Button regBut = new Button(this);

            regBut.setText(Html.fromHtml(text));
//          arcBut.setText(nom);
            regBut.setTextColor(getResources().getColor(R.color.color_button_text));
            regBut.setHintTextColor(getResources().getColor(R.color.color_button_text));

            regBut.setBackgroundColor(getResources().getColor(R.color.color_button));
            regBut.setTextSize(25);

            LinearLayout.MarginLayoutParams margin = (LinearLayout.MarginLayoutParams) ll.getLayoutParams();
            margin.setMargins(0,0,0,10);
            margin.height =LinearLayout.LayoutParams.WRAP_CONTENT ;
            regBut.setGravity(Gravity.CENTER_HORIZONTAL);
            regBut.setLayoutParams(margin);

            regBut.requestLayout();
            ll.addView(regBut);
        }
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_add:
                showAddReglage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void showAddReglage (){
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.add_reg)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_add_reglage, null))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Annuler", null)
                .create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Editdistance = (EditText) ((Dialog)d).findViewById(R.id.popup_distance_reg);
                Edithorizontal = (EditText) ((Dialog)d).findViewById(R.id.popup_horizontal_reg);
                Editvertical = (EditText) ((Dialog)d).findViewById(R.id.popup_vertical_reg);
                Editprofondeur = (EditText) ((Dialog)d).findViewById(R.id.popup_profondeur_reg);
                Editremarque = (EditText) ((Dialog)d).findViewById(R.id.popup_remarque_reg);



                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View dialog) {
                        boolean submit = true;

                        String distance = Editdistance.getText().toString();
                        String horizontal = Edithorizontal.getText().toString();
                        String vertical = Editvertical.getText().toString();
                        String profondeur = Editprofondeur.getText().toString();
                        String remarque = Editremarque.getText().toString();

                        if (distance.isEmpty()) {
                            Editdistance.setError(getString(R.string.distance_not_null));
                            submit =false;
                        }
                        if (horizontal.isEmpty()) {
                            Edithorizontal.setError(getString(R.string.horizontal_not_null));
                            submit =false;
                        }
                        if (vertical.isEmpty()) {
                            Editvertical.setError(getString(R.string.vertical_not_null));
                            submit =false;
                        }
                        if (profondeur.isEmpty()) {
                            Editprofondeur.setError(getString(R.string.profondeur_not_null));
                            submit =false;
                        }


                        if (submit){

                            db.addGraduation(new Graduation(arcSelect.getIdArc(), 0,Integer.valueOf(distance),remarque , Float.valueOf(horizontal),Float.valueOf(vertical),Float.valueOf(profondeur)));
                            ListreglageFragments(arcSelect.getIdArc());
                            d.dismiss();
                        }
                    }
                });
            }
        });
        d.show();
    }



}
