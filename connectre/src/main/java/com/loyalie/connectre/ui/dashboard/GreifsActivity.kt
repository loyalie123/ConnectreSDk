package com.loyalie.connectre.ui.dashboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.data.greifList
import com.loyalie.connectre.ui.home.HomeActivity
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.Utils
import com.loyalie.connectre.util.toast
import kotlinx.android.synthetic.main.activity_greifs.*
import kotlinx.android.synthetic.main.alert_layout.view.*
import kotlinx.android.synthetic.main.close_title_toolbar.*

class GreifsActivity : AppCompatActivity() {
    var adapter: GreifAdapter? = null
    var list = ArrayList<greifList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greifs)
        initToolbar()
        list.clear()
        list.add(greifList("", "", "", "", "Pending", 1))
        list.add(greifList("", "", "", "", "Resolved", 2))
        initRV()
        addBtn.setOnClickListener {
            alertShow()
        }

    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        greifToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = ""
        backArrowIV.setOnClickListener {
            onBackPressed()
        }
        closeIv.setOnClickListener {    
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun initRV() {

        val layoutManager = LinearLayoutManager(this)
        greifRcv.layoutManager = layoutManager
        adapter = GreifAdapter(this, list)
        greifRcv.adapter = adapter
        /*  scrollListener = object : PaginatingScrollListener(layoutManager) {
              override fun onLoadMore() {
                  viewModel.getData(projectId, isInitial = false, isRefresh = false)
              }
          }
  */
//        documentationRV.addOnScrollListener(scrollListener)
    }

    fun alertShow() {
        val dialogBuilder = AlertDialog.Builder(this).create();
        val inflater = this.getLayoutInflater();
        val dialogView = inflater.inflate(R.layout.alert_layout, null);

        val extractedColor = ConnectReApp.themeColor
        dialogView.alertTitle.setTextColor(extractedColor)
        dialogView.buttonCancel.setTextColor(extractedColor)
        dialogView.buttonSubmit.setTextColor(extractedColor)
        dialogView.buttonCancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
       dialogView. buttonSubmit.setOnClickListener {
            "Feedback added successfully".toast(this)
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, GreifsActivity::class.java).apply {

            })

        }
    }
}
