package rs.edu.viser.pmu.aplikacijazakupovinu


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_korpa.view.*

class KorpaFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_korpa,container,false)
        val lista_vesti=view.Korpa
        val databaseHandler: DataBaseHandler = DataBaseHandler(view.context)
        var   korpa:List<ModelClassKorpa> =databaseHandler.viewKorpa()
        lista_vesti.adapter=MojAdapter(view.context,korpa,this)
        return view
    }
    companion object{
        fun newInstance():KorpaFragment=KorpaFragment()
    }

    private class MojAdapter(context: Context,var korpa: List<ModelClassKorpa>,private val context2:KorpaFragment):BaseAdapter(){
        private val mContext:Context=context
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = context2.layoutInflater
            val rowView = inflater.inflate(R.layout.korpa_custom_list, null, true)
            val naziviCena = rowView.findViewById(R.id.KorpaNazivICena) as TextView
            val IzbrisiIzKorpe =rowView.findViewById(R.id.Izbrisi) as Button
            IzbrisiIzKorpe.setText("Izbrisi ovaj proizvod iz korpe")
            naziviCena.setText("Proizvod:" + korpa[position].naziv+"\n Kolicina:"+korpa[position].kolicina + "\n Ukupna Cena: " + korpa[position].UkupnaCena)
            naziviCena.textSize = 20F
            naziviCena.setOnClickListener {}
            IzbrisiIzKorpe.setOnClickListener {
                val databaseHandler: DataBaseHandler = DataBaseHandler(mContext)
                var status = databaseHandler.IzbrisiIzKorpe(korpa[position].id)
                var status2 = databaseHandler.getKolicina(korpa[position].idProizvoda)
                var status3 = databaseHandler.updateKolicina(korpa[position].idProizvoda,(korpa[position].kolicina+status2))
                if (status > -1 && status3 > -1) {
                    Toast.makeText(mContext, "Uspesno obrisana stavka iz korpe", Toast.LENGTH_LONG)
                        .show()
            }
                else{
                    Toast.makeText(mContext, "Doslo je do greske pri brisanju", Toast.LENGTH_LONG)
                        .show()
                }
            }
            return rowView
        }

        override fun getItem(position: Int): Any {
            return korpa[position]
        }

        override fun getItemId(position: Int): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCount(): Int {
            return korpa.size
        }

    }

}