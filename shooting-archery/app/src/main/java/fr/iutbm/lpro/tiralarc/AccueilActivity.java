package fr.iutbm.lpro.tiralarc;


import java.util.ArrayList;

import fr.iutbm.lpro.tiralarc.dbman.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccueilActivity extends Activity {

	private DBHelper db;
	private int idPartieResumable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		
		db = new DBHelper(this);
		
		//Créer les fragments une seule fois
//		if (savedInstanceState == null) {
//			makeLastGamesFragments();
//		}
		
		checkOngoingGame();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		FragmentManager fragmentManager = getFragmentManager();
		int i = 0;
		ScoreTableFragment fragment = null;
		do {
			fragment = (ScoreTableFragment) fragmentManager.findFragmentByTag("stf" + i++);
			if (fragment != null) {
				fragment.refresh();
			}
		} while (fragment != null);
	}

//	private void makeLastGamesFragments() {
//		ArrayList<Integer> parties = db.getLastParties();
//		if (parties.size() == 0) {
//			((TextView) findViewById(R.id.accueil_noDataText)).setVisibility(View.VISIBLE);
//			return;
//		}
//
//
//		FragmentManager fragmentManager = getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//		for (int i=0;i<parties.size();i++) {
//			ScoreTableFragment stf = new ScoreTableFragment();
//			Bundle arguments = new Bundle();
//			arguments.putInt("idPartie", parties.get(i));
//			stf.setArguments(arguments);
//			fragmentTransaction.add(R.id.accueil_fragments, stf, "stf" + i);
//
//			if (i != parties.size() - 1) {
//				SeparatorFragment sf = new SeparatorFragment();
//				fragmentTransaction.add(R.id.accueil_fragments, sf);
//			}
//		}
//		fragmentTransaction.commit();
//	}

	private void checkOngoingGame() 
	{
		idPartieResumable = db.checkOngoingGame();
		if(idPartieResumable==0) 
			return;
		
		 new AlertDialog.Builder(this)
	        .setTitle(R.string.attention)
	        .setMessage(R.string.checkOngoingGame)
	        .setIcon(R.drawable.warning_dark)
	        .setCancelable(false)
	        .setNegativeButton(android.R.string.no, new OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					db.supprimerPartie(idPartieResumable);
					idPartieResumable = 0;
					getSharedPreferences("partie", Context.MODE_PRIVATE).edit().clear().commit();
					arg0.dismiss();
					finish();
					Intent thisActivity = getIntent();
					thisActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(thisActivity);
				}

			})
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            	resumeOngoingGame();
	            	arg0.dismiss();
	            }
	        }).create().show();

	}

	protected void trashOngoingGame() {
		//Supprimer partie non termin�e
	}

	protected void resumeOngoingGame() {
		Intent intent = new Intent(this, InGameActivity.class);
    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accueil, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_search:
	    		Intent intent3 = new Intent(this, SortActivity.class);
	    		startActivity(intent3);
	    		return true;
	    
	        case R.id.action_newgame:
	        	Intent intent = new Intent(this, Config1Activity.class);
            	startActivity(intent);
	            return true;
	        case R.id.action_settings:
	        	Intent intent2 = new Intent(this, SettingsActivity.class);
            	startActivity(intent2);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
        public void buttonOnClick(View v){
           Button monarc = (Button) v;
            startActivity(new Intent(this,mesarcs_activity.class));
        }
    public void buttonOnClick1(View v){
        Button monarc = (Button) v;
        startActivity(new Intent(this,nouveau_activity.class));
    }
}
