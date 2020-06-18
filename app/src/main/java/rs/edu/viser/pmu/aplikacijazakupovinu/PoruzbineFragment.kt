package rs.edu.viser.pmu.aplikacijazakupovinu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_porudzbine.view.*

class PoruzbineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =inflater.inflate(R.layout.fragment_porudzbine,container,false)
        val lista_vesti=view.lista_porudzbina
        val databaseHandler: DataBaseHandler = DataBaseHandler(view.context)
        var   porudzbenica:List<ModelClassPorudzbenica> =databaseHandler.viewPorudzbine()
        lista_vesti.adapter=MojAdapter(view.context,porudzbenica)
        return view
    }
    companion object{
        fun newInstance():PoruzbineFragment=PoruzbineFragment()
    }

    private class MojAdapter(context: Context, var porudzbenica:List<ModelClassPorudzbenica>): BaseAdapter(){
        private var mContext: Context =context

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var nazivPorudzbine: TextView = TextView(mContext)
            nazivPorudzbine.setText("Adresa:" + porudzbenica[position].adresaDostave+"\n Ukupno za placanje:"+porudzbenica[position].UkupnoZaPlacanje +"\n Broj dana dostave:"+porudzbenica[position].vremeDostave )
            nazivPorudzbine.textSize = 20F
            nazivPorudzbine.setOnClickListener{
            }
            return nazivPorudzbine
        }


        override fun getItem(position: Int): Any {
            return porudzbenica[position]
        }

        override fun getItemId(position: Int): Long {
            return  porudzbenica[position].idPorudzbine.toLong()
        }

        override fun getCount(): Int {
            return porudzbenica.size
        }

    }
}