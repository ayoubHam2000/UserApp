package com.example.userApp.Adapters.SearchAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.userApp.R
import java.util.zip.Inflater

class SearchAdapter(context : Context, itemList : ArrayList<String>) {

    val congregations = ArrayList<String>()

     val setArrayAdapter =  object : ArrayAdapter<String>(context, 0, congregations){

        @SuppressLint("ViewHolder")
        override fun getView(position: Int,  convertView: View?, parent: ViewGroup): View {

            val view = LayoutInflater.from(context).inflate(R.layout.search_adapter, parent, false)
            val name = view.findViewById<TextView>(R.id.name)
            name.text = congregations[position]
            return view
        }

        private val vocabularyFilter: Filter  = object : Filter(){
            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                return object : FilterResults(){}
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                addAll( itemList )
                //notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return ( (resultValue as String) )
            }
        }

        override fun getFilter(): Filter {
            return vocabularyFilter
        }

    }

}