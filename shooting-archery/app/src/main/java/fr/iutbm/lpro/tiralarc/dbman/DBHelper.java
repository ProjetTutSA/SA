package fr.iutbm.lpro.tiralarc.dbman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static int DB_VERSION = 1;
	private static String DB_NAME = "tiralarc";
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		//Script SQL pour creer la base
		arg0.execSQL("CREATE TABLE Utilisateur (" +
				"idUtilisateur INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idGrade INTEGER NOT NULL  REFERENCES Grade (idGrade)," +
				"NomUtilisateur TEXT(16) NOT NULL ," +
				"PrenomUtilisateur TEXT(16) NOT NULL " +
				");");
		
		arg0.execSQL("CREATE TABLE Grade (" +
				"idGrade INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"LibGrade TEXT(16) NOT NULL" +
				");");
		
		arg0.execSQL("CREATE TABLE Cible ("+
				"idCible INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"LibCible TEXT(16) NOT NULL " +
				");");
		
		arg0.execSQL("CREATE TABLE Partie (" +
				"idPartie INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idUtilisateur INTEGER NOT NULL  REFERENCES Utilisateur (idUtilisateur)," +
				"partieFini TEXT(1) NOT NULL  DEFAULT 'F'," +
				"idCible INTEGER NOT NULL  REFERENCES Cible (idCible)," +
				"distanceCible INTEGER NOT NULL ," +
				"datePartie TEXT NOT NULL ," +
				"nbreManches INTEGER NOT NULL  DEFAULT 1," +
				"nbreVolees INTEGER NOT NULL  DEFAULT 3," +
				"nbreFleches INTEGER NOT NULL ," +
				"comp_entrain TEXT(1) NOT NULL  DEFAULT 'E'," +
				"ext_int TEXT(1) NOT NULL  DEFAULT 'E'," +
				"idDiametre INTEGER NOT NULL  REFERENCES Diametre (idDiametre)" +
				");");
		
		arg0.execSQL("CREATE TABLE Tirer (" +
				"idTirer INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idPartie INTEGER NOT NULL  REFERENCES Partie (idPartie)," +
				"noManche INTEGER NOT NULL  DEFAULT 1," +
				"noVolee INTEGER NOT NULL  DEFAULT 1," +
				"ordreLancer INTEGER NOT NULL  DEFAULT 1," +
				"score INTEGER NOT NULL  DEFAULT 0" +
				");");
		
		arg0.execSQL("CREATE TABLE Diametre (" +
				"idDiametre INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idCible INTEGER NOT NULL  REFERENCES Cible (idCible)," +
				"diametreCible NUMERIC NOT NULL" +
				");");

		
		//Insertion de donnees statiques
		// -- Grade
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Poussin');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Benjamin');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Minime');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Cadet');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Junior');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Senior');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Veteran');");
        arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Super-Veteran');");

		// -- Cible
		arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'Blason');");
		arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'Trispot');");
		
		// -- Diametre
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,40);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,60);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,80);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,122);");
		
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,2,40);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,2,60);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO : Script d'upgrade
	}

	//---------------------------------------------------------------
	//Utilisateur
	public void addUtilisateur(Utilisateur user) throws UserAlreadyRegisteredException {
		if (getUtilisateurFromName(user.getNom(), user.getPrenom()) != null) {
			throw new UserAlreadyRegisteredException();
		}
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		//Remplir la requete
		ContentValues values = new ContentValues();
		values.put("nomUtilisateur", user.getNom());
		values.put("prenomUtilisateur", user.getPrenom());
		values.put("idGrade", user.getIdGrade());
		//Insert dans BDD
		db.insert("Utilisateur", null, values);
		db.close();
	}
	
	public Utilisateur getUtilisateur(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Utilisateur", new String[] { "idUtilisateur", "idGrade",
	            "nomUtilisateur", "prenomUtilisateur" }, "idUtilisateur" + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
				cursor.moveToFirst();
		
		Utilisateur user = new Utilisateur(
				cursor.getInt(0),
				cursor.getInt(1),
				cursor.getString(2), 
				cursor.getString(3));
		
		cursor.close();
		db.close();
		return user;
	}
	
	public Utilisateur getUtilisateurFromName(String nom,String prenom)
	{
		String selectQuery = "SELECT * FROM Utilisateur WHERE NomUtilisateur='"+nom+"' and PrenomUtilisateur='"+prenom+ "';";
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  
		  Utilisateur user = null;
		  
		  if(cursor.moveToFirst())
		  {
			  user = new Utilisateur(Integer.parseInt(cursor.getString(0)),
	            		Integer.parseInt(cursor.getString(1)),
	            		getGrade(Integer.parseInt(cursor.getString(1))).getLibelle(),
	            		cursor.getString(2),
	            		cursor.getString(3));
		  }
		  cursor.close();
		  db.close();
		  return user;
	}
	
	public List<Utilisateur> getUtilisateurs() {
		List<Utilisateur> usersList = new ArrayList<Utilisateur>();
		
		String selectQuery = "SELECT * FROM Utilisateur;";
		 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Utilisateur user = new Utilisateur(Integer.parseInt(cursor.getString(0)),
	            		Integer.parseInt(cursor.getString(1)),
	            		cursor.getString(2),
	            		cursor.getString(3));
	            // Adding contact to list
	            usersList.add(user);
	        } while (cursor.moveToNext());
	    }
		
	    cursor.close();
	    db.close();
	    
		return usersList;
	}
	
	public List<String> getUtilisateursForSpinner() {
		List<String> listUsers = new ArrayList<String>();
		
		for (Utilisateur user : getUtilisateurs()) {
			listUsers.add(user.getNom() + " " + user.getPrenom());
		}
		
		return listUsers;
	}
	
	public int getUtilisateurCounts() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Utilisateur;", null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}
	
	public void updateUtilisateur(Utilisateur user) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("NomUtilisateur", user.getNom());
		values.put("PrenomUtilisateur", user.getPrenom());
		values.put("idGrade", user.getIdGrade());
		
		db.update("Utilisateur", values, "idUtilisateur="+user.getId(), null);
		db.close();
	}
	
	public void deleteUtilisateur(Utilisateur user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.rawQuery("DELETE FROM Tirer WHERE idPartie in (SELECT idPartie FROM Partie WHERE idUtilisateur="+user.getId()+");", null);
		db.delete("Partie", "idUtilisateur="+user.getId(), null);
		db.delete("Utilisateur", "idUtilisateur="+user.getId(), null);
		db.close();
	}
	
	//---------------------------------------------------------------
	//Partie
	
	public int addPartie(Partie partie) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("idUtilisateur", partie.getIdUtilisateur());
		values.put("partieFini", partie.isPartieFini());
		values.put("idCible", partie.getIdCible());
		values.put("distanceCible", partie.getDistanceCible());
		values.put("datePartie", partie.getDatePartie().getTime());  
		values.put("nbreManches", partie.getNbManches());
		values.put("nbreVolees", partie.getNbVolees());
		values.put("nbreFleches", partie.getNbFleches());
		values.put("comp_entrain", ((partie.isCompetition())?"C":"E"));
		values.put("ext_int", ((partie.isExterieur())?"E":"I"));
		values.put("idDiametre",partie.getIdDiametre());
		
		int idPartie = Integer.valueOf(String.valueOf(db.insert("Partie", null, values)));
		db.close();
		return idPartie;
	}
	
	public int checkOngoingGame() {
		String selectQuery = "SELECT count(partieFini),idPartie FROM Partie WHERE partieFini != 'T' ;";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		int idPartie = 0;

		if(cursor.moveToFirst()) {
			if (cursor.getInt(0) != 0)
				idPartie=cursor.getInt(1);
		}
		
		cursor.close();
		db.close();
		return idPartie;
	}
	
	public void incVoleesPartie(int idPartie) {
		Partie partie = getPartie(idPartie);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("nbreVolees", partie.getNbVolees() + 1);
		db.update("Partie",values, "idPartie =" + idPartie, null);
		db.close();
	}
	
	public void terminerPartie(int idPartie)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values=new ContentValues();
		values.put("partieFini", "T");
		db.update("Partie",values, "idPartie =" + idPartie, null);
		db.close();
	}
	
	public void supprimerPartie(int idPartie)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Partie","idPartie ="+idPartie, null);
		db.delete("Tirer","idPartie ="+idPartie, null);
		db.close();
	}
	
	public Partie getPartie(int idPartie) {
		String selectQuery= "SELECT * FROM PARTIE WHERE idPartie =" + idPartie + ";";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Partie partie = null;
		
		if(cursor.moveToFirst()) {
			partie = new Partie(
					cursor.getInt(0), 
					cursor.getInt(1), 
					(cursor.getString(2).equals("T")?true:false),//boolean partieFini,
					cursor.getInt(3), 
					cursor.getInt(4), 
					new Date(cursor.getLong(5)), 
					cursor.getInt(6),
					cursor.getInt(7),
					cursor.getInt(8),
					(cursor.getString(9).equals("C")?true:false),
					(cursor.getString(10).equals("E")?true:false),
					cursor.getInt(11));
		}
		
		cursor.close();
		db.close();
		return partie;
	}
	
	public ArrayList<Integer> getLastParties() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		String selectQuery= "SELECT idPartie FROM PARTIE ORDER BY datePartie DESC LIMIT 5;";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
	        do {
	            result.add(cursor.getInt(0));
	        } while (cursor.moveToNext());
	    }
		cursor.close();
		db.close();
		return result;
	}

	public void normalizeVolees(int idPartie) {
		Partie partie = getPartie(idPartie);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("nbreVolees", partie.getNbVolees() - 1);
		db.update("Partie",values, "idPartie =" + idPartie, null);
		db.close();
	}
	
	public ArrayList<Partie> sortPartie(Integer idUtilisateur, Boolean typeCible, Boolean entr_comp, Boolean environnement) {
		ArrayList<Partie> parties = new ArrayList<Partie>();
		
		SQLiteDatabase db = getReadableDatabase();
		String SQLQuery = "SELECT idPartie FROM Partie WHERE idUtilisateur=" + idUtilisateur;
		
		if (typeCible != null)
			SQLQuery += " AND idCible=" + String.valueOf(typeCible==true?1:2);
		
		if (entr_comp != null)
			SQLQuery += " AND comp_entrain='" + (entr_comp==true?"C":"E") + "'";
		
		if (environnement != null)
			SQLQuery += " AND ext_int='" + (environnement==true?"E":"I") + "'";
		
		SQLQuery += " ORDER BY datePartie DESC;";
		Cursor cursor = db.rawQuery(SQLQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				parties.add(getPartie(cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		
		db.close();
		return parties;
	}
	
	//---------------------------------------------------------------
	// Grade
	
	public Grade getIdGradeFromName(String libelleGrade){
		String selectQuery = "SELECT * FROM Grade WHERE LibGrade='"+libelleGrade+"';";
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  
		  Grade grade = null;
		  
		  if(cursor.moveToFirst())
		  {
			  grade = new Grade(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
		  }
		  cursor.close();
		  db.close();
		  return grade;
	}
	
	public int findGradeByUser(int idUser){
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT idGrade FROM Utilisateur WHERE idUtilisateur="+idUser+";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		int idGrade = cursor.getInt(0);
		
		cursor.close();
		db.close();
		
		return idGrade;
	}
	
	public List<String> getGradeForSpinner() {
		List<String> listGrades = new ArrayList<String>();
		
		for (Grade grade : getGrades()) {
			listGrades.add(grade.getLibelle());
		}
		
		return listGrades;
	}
	

	public List<Grade> getGrades() 
	{
		List<Grade> gradeList = new ArrayList<Grade>();
		String selectQuery = "SELECT * FROM Grade;";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		 // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Grade grade = new Grade(Integer.parseInt(cursor.getString(0)),
	            		 cursor.getString(1));
	            // Adding contact to list
	            gradeList.add(grade);
	        } while (cursor.moveToNext());
	    }
		cursor.close();
		db.close();
		return gradeList;
	}

	public Grade getGrade(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Grade", new String[] { "idGrade", "LibGrade" }, "idGrade="+id , null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		Grade grade = new Grade(cursor.getInt(0), cursor.getString(1));
		
		cursor.close();
		db.close();
		return grade;
	}
	//---------------------------------------------------------------
	// Tirer
	
	public void addTirer(int idPartie, int noManche, int noVolee, int score, int ordreLancer) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("idPartie", idPartie);
		values.put("noManche", noManche);
		values.put("noVolee", noVolee);
		values.put("ordreLancer", ordreLancer);
		values.put("score", score);
		
		db.insert("Tirer", null, values);		
		db.close();
	}
	
	public ArrayList<Tirer> getTirerOf(int idPartie) {
		ArrayList<Tirer> resultat = new ArrayList<Tirer>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Tirer WHERE idPartie =" + idPartie +
				" ORDER BY noManche,noVolee,ordreLancer;", null);
		if (cursor.moveToFirst()) {
			do {
				resultat.add(new Tirer(cursor.getInt(0),
						cursor.getInt(1),
						cursor.getInt(2),
						cursor.getInt(3),
						cursor.getInt(4),
						cursor.getInt(5)));
			} while (cursor.moveToNext());
		}
		
		db.close();
		return resultat;
	}
	
	public ArrayList<Tirer> getTirerOf(int idPartie, int noManche, int nbVolee) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		ArrayList<Tirer> resultat = new ArrayList<Tirer>();
		String selectQuery = "SELECT * FROM Tirer WHERE idPartie=" + idPartie + " AND noManche=" + noManche + " AND noVolee=" + nbVolee + " ORDER BY ordreLancer;";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				resultat.add(new Tirer(cursor.getInt(0),
						cursor.getInt(1),
						cursor.getInt(2),
						cursor.getInt(3),
						cursor.getInt(4),
						cursor.getInt(5)));
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return resultat;
	}
	//---------------------------------------------------------------
	// Cible
	
	public Cible getCible(int idCible) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Cible", new String[] { "idCible", "LibCible" }, "idCible="+idCible , null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		Cible cible = new Cible(cursor.getInt(0), cursor.getString(1));
		
		cursor.close();
		db.close();
		return cible;
	}

	//---------------------------------------------------------------
	// Diametre
	
	public ArrayList<Diametre> getDiametres(int i) {
		ArrayList<Diametre> result = new ArrayList<Diametre>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Diametre", new String[] { "idDiametre" , "diametreCible" }, "idCible="+i, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				result.add(new Diametre(cursor.getInt(0), i, cursor.getInt(1)));
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return result;
	}
	
	public Diametre getDiametreFromId(int id)
	{
		Diametre result= null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor= db.query("Diametre", new String[] { "idCible" , "diametreCible" }, "idDiametre="+id, null, null, null, null);
		if (cursor.moveToFirst()) 
		{
			result= new Diametre(id,cursor.getInt(0), cursor.getInt(1));
		}
		
		return result;
	}

	
	
	//---------------------------------------------------------------
	
	public static String getDateString(Date date, Context context) {
		long when = date.getTime();
        int flags = 0;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

        return android.text.format.DateUtils.formatDateTime(context,
        	when, flags);
	}

	public void reset() {
		dropTables();
		onCreate(getWritableDatabase());
	}

	private void dropTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("DROP TABLE IF EXISTS Utilisateur;");
		db.execSQL("DROP TABLE IF EXISTS Grade;");
		db.execSQL("DROP TABLE IF EXISTS Cible;");
		db.execSQL("DROP TABLE IF EXISTS Tirer;");
		db.execSQL("DROP TABLE IF EXISTS Partie;");
		db.execSQL("DROP TABLE IF EXISTS Diametre;");
		
		db.close();
	}
}
