package rs.edu.viser.pmu.aplikacijazakupovinu

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class PrikazJednog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prikaz_jednog)

        val tekst:Int=intent.getStringExtra("IDProizvoda").toInt()
        val databaseHandler: DataBaseHandler = DataBaseHandler(this)
        var   proizvodi:List<ModelClassProizvod> =databaseHandler.viewProizvod(tekst)
        val Naziv: TextView = findViewById(R.id.NazivPr)
        val Cena: TextView = findViewById(R.id.cenaPr)
        val Kolicina: TextView = findViewById(R.id.KolicinaPr)
        val Dostava: TextView = findViewById(R.id.SlanjePr)
        val Drzava: TextView = findViewById(R.id.DrzavaPr)
        val DodajUKorpu: Button = findViewById(R.id.DodajUKorpu)

        Naziv.setText(proizvodi[proizvodi.size-1].naziv)
        Cena.setText("Cena: "+ proizvodi[proizvodi.size-1].cena.toString())
        Kolicina.setText("Dostupna Kolicina: " +proizvodi[proizvodi.size-1].kolicina.toString())
        Dostava.setText("Broj dana dostave: " +proizvodi[proizvodi.size-1].slanje.toString())
        Drzava.setText("Drzava proizvoda: " + proizvodi[proizvodi.size-1].drzava)

        DodajUKorpu.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.dodavanje, null)
            val databaseHandler: DataBaseHandler = DataBaseHandler(this)
            dialogBuilder.setView(dialogView)
            dialogBuilder.setTitle("Dodavanje u korpu")
            dialogBuilder.setMessage("Unesite zeljenu kolicinu")

            dialogBuilder.setPositiveButton("Dodaj", DialogInterface.OnClickListener { _, _ ->
                var  UnetaKolicinaa = dialogView.findViewById(R.id.KolicinaUnos) as EditText
                if(UnetaKolicinaa.text.toString()!=""){
                var koli = UnetaKolicinaa.text.toString().toInt()
                val kolicina = proizvodi[proizvodi.size-1].kolicina
               var Idkor=  databaseHandler.getIDKorisnika().toString().toInt()
                var DrzavaKorisnika:String = databaseHandler.getDrzavuKorisnika(Idkor).toString()
                var DostavaProizvoda = proizvodi[proizvodi.size-1].slanje
                if(!DrzavaKorisnika.equals(proizvodi[proizvodi.size-1].drzava)){
                    DostavaProizvoda++
                }

                var IdKorpaString = databaseHandler.getIDKorpe().toString()
                var IdKorpe = -1
                if (IdKorpaString == "null") {
                    IdKorpe = 0
                } else IdKorpe = IdKorpaString.toInt()
                IdKorpe++
                if (koli in 1..kolicina) {
                    var Ukupnacena = proizvodi[proizvodi.size-1].cena * koli.toDouble()
                    val status = databaseHandler.addKorpa(
                        ModelClassKorpa(
                            IdKorpe,
                            proizvodi[proizvodi.size-1].id,
                            Naziv.text.toString(),
                            proizvodi[proizvodi.size-1].cena,
                            koli,
                            DostavaProizvoda,
                            Ukupnacena

                        )
                    )
                    val status2 = databaseHandler.updateKolicina(proizvodi[proizvodi.size-1].id,(proizvodi[proizvodi.size-1].kolicina - koli))
                    if (status > -1 && status2 > -1) {
                        Toast.makeText(this, "Stavka uspesno dodata", Toast.LENGTH_LONG)
                            .show()

                    }}
                else {
                    Toast.makeText(this, "Molimo unesite ispravnu kolicinu", Toast.LENGTH_LONG)
                        .show()
                }
                } else {
                    Toast.makeText(this, "Molimo unesite ispravnu kolicinu", Toast.LENGTH_LONG)
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
        }

    }

