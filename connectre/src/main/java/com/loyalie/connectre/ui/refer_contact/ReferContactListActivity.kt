package com.loyalie.connectre.ui.refer_contact

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loyalie.connectre.R
import com.loyalie.connectre.base.BaseActivity
import com.loyalie.connectre.custom_views.LoadingDialog
import com.loyalie.connectre.data.ViewState
import com.loyalie.connectre.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.mirrajabi.rxcontacts.Contact
import ir.mirrajabi.rxcontacts.RxContacts
import kotlinx.android.synthetic.main.activity_refer_contact_list.*
import kotlinx.android.synthetic.main.center_title_toolbar.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class ReferContactListActivity : BaseActivity(), View.OnClickListener,
    ReferContactAdapter.ContactSelectionListener,
    MultipleContactDialog.OnContactClickedInformer {
    private var disposable: Disposable? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ReferContactVM

    private val contactList = ArrayList<Contact>()
    private val contactListDuplicate = ArrayList<Contact>()
    private val CONTACTS_FLAG = 1000
    private var contactsAdapter: ReferContactAdapter? = null
    var numberContactMap: HashMap<ir.mirrajabi.rxcontacts.Contact, String> =
        HashMap<ir.mirrajabi.rxcontacts.Contact, String>()
    var selectedContacts: Int = 0
    private var isFirstLoad: Boolean = true
    lateinit var programId: String
    lateinit var programName: String
    private var commentDialog: CommentDialog? = null
    private val loadingDialog by lazy {
        LoadingDialog(this)
    }


    override fun onContactClicked(number: String, dialog: Dialog, position: Int) {
        dialog.dismiss();
        if (contactsAdapter != null) contactsAdapter!!.onNumberSelectedFromList(number, position);
    }

    override fun onContactSelected(
        selected: Boolean,
        referUserItem: Contact,
        selectedNumber: String
    ) {
        if (selected) {
            numberContactMap[referUserItem] = selectedNumber
            selectedContacts += 1
            if (selectedContacts == 10) {
                contactsAdapter!!.onMaximumLimitOfContacts(false)
                "10 contacts selected, press Done to continue".toast(this)
            }
        } else {
            if (numberContactMap.containsKey(referUserItem)) numberContactMap.remove(referUserItem)
            selectedContacts -= 1
            if (selectedContacts < 10) contactsAdapter!!.onMaximumLimitOfContacts(true)
        }

        if (selectedContacts > 0) {
            contactDoneTV.visibility = View.VISIBLE

            tcCheckbox.visibility = View.VISIBLE


        } else {
            contactDoneTV.visibility = View.INVISIBLE
            tcCheckbox.visibility = View.GONE
        }
    }

    override fun onContactSelectedAfterLimit() {
        "You can only refer 10 contacts at a time, press Done to refer".toast(this)
    }

    override fun onContactWithMultipleNumbersSelected(
        mySet: Set<String>,
        position: Int,
        presentSelection: String,
        name: String
    ) {
        val filteredArray = mySet.toTypedArray()
        if (filteredArray.size > 1) {
            if (TextUtils.isEmpty(presentSelection))
                MultipleContactDialog(
                    this, filteredArray, position, filteredArray[0], name,
                    this
                ).show()
            else
                MultipleContactDialog(
                    this,
                    filteredArray,
                    position,
                    presentSelection,
                    name,
                    this
                ).show()
        } else
            contactsAdapter!!.onNumberSelectedFromList(filteredArray[0], position)

    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backArrowIV -> {
                onBackPressed()
            }
            R.id.contactDoneTV -> {
                if (!tcCheckbox.isChecked)
                    "Please agree Terms & Conditions".toast(this)
                else
                    viewModel.checkIfAnyUsersAreNew(
                        numberContactMap,
                        preferenceStorage.userId,
                        programId
                    )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_contact_list)
        viewModel = viewModelProvider(viewModelFactory)
        programId = intent.getStringExtra(PROGRAM_ID)!!
        programName = intent.getStringExtra(PROGRAM_NAME)!!
        initToolbar()
        clickableSpan()
        initViews()

        fetchContacts()

        initClick()
        observeVM()

    }

    private fun initClick() {
        backArrowIV.setOnClickListener(this)
        contactDoneTV.setOnClickListener(this)
    }

    private fun fetchContacts() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                CONTACTS_FLAG
            )
        } else {
            getContact()
        }
    }

    private fun getContact() {
        if (isFirstLoad)
            progressBar.visibility = View.VISIBLE

        disposable = RxContacts.fetch(this)
            .filter { m -> m.inVisibleGroup == 1 && (!m.phoneNumbers.isEmpty()) }
            .toSortedList { obj, other -> obj.compareTo(other) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { contacts ->
                setConatct(contacts)
            }
    }

    private fun setConatct(contacts: List<Contact>) {
        runOnUiThread {
            contactList.clear()
            contactListDuplicate.clear()
            contactList.addAll(contacts)
            contactListDuplicate.addAll(contacts)
            contactsAdapter!!.clearCaches()
            contactsAdapter!!.notifyDataSetChanged()

            if (isFirstLoad) {
                progressBar.visibility = View.GONE

            } else {
                //swipeRefresh.setRefreshing(false)
            }


        }

    }

    fun clickableSpan() {


        var url = preferenceStorage.tandC
        var str = "I agree to the Terms & Conditions"
        val ss = SpannableString(str)
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ConnectReApp.themeColor
            }

            override fun onClick(p0: View) {

                openInBrowser(url)
//                Toast.makeText(this@RegisterActivity, "link clicked", Toast.LENGTH_SHORT).show()

            }
        }

        ss.setSpan(clickableSpan, 15, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



        ss.setSpan(UnderlineSpan(), 15, ss.length, 0);


        tcCheckbox.text = ss

        tcCheckbox.movementMethod = LinkMovementMethod.getInstance()
    }


    private fun initViews() {
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked), // unchecked
                intArrayOf(android.R.attr.state_checked)  // checked
            ),
            intArrayOf(Color.LTGRAY, ConnectReApp.themeColor)
        )
