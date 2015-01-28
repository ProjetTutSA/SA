package fr.iutbm.lpro.tiralarc;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import fr.iutbm.lpro.tiralarc.dbman.Arc;
import fr.iutbm.lpro.tiralarc.dbman.DBHelper;
import fr.iutbm.lpro.tiralarc.dbman.TypeArc;
import fr.iutbm.lpro.tiralarc.dbman.Utilisateur;


public class ArcActivity extends Activity {
    private DBHelper db;
    private Spinner popupTypeSpinner = null;
    EditText EditNomArc = null;
    private List<TypeArc> TypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc);
        db = new DBHelper(this); //connection bdd
    }
    private void ListArcFragments() {
        List<Integer> arc = db.getListnbrArc();
        if (arc.size() == 0) {
            findViewById(R.id.arc_noDataText).setVisibility(View.VISIBLE);
            return;
        }
      /*  FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i=0;i<arc.size();i++) {
            ListeArc list = new ListeArc();
            *//*ScoreTableFragment stf = new ScoreTableFragment();*//*
            Bundle arguments = new Bundle();
            arguments.putInt("idArc", arc.get(i));
            list.setArguments(arguments);
            fragmentTransaction.add(R.id.arc_fragments, list, "stf" + i);
            if (i != arc.size() - 1) {
                SeparatorFragment sf = new SeparatorFragment();
                fragmentTransaction.add(R.id.arc_fragments, sf);
            }
        }
        fragmentTransaction.commit();*/
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
            case R.id.action_add:
                showAddArc();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showAddArc (){
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.add_arc)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_add_arc, null))
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
                popupTypeSpinner = (Spinner) ((Dialog) d).findViewById(R.id.popup_spinner_typearc);
                ListTypeArc();


                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View dialog) {
                        Object selection = popupTypeSpinner.getSelectedItem();
                        EditNomArc = (EditText) ((Dialog)d).findViewById(R.id.popup_nom_arc);
                        String nomArc = EditNomArc.getText().toString();

                        if (nomArc.isEmpty()) {
                            EditNomArc.setError(getString(R.string.nom_arc_not_null));

                        }else{
                            String type = selection.toString();
                            //lancer linsert get l'utilisateur
                            d.dismiss();
                        }
                    }
                });
            }


        });
        d.show();
    }

    private void ListTypeArc(){
        TypeList = null;
        TypeList = db.getTypeArc();

        ArrayList<String> listStringType = new ArrayList<String>();
        for (TypeArc type : TypeList){
            listStringType.add(type.getNomType());

        }

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStringType);
        popupTypeSpinner.setAdapter(adapterType);
    }
}