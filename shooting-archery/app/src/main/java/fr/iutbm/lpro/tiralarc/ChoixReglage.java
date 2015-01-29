package fr.iutbm.lpro.tiralarc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import fr.iutbm.lpro.tiralarc.dbman.Arc;
import fr.iutbm.lpro.tiralarc.dbman.DBHelper;


public class ChoixReglage extends Activity {
    private DBHelper db;
    Button btnViseur;
    Button btnTiller;
    Button btnBerger;
    int idArc;
    Arc arcSelect = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        idArc = myIntent.getIntExtra("idarc",0);

        setContentView(R.layout.activity_choix_reglage);
        db = new DBHelper(this);

        btnViseur = (Button)  findViewById(R.id.viseur);
        btnBerger = (Button) findViewById(R.id.berger_button);
        btnTiller = (Button) findViewById(R.id.tiller);
        btnViseur.setOnClickListener(handleClick);
        btnBerger.setOnClickListener(handleClick);
        btnTiller.setOnClickListener(handleClick);

        //setupUser();
    }
    protected View.OnClickListener handleClick = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.viseur:
                    Intent intent = new Intent(view.getContext(), Viseur.class);
                    intent.putExtra("idarc", idArc);
                    startActivity(intent);
                    break;
                /*case R.id.berger_button:
                    Intent intent2 = new Intent(view.getContext(), SortActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.tiller:
                    Intent intent3 = new Intent(view.getContext(), ArcActivity.class);
                    startActivity(intent3);
                    break;*/


            }
        }
    };


}
