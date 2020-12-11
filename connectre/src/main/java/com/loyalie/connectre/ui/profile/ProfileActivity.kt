package com.loyalie.connectre.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.ui.change_password.ChangePhoneNoActivity
import com.loyalie.connectre.ui.home.HomeActivity
import com.loyalie.connectre.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.app_bar_centre_title.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ProfileActivity : BaseActivity(), View.OnClickListener {
    private var isEditMode = false
    private val CROP_REQUST = 125
    private var imageFile: File? = null

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }
    var myCalendar1: Calendar? = null
    var myCalendar2: Calendar? = null
    var date: DatePickerDialog.OnDateSetListener? = null
    var anniversary_date: DatePickerDialog.OnDateSetListener? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ProfileVM

    @Inject
    lateinit var picasso: Picasso

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.editImageBtn -> {
                hideKeyboard()
                ChooserDialog(this)
            }

            R.id.changePhoneNoTV -> {
                val intent = Intent(this, ChangePhoneNoActivity::class.java)
                startActivityForResult(intent,1001)
                overridePendingTransition(R.anim.enter_final, R.anim.exit_final)

            }
            R.id.dobTV -> {

                DatePickerDialog(
                    this, R.style.customCalender, date, myCalendar1!!
                        .get(Calendar.YEAR), myCalendar1!!.get(Calendar.MONTH),
                    myCalendar1!!.get(Calendar.DAY_OF_MONTH)

                ).show()
            }
            R.id.anniversaryTV -> {

                DatePickerDialog(
                    this, R.style.customCalender, anniversary_date, myCalendar2!!
                        .get(Calendar.YEAR), myCalendar2!!.get(Calendar.MONTH),
                    myCalendar2!!.get(Calendar.DAY_OF_MONTH)
                ).show()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.getUserData()
        initToolbar()
        myCalendar1 = Calendar.getInstance()
        myCalendar2 = Calendar.getInstance()
        initClickListener()

        profileNameET.isFocusable = false
        emailIDET.isFocusable = false
        dobTV.isFocusable = false
        anniversaryTV.isFocusable = false
        dobTV.isClickable = false
        anniversaryTV.isClickable = false

        showDate()
        observeVM()
    }

    private fun showDate() {
        val extractedColor = ConnectReApp.themeColor

        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub

            myCalendar1!!.set(Calendar.YEAR, year)
            myCalendar1!!.set(Calendar.MONTH, monthOfYear)
            myCalendar1!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            updateLabel("dob")
        }

        anniversary_date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // TODO Auto-generated method stub

                myCalendar2!!.set(Calendar.YEAR, year)
                myCalendar2!!.set(Calendar.MONTH, monthOfYear)
                myCalendar2!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateLabel("anniversary")
            }


    }

    private fun updateLabel(s: String) {
        val myFormat = "dd MMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        if (s == "dob")
            dobTV.text = sdf.format(myCalendar1!!.time)
        else
            anniversaryTV.text = sdf.format(myCalendar2!!.time)
    }

    override fun onResume() {
        super.onResume()
        prefillFields()
    }

    private fun initClickListener() {
        editImageBtn.setOnClickListener(this)
        changePhoneNoTV.setOnClickListener(this)
        dobTV.setOnClickListener(this)
        anniversaryTV.setOnClickListener(this)
    }

    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        profileToolbar.setBackgroundColor(extractedColor)
        window.setStatusBarColor(Utils.darkenColor(extractedColor, 0.8f))


//        setSupportActionBar(profileToolbar as Toolbar?)
       profileTitleTV.text = "Profile"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        changePhoneNoTV.setTextColor(extractedColor)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        if (isEditMode) {
            menu?.findItem(R.id.edit)?.setVisible(false)
            menu?.findItem(R.id.done)?.setVisible(true)
        } else {
            menu?.findItem(R.id.edit)?.setVisible(true)
            menu?.findItem(R.id.done)?.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.edit) toggleToEditMode()
        else if (item?.itemId == R.id.done) {
            val name = profileNameET.getTrimText()
            val email = emailIDET.getTrimText()
            val dob = dobTV.text.toString().convertToyyyyMMdd()
            val anniveersarydtae = anniversaryTV.text.toString().convertToyyyyMMdd()
            if (name.isBlank()) {
                profileNameET.setError("Please enter your name")
                profileNameET.requestFocus()
            } else if (email.isBlank()) {
                emailIDET.setError("Please enter email ID")
                emailIDET.requestFocus()
            } else if (!email.isEmail()) {
                emailIDET.setError("Please enter a valid email ID")
                emailIDET.requestFocus()
            } else {
                hideKeyboardInput()
                viewModel.updateUser(name, email, dob, anniveersarydtae)
            }

        } else if (item?.itemId == android.R.id.home) {
            hideKeyboard()
            setResult(HomeActivity.PROFILE_UPDATED)
            onBackPressed()
        }
        return true
    }

    fun toggleToEditMode() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_profile)
        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 300
        TransitionManager.beginDelayedTransition(root, transition)
        constraintSet.applyTo(root)
        emailIDET.setFocusableInTouchMode(false)
