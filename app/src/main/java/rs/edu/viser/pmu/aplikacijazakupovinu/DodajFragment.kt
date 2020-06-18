package rs.edu.viser.pmu.aplikacijazakupovinu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_dodaj.*

class DodajFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_dodaj,container,false)
        val Drzave: Spinner =view.findViewById(R.id.Drzava)
        loadSpinnerData(Drzave)
        var Drzava:String
        var naziv:String
        var kolicina:Int
        var cena:Double
        var vreme:Int
        var dugme: Button =view.findViewById(R.id.dodajProizvod)
        dugme.setOnClickListener {
            if(nazivProizvoda.text.toString().trim() != "" && Cena.text.toString() != "" &&  Vreme.text.toString() != "" && Kolicina.text.toString() != "" && Cena.text.toString().toDouble() != 0.0  && Vreme.text.toString().toInt() != 0  && Kolicina.text.toString().toInt() != 0  && Drzave.selectedItem.toString().trim() != "" ) {
                Drzava = Drzave.selectedItem.toString();
                naziv = nazivProizvoda.text.toString()
                kolicina = Kolicina.text.toString().toInt()
                cena = Cena.text.toString().toDouble()
                vreme = Vreme.text.toString().toInt()
                val databaseHandler: DataBaseHandler = DataBaseHandler(view.context)
                var IdProizvodString = databaseHandler.getIDProizvoda().toString()
                var IdProizvod = -1
                if (IdProizvodString == "null") {
                    IdProizvod = 0
                } else IdProizvod = IdProizvodString.toInt()
                IdProizvod++
                val status =databaseHandler.addProizvod(ModelClassProizvod(IdProizvod,naziv,cena,kolicina,vreme,Drzava))
                if (status > -1) {
                    Toast.makeText(view.context, "Uspesno dodat proizvod", Toast.LENGTH_LONG)
                        .show()
                }
            }else{
                Toast.makeText(view.context, "Molimo popunite sva polja", Toast.LENGTH_LONG)
                    .show()
            }
        }
        return view
    }

    companion object{
        fun newInstance():DodajFragment=DodajFragment()
    }
    fun loadSpinnerData(spinner: Spinner) {
        val db = DataBaseHandler(context)
        val lables: List<String> = db.getDrzave()
        val dataAdapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, lables
            )
        }
        if (dataAdapter != null) {
            dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = dataAdapter;
    }
}