package com.loyalie.connectre.ui.register

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.custom_views.MultiSelectionSpinner
import com.loyalie.connectre.data.ProjectItem
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.activity_reg_customer.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import javax.inject.Inject

class RegCustomerAct : BaseActivity(), View.OnClickListener,
    MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    lateinit var viewModel: RegVM
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var projectItems: ArrayList<String> = ArrayList<String>()
    private var projectList: ArrayList<String> = ArrayList<String>()

    private var projectTotalList: ArrayList<ProjectItem> = ArrayList<ProjectItem>()

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_customer)
        viewModel = viewModelProvider(viewModelFactory)

        viewModel.getProjectList()
        initToolbar()
        registerBtn.setOnClickListener(this)
        backArrowIV.setOnClickListener(this)
        spinnerProject.setSelectionColor()
        observeVM()


    }

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

    private fun validateViews() {
        if (nameET.getTrimText().isEmpty()) {
            emailET.error = null
            mobET.error = null
            flatET.error = null
            nameET.setError("Please enter name")
//            "Please enter name".toast(this)
            nameET.requestFocus()
        } /*else if (mobET.getTrimText().isEmpty()) {
            emailET.error = null
            nameET.error = null
            flatET.error = null
            mobET.setError("Please enter mobile number")
//            "Please enter mobile number".toast(this)
            mobET.requestFocus()
        } else if (!mobET.getTrimText().isEmpty() && mobET.getTrimText().length < 10) {
//            "Please enter valid mobile number".toast(this)
            mobET.setError("Please enter valid mobile number")
            mobET.requestFocus()
            emailET.error = null
            nameET.error = null
            flatET.error = null
        }*/ else if (emailET.getTrimText().isEmpty()) {
            emailET.setError("Please enter email ID")
            emailET.requestFocus()
            nameET.error = null
            mobET.error = null
            flatET.error = null
        } else if (!emailET.getTrimText().isEmail()) {
            emailET.setError("Please enter a valid email ID")
            emailET.requestFocus()
            nameET.error = null
            mobET.error = null
            flatET.error = null

        } else if (p_idList == "") {
            emailET.error = null
            mobET.error = null

            nameET.error = null

            spinnerProject.requestFocus()
            "Please choose atleast one project".toast(this)
            flatET.error = null
        } else if (flatET.getTrimText().isEmpty()) {
            emailET.error = null
            mobET.error = null

            nameET.error = null
            flatET.setError("Please enter flat no")
            flatET.requestFocus()
//            "Please enter flat no".toast(this)
            flatET.requestFocus()
        } else {
            hideKeyboard()

            viewModel.registerEmp(
                nameET.getTrimText(),

                emailET.getTrimText(),
                flatET.getTrimText(),
                "",
                "",
                "",
                p_idList
            )
        }

    }

    private fun observeVM() {
        viewModel.projectHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.remove()
                    /* projectList.clear()
                     projectList.add(it.data)
                     projectItems.a(it.data)*/
                    projectTotalList!!.clear()
                    projectTotalList!!.addAll(it.data)
                    for (i in 0 until it.data.size)
                        it.data[i].projectName?.let { it1 -> projectItems.add(it1) }
                    spinnerProject.setItems(projectItems);
                    spinnerProject.setListener(this)
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

    fun alertForReg() {
        val viewGroup = findViewById(android.R.id.content) as ViewGroup
        val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert, viewGroup, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss()
                    this@RegCustomerAct.finish()
//                    EnterPhoneNumberActivity.start(this@RegCustomerAct, false, true, false)
                }
            }
        }
        alertDialog.setOnDismissListener { handler.removeCallbacks(runnable) }
        handler.postDelayed(runnable, 1000)
    }

    var p_idList = ""
    var isSelecxted = false
    /* override fun onItemsSelected(
         selected: BooleanArray?,
         indices: Int,
         items: List<String>
     ) {
         projectList.clear()
 isSelecxted=false
         p_idList=""
 //spinnerProject.setItems(selected)
         for (i in 0 until projectTotalList!!.size){
             if (selected?.get(i) == true) {
                 projectList.add(projectTotalList!![i].projectId)
 //        projectItems.add(items[i])
                 isSelecxted=true
                 p_idList = TextUtils.join(",", projectList);
             }

         }

         if(!isSelecxted)
             p_idList=""

     }*/


    override fun selectedIndices(indices: MutableList<Int>) {
        p_idList = ""
        projectList.clear()
        for (i in 0 until indices!!.size) {

            projectList.add(projectTotalList.get(indices.get(i)).projectId)
//        projectItems.add(items[i])
            isSelecxted = true
            p_idList = TextUtils.join(",", projectList);


        }
    }

    override fun selectedStrings(strings: MutableList<String>) {
//        p_idList= TextUtils.join(",", strings);

    }
}