//        commentCB.setButtonTintList(colorStateList)
        tcCheckbox.buttonTintList = colorStateList
        contactRV.layoutManager = LinearLayoutManager(this)
        contactsAdapter = ReferContactAdapter(contactList, this)
        contactRV.setHasFixedSize(true)
        contactRV.setAdapter(contactsAdapter)

        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TextUtils.isEmpty(newText.toString())) {
                    contactList.clear()
                    contactList.addAll(contactListDuplicate)
                    contactsAdapter!!.notifyDataSetChanged()
                    emptyView.visibility = View.GONE
                } else {
                    val searchListContact = ArrayList<ir.mirrajabi.rxcontacts.Contact>()
                    if (contactList != null) {
                        for (referUserItem in contactListDuplicate) {
                            if (referUserItem.displayName != null && referUserItem.displayName.toLowerCase().contains(
                                    newText.toString().toLowerCase()
                                )
                            )
                                searchListContact.add(referUserItem)
                            else if (referUserItem.phoneNumbers != null && !referUserItem.phoneNumbers.isEmpty()) {
                                for (nums in referUserItem.phoneNumbers) {
                                    if (nums.toLowerCase().contains(newText.toString().toLowerCase()))
                                        searchListContact.add(referUserItem)
                                }
                            }
                        }
                        contactList.clear()
                        contactList.addAll(searchListContact)
                        contactsAdapter!!.notifyDataSetChanged()
                        if (searchListContact.isEmpty())
                            emptyView.visibility = View.VISIBLE
                        else
                            emptyView.visibility = View.GONE

                    }
                }
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_FLAG -> if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContact()
            }
        }
    }


    private fun initToolbar() {
        val extractedColor = ConnectReApp.themeColor
        referToolbar.setBackgroundColor(extractedColor)
        window.statusBarColor = Utils.darkenColor(extractedColor, 0.8f)
        titleTV.text = "Refer Now"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_final, R.anim.slideout_final)
    }

    override fun onDestroy() {
        if (disposable?.isDisposed ?: true) disposable?.dispose()
        super.onDestroy()
    }

    private fun observeVM() {
        viewModel.checkUserExistHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {
                    loadingDialog.dismiss()

                    if (it.data.first.size > 0) {
                        commentDialog =
                            CommentDialog(this, object : CommentDialog.CommentDialogInteractions {
                                override fun onCommentSubmit(comment: String) {
                                    viewModel.referContacts(comment)
                                }
                            }).apply {
                                show()
                            }

                    } else {
                        ReferSuccessDialog(
                            this,
                            programName, it.data.first, it.data.second, it.data.third,
                            object : ReferSuccessDialog.OnSuccessDismissListener {
                                override fun onDismiss() {
                                    finish()
                                }
                            }, false
                        )
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
        viewModel.referSuccessHolder.observe(this, Observer {
            when (it) {
                is ViewState.Success -> {

                    loadingDialog.dismiss()
                    commentDialog?.dismiss()
                    ReferSuccessDialog(
                        this,
                        programName, it.data.first, it.data.second, it.data.third,
                        object : ReferSuccessDialog.OnSuccessDismissListener {
                            override fun onDismiss() {
                                finish()
                            }
                        }
                    )


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

    companion object {
        const val PROGRAM_ID = "program_id"
        const val PROGRAM_NAME = "program_name"
        const val START_CODE = 21
        const val REFFERAL_SUCCESS = 22
        fun start(activity: Activity, programId: String, programName: String) {
            activity.startActivityForResult(
                Intent(
                    activity,
                    ReferContactListActivity::class.java
                ).apply {
                    putExtra(PROGRAM_ID, programId)
                    putExtra(PROGRAM_NAME, programName)
                }, START_CODE
            )
        }
    }
}
