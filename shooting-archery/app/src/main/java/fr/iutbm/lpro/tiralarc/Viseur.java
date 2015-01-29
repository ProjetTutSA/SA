package fr.iutbm.lpro.tiralarc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import fr.iutbm.lpro.tiralarc.dbman.DBHelper;

public class Viseur extends Activity {
    private DBHelper db;
    int idArc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        idArc = myIntent.getIntExtra("idarc",0);

        setContentView(R.layout.activity_viseur);
        db = new DBHelper(this);




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
                showAddGraduation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddGraduation() {


    }
}
