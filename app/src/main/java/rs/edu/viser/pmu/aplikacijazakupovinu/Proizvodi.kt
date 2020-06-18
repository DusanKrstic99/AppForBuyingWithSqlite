package rs.edu.viser.pmu.aplikacijazakupovinu

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class Proizvodi : AppCompatActivity() {
    lateinit var toolbar: ActionBar

    fun openFragment(fragment: Fragment){
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    val mOnNavigationItemSelectedLister= BottomNavigationView.OnNavigationItemSelectedListener {
            item ->
        when(item.itemId){
            R.id.navigation_proizvodi->{
                toolbar.title="Proizvodi"
                val vestiFragment=ProizvodiFragment.newInstance()
                openFragment(vestiFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dodaj_proizvod->{
                toolbar.title="Dodaj proizvod"
                val dodajVestFragment=DodajFragment.newInstance()
                openFragment(dodajVestFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_izmeni_proizvod->{
                toolbar.title="Izmeni proizvod"
                val dodajVestFragment=IzmenaFragment.newInstance()
                openFragment(dodajVestFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pregledaj_korpu->{
                toolbar.title="Korpa"
                val dodajVestFragment=KorpaFragment.newInstance()
                openFragment(dodajVestFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pregledaj_narudzbine->{
                toolbar.title="Porudzbine"
                val dodajVestFragment=PoruzbineFragment.newInstance()
                openFragment(dodajVestFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proizvodi)
        toolbar=supportActionBar!!
        val bottomNavigationView:BottomNavigationView=findViewById(R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedLister)
        openFragment(ProizvodiFragment.newInstance())
    }
    fun Zavrsi(view: View) {
        val databaseHandler: DataBaseHandler = DataBaseHandler(this)
        var   korpa:List<ModelClassKorpa> =databaseHandler.viewKorpa()
if(korpa.isNotEmpty()) {
    val dialogBuilder = AlertDialog.Builder(this)
    val inflater = this.layoutInflater
    val dialogView = inflater.inflate(R.layout.zavrsi_porudzbinu, null)
    dialogBuilder.setView(dialogView)
    var IdPorudzbinaString = databaseHandler.getIDPorudzbina().toString()
    var idKorinsika = databaseHandler.getIDKorisnika()
    var adresa = idKorinsika?.let { databaseHandler.getAdresuKorisnika(it) }.toString()
    var IdPoruzbina = -1
    if (IdPorudzbinaString == "null") {
        IdPoruzbina = 0
    } else IdPoruzbina = IdPorudzbinaString.toInt()
    IdPoruzbina++
    val adresaBtn = dialogView.findViewById(R.id.PromenaAdrese) as Button
    val Dostava = dialogView.findViewById(R.id.BrzinaDostave) as Spinner
    var listaBrzina: ArrayList<String> = ArrayList()
    listaBrzina.addAll(listOf("Standardna", "Spora(-10%)", "Brza(+20%)"))
    val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaBrzina)
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    Dostava.adapter = arrayAdapter
    adresaBtn.setOnClickListener {
        val dialogBuilder2 = AlertDialog.Builder(this)
        dialogBuilder2.setTitle("Promena adrese dostave")
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.unos_nove_adrese, null)
        dialogBuilder2.setView(dialogView)
        val Adresanova = dialogView.findViewById(R.id.AdresaNova) as EditText

        dialogBuilder2.setPositiveButton("Promeni", DialogInterface.OnClickListener { _, _ ->
            if (Adresanova.text.toString() != "") {
                adresa = Adresanova.text.toString()
                Toast.makeText(this, "Uspesno promenjena adresa dostave", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(this, "Niste uneli adresu", Toast.LENGTH_LONG)
                    .show()
            }
        })
        dialogBuilder2.setNegativeButton(
            "Odustani",
            DialogInterface.OnClickListener { dialog, which ->
                //pass
            })
        val b = dialogBuilder2.create()
        b.show()
    }
    dialogBuilder.setTitle("Zavrsi Poruzbinu")
    dialogBuilder.setPositiveButton("Dodaj", DialogInterface.OnClickListener { _, _ ->
        val dostava = Dostava.selectedItem.toString();
        var UkupnaCena: Double = 0.0;
        var BrojdanaDostave: Int = 0;
        for (index2 in korpa.indices) {
            UkupnaCena = UkupnaCena + korpa[index2].UkupnaCena
            if (BrojdanaDostave < korpa[index2].broj_dana) {
                BrojdanaDostave = korpa[index2].broj_dana
            }
        }
        if (dostava.equals(listaBrzina[1])) {
            UkupnaCena -= UkupnaCena * 0.1
            BrojdanaDostave += 2
        } else if (dostava.equals(listaBrzina[2])) {
            UkupnaCena += UkupnaCena * 0.2
            BrojdanaDostave -= 2
        }

                val status = databaseHandler.addPorudzbina(
                    ModelClassPorudzbenica(
                        IdPoruzbina,
                        adresa,
                        BrojdanaDostave,
                        UkupnaCena
                    )
                )
                val status2 = databaseHandler.IzbrisiSveIzKorpe()
        if (status > -1 && status2 > -1) {
            Toast.makeText(
                this,"Uspesno potvrdjena porudzbina",Toast.LENGTH_LONG)
                .show()
            val dialogBuilder3 = AlertDialog.Builder(this)
            dialogBuilder3.setTitle("Uspesna porudzbina")
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.ispis_uspesna_porudzbina, null)
            dialogBuilder3.setView(dialogView)
            var IspisPorudzbina:TextView = dialogView.findViewById(R.id.IspisPorudzbina)
            IspisPorudzbina.setText("Adresa za dostavu :" + adresa + "\n Broj dana dostave :" + BrojdanaDostave + "\n Ukupno za placanje: " + UkupnaCena)
            dialogBuilder3.setNeutralButton("Izadji") { dialog, which ->
                //pass
            }
            val b = dialogBuilder3.create()
            b.show()
        }
        else {
                Toast.makeText(this, "Doslo je do greske", Toast.LENGTH_LONG)
                    .show()
            }

    })
    dialogBuilder.setNegativeButton(
        "Odustani",
        DialogInterface.OnClickListener { dialog, which ->
            //pass
        })
    val b = dialogBuilder.create()
    b.show()
}
        else{
    Toast.makeText(
        this, "Nemate nijedan proizvod u korpi",Toast.LENGTH_LONG)
        .show()
}
    }
}
