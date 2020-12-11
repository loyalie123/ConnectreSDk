package com.loyalie.connectre.ui.register


import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.custom_views.Spinner_Adapter
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.data.domainList
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject


class RegisterAct : BaseActivity(), View.OnClickListener {
    lateinit var viewModel: RegVM
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    var first = true
    private var domainList: ArrayList<domainList> = ArrayList<domainList>()
    private var domainItems: ArrayList<String> = ArrayList<String>()
    var domain = ""
    var adapter: Spinner_Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        suffxTV.setText("@")

        viewModel = viewModelProvider(viewModelFactory)
        viewModel.getDomainList()
//        alertForReg()
        initToolbar()
        registerBtn.setOnClickListener(this)
        backArrowIV.setOnClickListener(this)
        observeVM()
//        spnnerdomain.prompt=getString(R.string.default_text)
//        domain = "Select Domain"

        adapter = Spinner_Adapter(this, domainItems, selectedItem)



        spnnerdomain.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View?,
                position: Int, id: Long
            ) {
                domain = arg0!!.getItemAtPosition(position).toString()

                if (!domain.equals("Select Domain", ignoreCase = true)) {
                    adapter!!.selectedItem = position
                } else {
                    domain = ""
                    //do your code
                    adapter!!.selectedItem = -1
//                    val textView = arg0.getChildAt(0) as TextView
//                    textView.setTextColor(Color.RED)


//                    }
                }
                adapter!!.notifyDataSetChanged()
/*
                if (first) {
                    first = false;
                } else {

                    if (position == 0) {

                    } else {*/


            }

            //            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {
                selectedItem = -1
                domain = ""

            }
        }
        /* adapter = object : ArrayAdapter<String>(
            this,
            R.layout.custom_spinner_main,
            domainItems
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup?
            ): View? {
                var v: View? = null
                v = super.getDropDownView(position, null, parent)
                val tv = v.findViewById<View>(android.R.id.text1) as TextView


                // If this is the selected item position
                if (position == selectedItem) {
                    v.setBackgroundColor(Color.BLACK)
                    tv.setTextColor(Color.WHITE)
                } else { // for other views
                    v.setBackgroundColor(Color.WHITE)
                    tv.setTextColor(Color.BLACK)
                }
                return v
            }
        }*/
// set Adapter
//        adapter!!.setDropDownViewResource(R.layout.custom_single_spinner)
        // set Adapter
        spnnerdomain.adapter = adapter

        /*  val dataAdapter: ArrayAdapter<String> = object :
              ArrayAdapter<String?>(this, R.layout.custom_single_spinner, list) {

          }*/


