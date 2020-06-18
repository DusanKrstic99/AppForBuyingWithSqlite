package rs.edu.viser.pmu.aplikacijazakupovinu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_izmena.*


class IzmenaFragment: Fragment() {
    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
        val databaseHandler: DataBaseHandler = DataBaseHandler(context)
    val view: View = inflater.inflate(R.layout.fragment_izmena,container,false)
    val Proizvodi: Spinner =view.findViewById(R.id.OdabirProizvoda)
        var SviProizvodi = databaseHandler.viewProizvode()
        var ListaNazivaProizvoda = Array(SviProizvodi.size) { "null" }
        val DrzavaIzmena: Spinner =view.findViewById(R.id.DrzavaIzmena)
        var index = 0
        for (e in SviProizvodi){
            ListaNazivaProizvoda[index] = e.naziv
            index++
        }
        val arrayAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, ListaNazivaProizvoda) }
        if (arrayAdapter != null) {
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        Proizvodi.adapter = arrayAdapter
        val lables: List<String> = databaseHandler.getDrzave()
        val arrayAdapter2 = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, lables) }
        if (arrayAdapter2 != null) {
            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        DrzavaIzmena.adapter = arrayAdapter2
        Proizvodi.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                var  Proizvod = Proizvodi.selectedItem.toString();
                var Id:Int = 0;
                for(e in SviProizvodi){
                    if(Proizvod.equals(e.naziv)){
                        Id = e.id
                    }
                }
               var JedanProizvod =  databaseHandler.viewProizvod(Id)
                IDProizvoda.setText(JedanProizvod[JedanProizvod.size-1].id.toString())
                nazivProizvodaIzmena.setText(JedanProizvod[JedanProizvod.size-1].naziv)
                CenaIzmena.setText(JedanProizvod[JedanProizvod.size-1].cena.toString())
                VremeIzmena.setText(JedanProizvod[JedanProizvod.size-1].slanje.toString())
                KolicinaIzmena.setText(JedanProizvod[JedanProizvod.size-1].kolicina.toString())
                if (arrayAdapter2 != null) {
                    DrzavaIzmena.setSelection(arrayAdapter2.getPosition(JedanProizvod[JedanProizvod.size-1].drzava))
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

    var dugme: Button =view.findViewById(R.id.dodajProizvodIzmena)
    dugme.setOnClickListener {
        if(nazivProizvodaIzmena.text.toString().trim() != "" && CenaIzmena.text.toString() != "" &&  VremeIzmena.text.toString() != "" && KolicinaIzmena.text.toString() != "" && CenaIzmena.text.toString().toDouble() != 0.0  && VremeIzmena.text.toString().toInt() != 0  && KolicinaIzmena.text.toString().toInt() != 0  && DrzavaIzmena.selectedItem.toString().trim() != "" ) {
            val Drzava = DrzavaIzmena.selectedItem.toString();
            val naziv = nazivProizvodaIzmena.text.toString()
            val kolicina = KolicinaIzmena.text.toString().toInt()
            val cena = CenaIzmena.text.toString().toDouble()
            val vreme = VremeIzmena.text.toString().toInt()
            val IDProizvoda = IDProizvoda.text.toString().toInt()

            val status =databaseHandler.updateProizvod(
                ModelClassProizvod(IDProizvoda,naziv,cena,kolicina,vreme,Drzava))

        if (status > -1) {
                Toast.makeText(view.context, "Uspesno izmenjen proizvod", Toast.LENGTH_LONG)
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
        fun newInstance():IzmenaFragment=IzmenaFragment()
    }
}