package fr.iutbm.lpro.tiralarc;

import java.util.ArrayList;

import fr.iutbm.lpro.tiralarc.dbman.DBHelper;
import fr.iutbm.lpro.tiralarc.dbman.Diametre;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

public class Config2Activity extends Activity 
{
	ImageView classique=null;
	ImageView trispot =null;
	RadioButton classiqueRadio = null;
	RadioButton trispotRadio = null;
	EditText tailleCible=null;
	private RadioButton competition;
	private RadioButton entrainement;
	private DBHelper db;
	
	Spinner DiametreSpinner=null;
	private ArrayList<Diametre> listDiametre;
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config2);

		// Show the Up button in the action bar.
		setupActionBar();
		fillAttributes();
		readPreferences();
	}

	private void readPreferences() {
		SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);

		if(preferences.getBoolean("ImageClassique", false))
			classiqueRadio.setChecked(true);
		if(preferences.getBoolean("ImageTrispot", false))
			trispotRadio.setChecked(true);
		
		//lecture choix condition
		if(preferences.getBoolean("RadioEntrainement", false))
			entrainement.setChecked(true);
		if(preferences.getBoolean("RadioCompetition", false))
			competition.setChecked(true);
		//fin lecture choix condition
	}
	
	private void fillAttributes()
	{
		db = new DBHelper(this);
		classique = (ImageView)findViewById(R.id.config2_cibleImg);
		trispot = (ImageView)findViewById(R.id.config2_cibleTrispotImg);
		
		spinner = (Spinner) findViewById(R.id.config2_spinnertaille);
		
		CompoundButton.OnCheckedChangeListener occl = new CompoundButton.OnCheckedChangeListener() {
			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   if (!isChecked)
					   return;
				   updateDiametres((((View) buttonView).getId() == R.id.config2_radioCibleBlason));
			   }
			};
		
		classiqueRadio = (RadioButton)findViewById(R.id.config2_radioCibleBlason);
		classiqueRadio.setOnCheckedChangeListener(occl);
		trispotRadio = (RadioButton)findViewById(R.id.config2_radioCibleTrispot);
		trispotRadio.setOnCheckedChangeListener(occl);
		
		
		competition = (RadioButton) findViewById(R.id.config2_radioCompetition);
		entrainement = (RadioButton) findViewById(R.id.config2_radioEntrainement);
				
		// On attribue un listener adapt� aux vues qui en ont besoin
		// en cliquant sur l'id voulue -> check le radio correspondant
		classique.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				classiqueRadio.setChecked(true);
			}
		});

		trispot.setOnClickListener( new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				trispotRadio.setChecked(true);
			}
		});
		
	}

	private void setupActionBar() 
	{
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.action_next:
			//Erreur de saisie ?
			if (!checkInputs()) 
				return true;
			
			savePreferences();
			Intent intent = new Intent(this, Config3Activity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void savePreferences() {
		//Taille Cible
		SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putInt("idDiametre", listDiametre.get(spinner.getSelectedItemPosition()).getIdDiametre());
		
		//Choix cible
		classiqueRadio = (RadioButton)findViewById(R.id.config2_radioCibleBlason);
		trispotRadio = (RadioButton)findViewById(R.id.config2_radioCibleTrispot);
		editor.putBoolean("ImageClassique",classiqueRadio.isChecked());
		editor.putBoolean("ImageTrispot", trispotRadio.isChecked());
		//fin choix cible
		
		//choix condition
		editor.putBoolean("RadioEntrainement", entrainement.isChecked());
		editor.putBoolean("RadioCompetition", competition.isChecked());
		//fin choix condition
		
		editor.commit();
	}

	private boolean checkInputs() 
	{
		try
		{
			if(!classiqueRadio.isChecked() && !trispotRadio.isChecked())
			{
				makeAlert(getString(R.string.error),getString(R.string.erreur_radio_non_check));
				return false;
			}
			if (spinner.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
				makeAlert(getString(R.string.error),getString(R.string.have_to_choose_a_size));
				return false;
			}
			if(!competition.isChecked() && !entrainement.isChecked()){
				makeAlert(getString(R.string.erreur),getString(R.string.conditions_error));
				return false;
			}
		}
		catch(Exception e)
		{
			makeAlert(getString(R.string.error),getString(R.string.erreur_valeur_nul));
			return false;
		}
		return true;
	}

	private void makeAlert(String title,String message) 
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.error));
		alertDialog.setMessage(message);
		alertDialog.setIcon(R.drawable.warning_dark);
		alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Nothing here
			}
		});
		alertDialog.show();
	}

	private void updateDiametres(boolean isBlason) {
		listDiametre = null;
		listDiametre = db.getDiametres((isBlason)?1:2);
		ArrayList<String> listStringDiametre = new ArrayList<String>();
		for (Diametre d : listDiametre) {
			listStringDiametre.add(String.valueOf(d.getDiametre()) + " cm");
		}

		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listStringDiametre);
		spinner.setAdapter(adapt);
	}
}
