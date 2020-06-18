package rs.edu.viser.pmu.aplikacijazakupovinu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val drzave = findViewById<Spinner>(R.id.Drzave)
        loadSpinnerData(drzave)

    }

    fun PredjiNaProizvode(view: View) {

        val ime = findViewById<EditText>(R.id.Ime)
        val prezime = findViewById<EditText>(R.id.Prezime)
        val email = findViewById<EditText>(R.id.Email)
        val adresa = findViewById<EditText>(R.id.Adresa)
        val drzave = findViewById<Spinner>(R.id.Drzave)
        val Drzava = drzave.selectedItem.toString();
        val Ime = ime.text.toString()
        val Prezime = prezime.text.toString()
        val Email = email.text.toString()
        val Adresa = adresa.text.toString()
        if (Ime.trim() != "" && Prezime.trim() != "" && Email.trim() != "" && Drzava.trim() != "" && Adresa.trim() != "") {
            val databaseHandler: DataBaseHandler = DataBaseHandler(this)
            var IdKorisnikString = databaseHandler.getIDKorisnika().toString()
            var IdKorisnik = -1
            if (IdKorisnikString == "null") {
                IdKorisnik = 0
            } else IdKorisnik = IdKorisnikString.toInt()
            IdKorisnik++
            val status =databaseHandler.addKorisnika(ModelClassKorisnik(IdKorisnik,Ime,Prezime,Email,Adresa,Drzava))
            if (status > -1) {
                Toast.makeText(applicationContext, "Dobro dosli", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(this, Proizvodi::class.java)
                startActivity(intent)
            }
    }
    else
    {
        Toast.makeText(applicationContext, "Molimo popunite sva polja", Toast.LENGTH_LONG)
            .show()
    }
}
    fun loadSpinnerData(spinner: Spinner) {
        val db = DataBaseHandler(applicationContext)
        val lables: List<String> = db.getDrzave()
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, lables
        )
        dataAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter;
    }
}