//        spnnerdomain.setAdapter(dataAdapter)
    }

    var selectedItem = -1
    private fun initToolbar() {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val extractedColor = resources.getColor(R.color.black)
        registerToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))
        titleTV.text = "Register"
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.registerBtn -> validateViews()
            R.id.backArrowIV -> onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
    }

    var emailaddress: String = ""
    private fun validateViews() {
        if (emailET.getTrimText().isNotEmpty())
            emailaddress = emailET.getTrimText() + "@" + domain
        if (nameET.getTrimText().isEmpty()) {
            emailET.error = null
            mobET.error = null
            designationET.error = null
            compNameET.error = null
            empidTV.error = null
            nameET.error = "Please enter name"
//            "Please enter name".toast(this)
            nameET.requestFocus()
        } /*else if (mobET.getTrimText().isEmpty()) {
            emailET.error = null
            nameET.error = null
            designationET.error = null
            compNameET.error = null
            empidTV.error = null
            mobET.setError("Please enter mobile number")
//            "Please Enter mobile number".toast(this)
            mobET.requestFocus()
        } else if (!mobET.getTrimText().isEmpty() && mobET.getTrimText().length < 10) {
//            "Please enter valid mobile number".toast(this)
            mobET.setError("Please enter valid mobile number")
            mobET.requestFocus()
            emailET.error = null
            nameET.error = null
            designationET.error = null
            compNameET.error = null
            empidTV.error = null
        } */ else if (emailET.getTrimText().isEmpty()) {
            emailET.setError("Please enter email ID")
            emailET.requestFocus()
            nameET.error = null
            mobET.error = null
            designationET.error = null
            compNameET.error = null
            empidTV.error = null
        } else if (domain == "") {
            "Please choose a domain".toast(this)
            emailET.requestFocus()
            nameET.error = null
            mobET.error = null
            designationET.error = null
            compNameET.error = null
            empidTV.error = null
            if (emailET.getTrimText().isNotEmpty())
                emailaddress = emailET.getTrimText() + "@" + domain
        } else if (emailaddress != "" && !emailaddress.isEmail()) {
            emailET.setError("Please enter a valid email ID")
            emailET.requestFocus()
            nameET.error = null
            mobET.error = null
            designationET.error = null
            compNameET.error = null
            empidTV.error = null
        } else if (compNameET.getTrimText().isEmpty()) {
            emailET.error = null
            mobET.error = null
            designationET.error = null
            nameET.error = null
            empidTV.error = null
            compNameET.setError("Please enter your company name")
//            "Please enter your company name".toast(this)
            compNameET.requestFocus()
        } else if (designationET.getTrimText().isEmpty()) {
            emailET.error = null
            mobET.error = null
            nameET.error = null
            compNameET.error = null
            empidTV.error = null
            designationET.setError("Please enter your designation")
//            "Please enter your designation".toast(this)
            designationET.requestFocus()
        } else if (empidTV.getTrimText().isEmpty()) {
            emailET.error = null
            mobET.error = null
            nameET.error = null
            compNameET.error = null
            designationET.error = null
            empidTV.error = "Please enter empolyee id"
//            "Please enter empolyee id".toast(this)
            empidTV.requestFocus()
        } else {
            hideKeyboard()
            viewModel.registerEmp(
                nameET.getTrimText(),

                emailET.getTrimText() + "@" + domain,
                "",
                designationET.getTrimText().toString(),
                compNameET.getTrimText(),
                empidTV.getTrimText(),
                ""
            )
        }

    }

    private fun observeVM() {
        viewModel.domainHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    /* projectList.clear()
                     projectList.add(it.data)
                     projectItems.a(it.data)*/
                    domainList!!.clear()
                    domainItems.clear()
                    domainList!!.addAll(it.data)
                    domainItems.add(0, "Select Domain")
                    for (i in 0 until it.data.size)
                        it.data[i].domainName?.let { it1 -> domainItems.add(it1) }

//                    var text1=findViewById<TextView>(android.R.id.text1)
//                    text1.hint = "Select Domain"
                    adapter!!.notifyDataSetChanged()

                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.load()
                }
            }
        })
        viewModel.userHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    preferenceStorage.userId = it.data.second.userDetails.user_id.toString()
                    preferenceStorage.userEmail = it.data.second.userDetails.user_email.toString()
                    preferenceStorage.userName = it.data.second.userDetails.user_name.toString()
                    preferenceStorage.userPhone = it.data.second.userDetails.user_phone.toString()
                    preferenceStorage.userType = it.data.second.userDetails.userType
                    preferenceStorage.userAvatar = it.data.second.userDetails.user_avatar
                    alertForReg()

                }
                is ViewState.Error -> {
                    loadingDialog.remove()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.load()
                }
            }
        })


    }

    fun alertForReg() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert, viewGroup, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()
        val handler = Handler()
        val runnable = Runnable {
            if (alertDialog != null && alertDialog.isShowing) {

                alertDialog.dismiss()
                this@RegisterAct.finish()
//                EnterPhoneNumberActivity.start(this@RegisterAct, false, true, false)
            }
        }
        alertDialog.setOnDismissListener { handler.removeCallbacks(runnable) }
        handler.postDelayed(runnable, 1000)
    }


}
