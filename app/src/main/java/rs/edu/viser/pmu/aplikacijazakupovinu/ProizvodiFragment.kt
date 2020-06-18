package rs.edu.viser.pmu.aplikacijazakupovinu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_proizvodi.view.*

class ProizvodiFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =inflater.inflate(R.layout.fragment_proizvodi,container,false)
        val lista_vesti=view.lista_proizvoda
        val databaseHandler: DataBaseHandler = DataBaseHandler(view.context)
        var   proizvodi:List<ModelClassProizvod> =databaseHandler.viewProizvode()
        lista_vesti.adapter=MojAdapter(view.context,proizvodi)
        return view
    }
    companion object{
        fun newInstance():ProizvodiFragment=ProizvodiFragment()
    }

    private class MojAdapter(context: Context,var proizvodi:List<ModelClassProizvod>): BaseAdapter(){
        private var mContext: Context =context

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var naziviCena: TextView = TextView(mContext)
                naziviCena.setText("Proizvod:" + proizvodi[position].naziv+"\n Cena:"+proizvodi[position].cena)
                naziviCena.textSize = 20F
                naziviCena.setOnClickListener{
                    val intent= Intent(mContext.applicationContext,PrikazJednog::class.java)
                    intent.putExtra("IDProizvoda",proizvodi[position].id.toString())
                    mContext.startActivity(intent)
              }
            return naziviCena
        }


        override fun getItem(position: Int): Any {
            return proizvodi[position]
        }

        override fun getItemId(position: Int): Long {
            return  proizvodi[position].id.toLong()
           }

        override fun getCount(): Int {
            return proizvodi.size
        }

    }
}