/*
package com.loyalie.selectContact

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.loyalie.R
import com.loyalie.base.LoyalieActivity
import com.loyalie.customviews.LoyalieTextView
import com.loyalie.helper.LoyalieModel
import com.loyalie.helper.LoyaliePref
import com.loyalie.ui.refer_contact.MultipleContactDialog.OnContactClickedInformer
import com.loyalie.ui.refer_contact.ReferContactAdapter.ContactSelectionListener
import com.loyalie.utils.Model
import com.loyalie.utils.Pref
import com.loyalie.utils.Utilities
import com.miguelcatalan.materialsearchview.MaterialSearchView

import java.util.ArrayList
import java.util.HashMap

import butterknife.BindView
import butterknife.ButterKnife

import android.content.Intent.getIntent
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import com.sun.corba.se.impl.util.Utility.clearCaches
import com.sun.deploy.ui.CacheUpdateProgressDialog.dismiss
import com.sun.deploy.ui.UIFactory.showProgressDialog
import org.omg.PortableServer.IdAssignmentPolicyValue.USER_ID

class SelectContactActivity : LoyalieActivity(), View.OnClickListener, ContactsAdapter.ContactSelectionListener,
    SelectContactView, MultiContactAdapter.OnContactClickedInformer {
    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null
    @BindView(R.id.toolbarText)
    internal var toolbarText: LoyalieTextView? = null
    @BindView(R.id.progressBar)
    internal var mProgressBar: ProgressBar? = null
    @BindView(R.id.contactsRV)
    internal var mContactsRecyclerView: RecyclerView? = null
    private val CONTACTS_FLAG = 1000
    @BindView(R.id.search_view)
    internal var searchView: MaterialSearchView? = null
    @BindView(R.id.doneButton)
    internal var doneButton: RelativeLayout? = null
    @BindView(R.id.selectedCountTV)
    internal var selectedCountTV: LoyalieTextView? = null
    @BindView(R.id.emptyView)
    internal var emptyView: LoyalieTextView? = null
    @BindView(R.id.swipeRefresh)
    internal var swipeRefresh: SwipeRefreshLayout? = null

    private val contactList = ArrayList<ir.mirrajabi.rxcontacts.Contact>()
    private val contactListDuplicate = ArrayList<ir.mirrajabi.rxcontacts.Contact>()
    private var contactsAdapter: ContactsAdapter? = null
    private var selectedContacts = 0
    private var userId: String? = null
    private val numberContactMap = HashMap<ir.mirrajabi.rxcontacts.Contact, String>()
    private var header: String? = null
    private var programID: String? = null
    private var referralId: String? = null
    private var programName: String? = null
    private var selectContactPrsnter: SelectContactPrsnter? = null

    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_contact)
        ButterKnife.bind(this)
        userId = getSharedPreferencesHelper().getString(Pref.USER_ID, "")
        header = getSharedPreferencesHelper().getString(Pref.TOKEN_HEADER, null)
        selectContactPrsnter = SelectContactPrsnter(this)
        initToolbar()
        initViews()
        getContacts(true)
        doneButton!!.setOnClickListener(this)
        if (getIntent() != null && getIntent().getIntExtra(LoyaliePref.CARD_ID, 0) != 0 &&
            getIntent().getIntExtra(LoyaliePref.REFERRAL_ID, 0) != 0
        ) {
            programID = getIntent().getIntExtra(LoyaliePref.CARD_ID, 0).toString()
            referralId = getIntent().getIntExtra(LoyaliePref.REFERRAL_ID, 0).toString()
            programName = getIntent().getStringExtra(LoyaliePref.REFERRAL_NAME)
        }

        searchView!!.setCursorDrawable(R.drawable.white_cursor)

        swipeRefresh!!.setOnRefreshListener {
            contactList.clear()
            contactListDuplicate.clear()
            contactsAdapter!!.notifyDataSetChanged()
            numberContactMap.clear()
            selectedContacts = 0
            selectedCountTV!!.setText("(0)")
            getContacts(false)
            searchView!!.closeSearch()
        }

    }

    private fun getContacts(isFirstLoad: Boolean) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), CONTACTS_FLAG)
        } else {
            if (isFirstLoad) mProgressBar!!.visibility = View.VISIBLE
            selectContactPrsnter!!.getContactsByRx(this, isFirstLoad)
        }
    }


    private fun initViews() {
        mContactsRecyclerView!!.layoutManager = LinearLayoutManager(this)
        contactsAdapter = ContactsAdapter(contactList, this)
        mContactsRecyclerView!!.setHasFixedSize(true)
        mContactsRecyclerView!!.adapter = contactsAdapter
        searchView!!.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    contactList.clear()
                    contactList.addAll(contactListDuplicate)
                    contactsAdapter!!.notifyDataSetChanged()
                    emptyView!!.setVisibility(View.GONE)
                } else {
                    val searchListContact = ArrayList<ir.mirrajabi.rxcontacts.Contact>()
                    if (contactList != null) {
                        for (referUserItem in contactListDuplicate) {
                            if (referUserItem.displayName != null && referUserItem.displayName.toLowerCase().contains(
                                    newText.toLowerCase()
                                )
                            )
                                searchListContact.add(referUserItem)
                            else if (referUserItem.phoneNumbers != null && !referUserItem.phoneNumbers.isEmpty()) {
                                for (nums in referUserItem.phoneNumbers) {
                                    if (nums.toLowerCase().contains(newText.toLowerCase()))
                                        searchListContact.add(referUserItem)
                                }
                            }
                        }
                        contactList.clear()
                        contactList.addAll(searchListContact)
                        contactsAdapter!!.notifyDataSetChanged()
                        if (searchListContact.isEmpty())
                            emptyView!!.setVisibility(View.VISIBLE)
                        else
                            emptyView!!.setVisibility(View.GONE)

                    }
                }
                return false

            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        getSupportActionBar().setDisplayShowTitleEnabled(false)
        getSupportActionBar().setDisplayShowHomeEnabled(true)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back)
        toolbarText!!.setText("CONTACT")
    }

    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.contact_search, menu)
        val item = menu.findItem(R.id.action_search)
        searchView!!.setMenuItem(item)
        return true
    }


    fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (contactList.size > 0) {
            menu.findItem(R.id.action_search).isVisible = true
        } else {
            menu.findItem(R.id.action_search).isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_FLAG -> if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts(true)
            }
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.doneButton) {
            if (!numberContactMap.isEmpty()) {
                if (LoyalieModel.isNetworkAvailable(this@SelectContactActivity)) {
                    selectContactPrsnter!!.checkIfAnyUsersAreNew(
                        numberContactMap, header,
                        userId, programID, referralId, this@SelectContactActivity
                    )
                } else
                    Model.showSnackbar(this@SelectContactActivity, getString(R.string.network_error))
            } else {
                Model.showSnackbar(this, "No contact selected...")
            }
        }
    }

    fun onContactSelected(selected: Boolean, referUserItem: ir.mirrajabi.rxcontacts.Contact, selectedNumber: String) {
        if (selected) {
            numberContactMap[referUserItem] = selectedNumber
            selectedContacts += 1
            if (selectedContacts == 10) {
                contactsAdapter!!.onMaximumLimitOfContacts(false)
                Utilities.showToast(this, "10 contacts selected, press Done to continue", false)
            }
        } else {
            if (numberContactMap.containsKey(referUserItem)) numberContactMap.remove(referUserItem)
            selectedContacts -= 1
            if (selectedContacts < 10) contactsAdapter!!.onMaximumLimitOfContacts(true)
        }
        selectedCountTV!!.setText("(" + selectedContacts.toString() + ")")
    }


    fun showLoading() {
        showProgressDialog("Please wait...")
    }

    fun hideLoading() {
        destroyDialog()
    }

    fun onReferralSuccess(userStatusItemList: List<ReferredUserStatusItem>) {
        showReferalSuccessPopUp(userStatusItemList)
    }

    fun onUnExpectedError() {
        Model.showSnackbar(this, getString(R.string.unexpected_error))
    }

    private fun showReferalSuccessPopUp(userStatusArray: List<ReferredUserStatusItem>) {

        val dialog = Dialog(this)
        dialog.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.setContentView(R.layout.refer_success_popup)
        val countTV = dialog.findViewById(R.id.countTV) as LoyalieTextView
        val programNameTV = dialog.findViewById<View>(R.id.programNameTV) as LoyalieTextView
        val addedLabelTV = dialog.findViewById(R.id.addedLabelTV) as LoyalieTextView
        if (userStatusArray.size == 1) addedLabelTV.setText("contact successfully referred")
        programNameTV.setText(programName)
        var successCount = 0
        for (i in userStatusArray.indices) {
            val userStatusItem = userStatusArray[i]
            if (userStatusItem.getStatus() === 1) {
                successCount++
            }
        }
        countTV.setText(successCount.toString() + " of " + userStatusArray.size.toString())
        val referSuccessRecycler = dialog.findViewById(R.id.referSuccessRecycler) as RecyclerView
        val referalSuccessAdapter = ReferalSuccessAdapter(userStatusArray)
        referSuccessRecycler.setHasFixedSize(true)
        referSuccessRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        referSuccessRecycler.adapter = referalSuccessAdapter
        dialog.show()
        dialog.setOnDismissListener {
            val resultIntent = Intent()
            resultIntent.putExtra(LoyaliePref.IS_REFERAL_SUCCESSFUL, true)
            setResult(LoyaliePref.REFER_FROM_CARD_RESULT, resultIntent)
            this@SelectContactActivity.finish()
        }
    }


    fun userExistCheckResult(
        newUserCount: Int,
        oldUserCount: Int,
        invalidUserCount: Int,
        referUserListItem: ReferalHolder
    ) {

        if (newUserCount > 0) {
            if (Model.isNetworkAvailable(this)) {
                CommentDialog(this, referUserListItem, object : CommentDialog.CommentDialogInteractions() {
                    fun onCommentSubmit(
                        referUserListItem: ReferalHolder, comment: String,
                        isSkipChecked: Boolean, commentDialog: CommentDialog
                    ) {
                        if (!isSkipChecked) {
                            if (TextUtils.isEmpty(comment)) {
                                hideKeyboard()
                                Model.showShortToast(this@SelectContactActivity, "Please enter your comment")
                                return
                            }
                        }
                        if (Model.isNetworkAvailable(this@SelectContactActivity)) {
                            selectContactPrsnter!!.referContacts(referUserListItem, header, comment)
                            commentDialog.dismiss()
                        } else
                            Model.showToast(this@SelectContactActivity, getString(R.string.network_error))
                        hideKeyboard()
                    }
                }).show()
            } else
                Model.networkDialog(this)
        } else if (newUserCount == 0 && oldUserCount > 0) {
            var userPrefixx = ""
            if (oldUserCount == 1)
                userPrefixx = "Selected user is"
            else
                userPrefixx = "All selected users are"
            Model.showSnackbar(this, "$userPrefixx already referred ")
        } else if (newUserCount == 0 && oldUserCount == 0 && invalidUserCount > 0) {
            Model.showSnackbar(this, "Selected contact has invalid phone number ")
        }
    }

    fun onContactWithMultipleNumbersSelected(
        mySet: Set<String>,
        position: Int,
        presentSelection: String,
        name: String
    ) {

        val filteredArray = mySet.toTypedArray()
        if (filteredArray.size > 1) {
            if (TextUtils.isEmpty(presentSelection))
                MultiContactDialog(this, filteredArray, position, filteredArray[0], name).show()
            else
                MultiContactDialog(this, filteredArray, position, presentSelection, name).show()

        } else
            contactsAdapter!!.onNumberSelectedFromList(filteredArray[0], position)
    }


    fun onContactClicked(number: String, dialog: Dialog, position: Int) {
        dialog.dismiss()
        if (contactsAdapter != null) contactsAdapter!!.onNumberSelectedFromList(number, position)
    }

    protected fun onDestroy() {
        selectContactPrsnter!!.disposeAll()
        super.onDestroy()
    }

    fun onContactsFetched(contacts: List<ir.mirrajabi.rxcontacts.Contact>, isFirstLoad: Boolean) {
        contactList.clear()
        contactListDuplicate.clear()
        contactList.addAll(contacts)
        contactListDuplicate.addAll(contacts)
        invalidateOptionsMenu()
        contactsAdapter!!.clearCaches()
        contactsAdapter!!.notifyDataSetChanged()
        if (isFirstLoad) {
            mProgressBar!!.visibility = View.GONE
        } else {
            swipeRefresh!!.isRefreshing = false
        }
    }

    fun onContactSelectedAfterLimit() {
        Model.showToast(this, "You can only refer 10 contacts at a time, press Done to refer")
    }

    fun onError(error: String) {
        Model.showToast(this, error)
    }
}
*/
