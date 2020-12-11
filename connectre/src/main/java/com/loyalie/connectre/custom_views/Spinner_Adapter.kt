package com.loyalie.connectre.custom_views


import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.loyalie.connectre.R


class Spinner_Adapter(context: Context, var brands: ArrayList<String>, var selectedItem: Int) :
    BaseAdapter(), SpinnerAdapter {
    var context: Context

    override fun getCount(): Int {
        return brands.size
    }

    override fun getItem(position: Int): Any {
        return brands[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.custom_spinner_main, null)
        val textView = view.findViewById(R.id.titlemain) as TextView
        if (position === 0) {
            textView.text = ""
            textView.setHint("Select Domain") //"Hint to be displayed"
        } else {
            textView.text = brands[position]
        }


        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (position == 0) {

            view = View.inflate(context, R.layout.spinner_prompt, null);
            var textView = view.findViewById(R.id.propmt) as TextView
            textView.setText("Select Domain");
            textView.setOnClickListener {

            }


        } else {
            view = View.inflate(context, R.layout.custom_single_spinner, null)
            val textView = view.findViewById(R.id.tvTitleItem) as TextView
            textView.text = brands[position]

            if (position == selectedItem) {
                textView.setBackgroundColor(Color.BLACK)
                textView.setTextColor(Color.WHITE)
            } else { // for other views
                textView.setBackgroundColor(Color.WHITE)
                textView.setTextColor(Color.BLACK)
            }
//        }
        }
        return view
    }

    init {
        this.context = context
    }

    interface onCallback {
        fun setOnclick(position: Int)
    }
}