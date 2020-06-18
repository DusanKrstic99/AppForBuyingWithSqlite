package rs.edu.viser.pmu.aplikacijazakupovinu
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class DataBaseHandler(context: Context?): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "BazaProzivoda"
        private val TABLE_Name= "Korisnik"
        private val REC_ID = "id"
        private val REC_NAME = "ime"
        private val REC_PREZIME = "prezime"
        private val REC_EMAIL = "email"
        private val REC_ULICA_I_BROJ = "adresa"
        private val REC_DRZAVA = "drzava"
        private val TABLE_Name3= "Proizvod"
        private val REC_ID_PROIZVOD = "id_proizvoda"
        private val REC_NAME_PROIZVOD = "naziv_proizvoda"
        private val REC_CENA_PROIZVOD = "cena_proizvoda"
        private val REC_KOLICINA_PROIZVOD = "kolicina_proizvoda"
        private val REC_STANDARDNO_SLANJE = "standardno"
        private val REC_DRZAVA_PROIZVOD = "drzava_proizvoda"
        private val TABLE_Name4= "Korpa"
        private val REC_ID_KORPA = "id_korpa"
        private val REC_ID_KORPA_PR = "id_proizvoda_korpa"
        private val REC_NAME_KORPA_IME_PR = "naziv_Proizvoda"
        private val REC_KOL_KORPA_PR = "kolicina_Proizvoda"
        private val REC_VREME_KORPA = "broj_dana_isporuke"
        private val REC_UKUPNA_CENA = "cena_puta_kolicina"
        private val REC_CENA_PO_KOM = "cena_po_komadu"
        private val TABLE_Name5= "Drzave"
        private val REC_NAME_DRZAVA = "naziv_drzave"
        private val TABLE_Name6= "Porudzbenica"
        private val REC_ID_Porudzba = "id_porudzba"
        private val REC_ADRESA_DOSTAVE = "adresa_za_dostavu"
        private val REC_VREME_DOSTAVE = "vreme_za_dostavu"
        private val REC_PLACANJE_DOSTAVE = "ukupno_za_placanje"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE1 = ("CREATE TABLE " + TABLE_Name + "("
                + REC_ID + " INTEGER PRIMARY KEY," + REC_NAME + " TEXT,"
                + REC_PREZIME + " TEXT," + REC_EMAIL + " TEXT,"+ REC_ULICA_I_BROJ + " TEXT,"
                + REC_DRZAVA + " TEXT"+")")
        val CREATE_TABLE3 = ("CREATE TABLE " + TABLE_Name3 + "("
                + REC_ID_PROIZVOD + " INTEGER PRIMARY KEY," + REC_NAME_PROIZVOD + " TEXT,"
                + REC_CENA_PROIZVOD + " DOUBLE," + REC_KOLICINA_PROIZVOD + " INTEGER,"+ REC_STANDARDNO_SLANJE + " INTEGER,"
                + REC_DRZAVA_PROIZVOD + " TEXT"+")")
        val CREATE_TABLE4 =("CREATE TABLE " + TABLE_Name4 + "("
                + REC_ID_KORPA + " INTEGER PRIMARY KEY," + REC_ID_KORPA_PR + " INTEGER," + REC_NAME_KORPA_IME_PR + " TEXT,"
                + REC_CENA_PO_KOM + " DOUBLE,"+ REC_KOL_KORPA_PR + " INTEGER,"
                + REC_VREME_KORPA + " INTEGER," + REC_UKUPNA_CENA + " DOUBLE"+")")
        val CREATE_TABLE5 =("CREATE TABLE " + TABLE_Name5 + "("
                + REC_NAME_DRZAVA + " TEXT PRIMARY KEY" +")")
        val CREATE_TABLE6 = ("CREATE TABLE " + TABLE_Name6 + "("
                + REC_ID_Porudzba + " INTEGER PRIMARY KEY," + REC_ADRESA_DOSTAVE + " TEXT,"
                + REC_VREME_DOSTAVE + " INTEGER," + REC_PLACANJE_DOSTAVE + " DOUBLE"+")")
        db?.execSQL(CREATE_TABLE1)
        db?.execSQL(CREATE_TABLE3)
        db?.execSQL(CREATE_TABLE4)
        db?.execSQL(CREATE_TABLE5)
        db?.execSQL(CREATE_TABLE6)
        val empList:ArrayList<String> = ArrayList<String>()
        empList.addAll(listOf("Austrija","Belgija",
            "Bugarska","Hrvatska","Kipar","Danska","Estonija","Finska","Francuska",
            "Nemacka","Grcka","Italija","Holandija","Rumunija","Engleska","Spanija","Srbija",
            "Slovenija","Rusija","Poljska"))
        empList.sort()
        for (index2 in 0 until empList.size) {
            val contentValues = ContentValues()
            contentValues.put(REC_NAME_DRZAVA, empList[index2])
           db?.insert(TABLE_Name5,null,contentValues)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_Name)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_Name3)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_Name4)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_Name5)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_Name6)

        onCreate(db)
    }
    fun addKorisnika(emp: ModelClassKorisnik):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(REC_ID, emp.id)
        contentValues.put(REC_NAME, emp.ime)
        contentValues.put(REC_PREZIME,emp.prezime )
        contentValues.put(REC_EMAIL, emp.email)
        contentValues.put(REC_ULICA_I_BROJ, emp.UlicaIBroj)
        contentValues.put(REC_DRZAVA, emp.drzava)
        val success = db.insert(TABLE_Name, null, contentValues)
        db.close() // Closing database connection
        return success
    }
    fun getDrzave(): List<String> {
        val labels: MutableList<String> = ArrayList()
        val selectQuery = "SELECT $REC_NAME_DRZAVA FROM $TABLE_Name5"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return labels
    }
    fun getIDKorisnika(): Int? {
        val labels: MutableList<Int> = ArrayList()
        val selectQuery = "SELECT $REC_ID FROM $TABLE_Name"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        var NajveciID= labels.max()
        return NajveciID
    }
    fun getDrzavuKorisnika(id: Int): String? {
        val labels: MutableList<String> = ArrayList()
        val selectQuery = "SELECT $REC_DRZAVA FROM $TABLE_Name WHERE $REC_ID "+ "= $id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        var Drzava= labels[labels.size-1]
        return Drzava
    }
    fun getAdresuKorisnika(id: Int): String? {
        val labels: MutableList<String> = ArrayList()
        val selectQuery = "SELECT $REC_ULICA_I_BROJ FROM $TABLE_Name WHERE $REC_ID "+ "= $id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        var Adresa= labels[labels.size-1]
        return Adresa
    }
    fun getIDProizvoda(): Int? {
        val labels: MutableList<Int> = ArrayList()
        val selectQuery = "SELECT $REC_ID_PROIZVOD FROM $TABLE_Name3"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        var NajveciID= labels.max()
        return NajveciID
    }
    fun getIDKorpe(): Int? {
        val labels: MutableList<Int> = ArrayList()
        val selectQuery = "SELECT $REC_ID_KORPA FROM $TABLE_Name4"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        var NajveciID= labels.max()
        return NajveciID
    }
    fun getIDPorudzbina(): Int? {
        val labels: MutableList<Int> = ArrayList()
        val selectQuery = "SELECT $REC_ID_Porudzba FROM $TABLE_Name6"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        var NajveciID= labels.max()
        return NajveciID
    }
    fun viewProizvode():List<ModelClassProizvod>{
        val empList:ArrayList<ModelClassProizvod> = ArrayList<ModelClassProizvod>()
        val selectQuery = "SELECT  * FROM $TABLE_Name3"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var naziv: String
        var cena: Double
        var kolicina: Int
        var slanje: Int
        var Drzava: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id_proizvoda"))
                naziv = cursor.getString(cursor.getColumnIndex("naziv_proizvoda"))
                cena = cursor.getDouble(cursor.getColumnIndex("cena_proizvoda"))
                kolicina = cursor.getInt(cursor.getColumnIndex("kolicina_proizvoda"))
                slanje = cursor.getInt(cursor.getColumnIndex("standardno"))
                Drzava = cursor.getString(cursor.getColumnIndex("drzava_proizvoda"))
                val emp= ModelClassProizvod(id = id,naziv = naziv,cena = cena,kolicina = kolicina,slanje = slanje,drzava = Drzava)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return empList
    }
    fun viewProizvod(id:Int):List<ModelClassProizvod>{
        val empList:ArrayList<ModelClassProizvod> = ArrayList<ModelClassProizvod>()
        val selectQuery = "SELECT * FROM $TABLE_Name3 WHERE $REC_ID_PROIZVOD "+ "= $id"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var naziv: String
        var cena: Double
        var kolicina: Int
        var slanje: Int
        var Drzava: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id_proizvoda"))
                naziv = cursor.getString(cursor.getColumnIndex("naziv_proizvoda"))
                cena = cursor.getDouble(cursor.getColumnIndex("cena_proizvoda"))
                kolicina = cursor.getInt(cursor.getColumnIndex("kolicina_proizvoda"))
                slanje = cursor.getInt(cursor.getColumnIndex("standardno"))
                Drzava = cursor.getString(cursor.getColumnIndex("drzava_proizvoda"))
                val emp= ModelClassProizvod(id = id,naziv = naziv,cena = cena,kolicina = kolicina,slanje = slanje,drzava = Drzava)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return empList
    }
    fun addProizvod(emp: ModelClassProizvod):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(REC_ID_PROIZVOD,emp.id)
        contentValues.put(REC_NAME_PROIZVOD,emp.naziv)
        contentValues.put(REC_CENA_PROIZVOD,emp.cena)
        contentValues.put(REC_KOLICINA_PROIZVOD,emp.kolicina)
        contentValues.put(REC_STANDARDNO_SLANJE,emp.slanje)
        contentValues.put(REC_DRZAVA_PROIZVOD,emp.drzava)
        val success = db.insert(TABLE_Name3, null, contentValues)
        db.close()
        return success
    }
    fun addKorpa(emp: ModelClassKorpa):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(REC_ID_KORPA, emp.id)
        contentValues.put(REC_ID_KORPA_PR, emp.idProizvoda)
        contentValues.put(REC_NAME_KORPA_IME_PR, emp.naziv)
        contentValues.put(REC_KOL_KORPA_PR, emp.kolicina)
        contentValues.put(REC_VREME_KORPA, emp.broj_dana)
        contentValues.put(REC_UKUPNA_CENA, emp.UkupnaCena)
        contentValues.put(REC_CENA_PO_KOM, emp.CenaPoKomadu)
        val success = db.insert(TABLE_Name4, null, contentValues)
        db.close()
        return success
    }
    fun viewKorpa():List<ModelClassKorpa>{
        val empList:ArrayList<ModelClassKorpa> = ArrayList<ModelClassKorpa>()
        val selectQuery = "SELECT  * FROM $TABLE_Name4"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var IdKorpe: Int
        var idProizvoda: Int
        var NazivProizvoda: String
        var KolicinaProizvoda: Int
        var DostavaDani: Int
        var UkupnaCena:Double
        var PoKomadu:Double


        if (cursor.moveToFirst()) {
            do {
                IdKorpe= cursor.getInt(cursor.getColumnIndex("id_korpa"))
                idProizvoda= cursor.getInt(cursor.getColumnIndex("id_proizvoda_korpa"))
                NazivProizvoda= cursor.getString(cursor.getColumnIndex("naziv_Proizvoda"))
                KolicinaProizvoda = cursor.getInt(cursor.getColumnIndex("kolicina_Proizvoda"))
                DostavaDani = cursor.getInt(cursor.getColumnIndex("broj_dana_isporuke"))
                UkupnaCena = cursor.getDouble(cursor.getColumnIndex("cena_puta_kolicina"))
                PoKomadu = cursor.getDouble(cursor.getColumnIndex("cena_po_komadu"))
                val emp= ModelClassKorpa(id = IdKorpe,naziv = NazivProizvoda,kolicina = KolicinaProizvoda,broj_dana = DostavaDani,UkupnaCena =UkupnaCena,CenaPoKomadu =PoKomadu,idProizvoda =idProizvoda)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return empList
    }
    fun viewPorudzbine():List<ModelClassPorudzbenica>{
        val empList:ArrayList<ModelClassPorudzbenica> = ArrayList<ModelClassPorudzbenica>()
        val selectQuery = "SELECT  * FROM $TABLE_Name6"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var IdPorudzbine: Int
        var AdresaZaDostavu: String
        var DostavaDani: Int
        var UkupnaCena:Double

        if (cursor.moveToFirst()) {
            do {
                IdPorudzbine= cursor.getInt(cursor.getColumnIndex("id_porudzba"))
                AdresaZaDostavu= cursor.getString(cursor.getColumnIndex("adresa_za_dostavu"))
                DostavaDani = cursor.getInt(cursor.getColumnIndex("vreme_za_dostavu"))
                UkupnaCena = cursor.getDouble(cursor.getColumnIndex("ukupno_za_placanje"))
                val emp= ModelClassPorudzbenica(idPorudzbine = IdPorudzbine,adresaDostave = AdresaZaDostavu,vremeDostave = DostavaDani,UkupnoZaPlacanje = UkupnaCena)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return empList
    }
    fun updateKolicina(id: Int,kolicina:Int):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(REC_KOLICINA_PROIZVOD, kolicina)
        val success = db.update(TABLE_Name3, contentValues,"$REC_ID_PROIZVOD = $id",null)
        db.close()
        return success
    }
    fun getKolicina(id: Int): Int {
        val labels: MutableList<Int> = ArrayList()
        val selectQuery = "SELECT $REC_KOLICINA_PROIZVOD FROM $TABLE_Name3 WHERE $REC_ID_PROIZVOD "+ "= $id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getInt(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        var Kolicina= labels[labels.size-1]
        return Kolicina
    }
    fun IzbrisiIzKorpe(id:Int):Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_Name4,"$REC_ID_KORPA = $id",null)
        db.close()
        return success
    }
    fun IzbrisiSveIzKorpe():Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_Name4,null,null)
        db.close()
        return success
    }
    fun addPorudzbina(emp: ModelClassPorudzbenica):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(REC_ID_Porudzba, emp.idPorudzbine)
        contentValues.put(REC_ADRESA_DOSTAVE, emp.adresaDostave)
        contentValues.put(REC_VREME_DOSTAVE,emp.vremeDostave)
        contentValues.put(REC_PLACANJE_DOSTAVE,emp.UkupnoZaPlacanje)
        val success = db.insert(TABLE_Name6, null, contentValues)
        db.close()
        return success
    }
    fun updateProizvod(emp: ModelClassProizvod):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(REC_ID_PROIZVOD, emp.id)
        contentValues.put(REC_NAME_PROIZVOD, emp.naziv)
        contentValues.put(REC_CENA_PROIZVOD, emp.cena)
        contentValues.put(REC_KOLICINA_PROIZVOD, emp.kolicina)
        contentValues.put(REC_STANDARDNO_SLANJE, emp.slanje)
        contentValues.put(REC_DRZAVA_PROIZVOD, emp.drzava)

        val success = db.update(TABLE_Name3, contentValues,"$REC_ID_PROIZVOD = ${emp.id}",null)
        db.close()
        return success
    }
}