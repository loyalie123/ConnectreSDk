package com.loyalie.connectresdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.loyalie.connectre.util.BASE_URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv=findViewById<Button>(R.id.tvTitle) as Button
        BASE_URL ="http://18.140.57.92:8080/Loyalie/"
        tv.setOnClickListener {
            try {
                val myIntent = Intent(this, Class.forName("com.loyalie.connectre.ui.home.HomeActivity"))
                startActivity(myIntent)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
    }}
}