//        emailIDET.setSelection(emailIDET.getText().toString().length)
        profileNameET.setFocusableInTouchMode(true)
        profileNameET.setSelection(profileNameET.getText().toString().length)
        isEditMode = true
        dobTV.isEnabled = true
        anniversaryTV.isEnabled = true
        dobTV.isClickable = true
        anniversaryTV.isClickable = true
        invalidateOptionsMenu()
        profileNameET.requestFocus()
    }

    fun toggleToNonEditMode() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_profile)
        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 300
        TransitionManager.beginDelayedTransition(root, transition)
        constraintSet.applyTo(root)
        profileNameET.setTextColor(ContextCompat.getColor(this, R.color.textColorDark))
        profileNameET.setSelection(0)
        profileNameET.isFocusable = false
        emailIDET.isFocusable = false
        hideKeyboard()
        isEditMode = false
        invalidateOptionsMenu()
        dobTV.isFocusable = false
        anniversaryTV.isFocusable = false
        dobTV.isClickable = false
        anniversaryTV.isClickable = false
    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        //Get a reference to your item by id
        val item = menu.findItem(R.id.edit)

        //Here, you get access to the view of your item, in this case, the layout of the item has a FrameLayout as root view but you can change it to whatever you use
        val rootView = item.actionView as TextView

        rootView.setOnClickListener {
            profileNameET.showKeyboard()
            toggleToEditMode()
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CAPTURE_IMG && resultCode == Activity.RESULT_OK) {
            val pathWithRotation = (ChooserUtils.getCapturedImagePathWithRotation(this))
            navigateToCrop(pathWithRotation.first, pathWithRotation.second)
        } else if (requestCode == PICK_IMG && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                //loadingDialog?.show()
                try {
                    AppExecutors.getInstance().diskIo.execute {
                        val pathWithRotn = ChooserUtils.getPickedImageWithPath(data, this)
                        AppExecutors.getInstance().mainThread.execute {
                            //loadingDialog?.dismiss()
                            if (pathWithRotn.first.isNotEmpty()) navigateToCrop(
                                pathWithRotn.first,
                                pathWithRotn.second
                            )
                            else "Unable to import image".toast(this)
                        }
                    }

                } catch (e: Exception) {
                    "Unable to import image".toast(this)
                }

            } else "Unable to import image".toast(this)

        } else if (requestCode == CROP_REQUST && resultCode == Activity.RESULT_OK) {

            data?.getStringExtra(FINAL_IMAGE_PATH)?.let {
                //                if (!isEditMode) {
//                    isEditMode = true
//                    invalidateOptionsMenu()
//                }
                imageFile = File(it)
                profileIV.loadUrlWithPh(it, picasso)
                viewModel.uploadImage(imageFile!!)
            }

        }else if(requestCode==1001 && resultCode==Activity.RESULT_OK){
            phoneNoTV.setText("+"+preferenceStorage.userCCode+"-" +preferenceStorage.userPhone)
            setResult(HomeActivity.PROFILE_UPDATED)
        } else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun navigateToCrop(path: String, angle: Int) {
        val intent = Intent(this, CropImageActivity::class.java)
        intent.putExtra(IMAGE_PATH, path)
        intent.putExtra(ROTATED_ANGLE, angle)
        startActivityForResult(intent, CROP_REQUST)
    }

    private fun prefillFields() {
        profileNameET.setText(preferenceStorage.userName)
        emailIDET.setText(preferenceStorage.userEmail)
        phoneNoTV.setText("+"+preferenceStorage.userCCode+"-" + preferenceStorage.userPhone)
        dobTV.setText(preferenceStorage.dob?.convertToddMMyyyy())
        anniversaryTV.text = preferenceStorage.anniversary.convertToddMMyyyy()
        preferenceStorage.userAvatar.let {
            if (!it.isBlank()) {
                picasso.load(it).fit().centerCrop()
                    .placeholder(
                        ColorDrawable(
                            ContextCompat.getColor(
                                this,
                                R.color.textColorGreyAlpha10
                            )
                        )
                    )
                    .error(
                        ColorDrawable(
                            ContextCompat.getColor(
                                this,
                                R.color.textColorGreyAlpha10
                            )
                        )
                    )
                    .into(profileIV)
            }
        }

    }


    private fun observeVM() {
        viewModel.userHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    preferenceStorage.userName = it.data.second.userDetails.user_name
                    preferenceStorage.userEmail = it.data.second.userDetails.user_email
                    preferenceStorage.userAvatar = it.data.second.userDetails.user_avatar ?: ""
                    preferenceStorage.userPhone = it.data.second.userDetails.user_phone
                    preferenceStorage.dob = it.data.second.userDetails.dob ?: ""
                    preferenceStorage.anniversary = it.data.second.userDetails.anniversary ?: ""
                    loadingDialog.dismiss()
                    if (it.data.first) {
                        "Profile updated successfully".toast(this)
                        setResult(HomeActivity.PROFILE_UPDATED)
                        toggleToNonEditMode()
                        profileNameET.setText(it.data.second.userDetails.user_name)
                        emailIDET.setText(it.data.second.userDetails.user_email)
                        phoneNoTV.setText("+"+preferenceStorage.userCCode+"-" + it.data.second.userDetails.user_phone)
                        dobTV.text = it.data.second.userDetails.dob?.convertToddMMyyyy() ?: ""
                        anniversaryTV.text =
                            it.data.second.userDetails.anniversary?.convertToddMMyyyy() ?: ""
                        if (!it.data.second.userDetails.user_avatar.isNullOrBlank()) {
                            picasso.load(it.data.second.userDetails.user_avatar).fit().centerCrop()
                                .placeholder(
                                    ColorDrawable(
                                        ContextCompat.getColor(
                                            this,
                                            R.color.textColorGreyAlpha10
                                        )
                                    )
                                )
                                .into(profileIV)
                        } else profileIV.setImageResource(R.drawable.profile_dummy)

                    }


                }
                is ViewState.Error -> {
                    loadingDialog.dismiss()
                    onApiError(it.exception)
                }
                is ViewState.Loading -> {
                    loadingDialog.startLoading()
                }
            }
        })
    }

    private fun hideKeyboardInput() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
