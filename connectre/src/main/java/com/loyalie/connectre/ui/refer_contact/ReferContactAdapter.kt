package com.loyalie.connectre.ui.refer_contact

import android.annotation.SuppressLint
import android.util.LongSparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.squareup.picasso.Picasso
import io.reactivex.annotations.Nullable
import ir.mirrajabi.rxcontacts.Contact
import kotlinx.android.synthetic.main.refer_contact_item.view.*
import java.util.*


class ReferContactAdapter(
    val contactsList: List<Contact>,
    val contactSelectionListener: ContactSelectionListener
) : RecyclerView.Adapter<ReferContactAdapter.ReferContactVH>() {

    private val selectedList = ArrayList<Contact>()
    private val idToNumber = LongSparseArray<String>()
    private var isSelectionAllowed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferContactVH {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.refer_contact_item, parent, false)
        return ReferContactVH(v)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: ReferContactVH, position: Int) {
        val contactItem = contactsList[position]
        holder.itemView.contactNameTV.setText(contactItem.getDisplayName())
        var num = ""
        if (idToNumber.get(contactItem.getId()) != null)
            num = idToNumber.get(contactItem.getId())
        else if (contactItem.phoneNumbers.size > 0)
            num = contactItem.phoneNumbers.iterator().next()
        holder.itemView.contactNumberTV.text = num
        if (contactItem.thumbnail != null)
            Picasso.get().load(contactItem.thumbnail).fit().centerCrop().into(holder.itemView.contactIV)
        else holder.itemView.contactIV.setImageResource(R.drawable.profile_dummy)

        holder.itemView.mReferRadioButton.isChecked = selectedList.contains(contactItem)
        /*val colorStateList = ColorStateList(
         arrayOf(
                intArrayOf(android.R.attr.state_enabled)
            ),
            intArrayOf(ConnectReApp.instance.getResources().getColor(R.color.lightestGreyq))
        )

        holder.itemView.mReferRadioButton.setSupportButtonTintList(colorStateList)*/
        holder.itemView.setOnClickListener {
            val contact = contactsList[position]
            if (isSelectionAllowed || holder.itemView.mReferRadioButton.isChecked) {
                if (holder.itemView.mReferRadioButton.isChecked) {
                    selectedList.remove(contact)
                    if (idToNumber.get(contact.getId()) != null) idToNumber.remove(contact.getId())
                    contactSelectionListener.onContactSelected(false, contactsList[position], "")
                    notifyItemChanged(position)
                } else {
                    if (contact.getPhoneNumbers().size > 1) {
                        var presentSelection = ""
                        if (idToNumber.get(contact.getId()) != null)
                            presentSelection = idToNumber.get(contact.getId())
                        contactSelectionListener.onContactWithMultipleNumbersSelected(
                            contact.getPhoneNumbers(),
                            position, presentSelection, contact.getDisplayName()
                        )
                    } else {
                        selectedList.add(contact)
                        var number = ""
                        if (contact.getPhoneNumbers().size > 0) number =
                            contact.getPhoneNumbers().iterator().next()
                        idToNumber.put(contact.getId(), number)
                        contactSelectionListener.onContactSelected(
                            true,
                            contactsList.get(position),
                            number
                        )
                        notifyItemChanged(position)
                    }
                }
            } else
                contactSelectionListener.onContactSelectedAfterLimit()
        }
    }

    inner class ReferContactVH(view: View) : RecyclerView.ViewHolder(view) {

    }

    internal fun clearCaches() {
        selectedList.clear()
        idToNumber.clear()
        isSelectionAllowed = true
    }


    interface ContactSelectionListener {
        fun onContactSelected(selected: Boolean, referUserItem: Contact, selectedNumber: String)

        fun onContactSelectedAfterLimit()

        fun onContactWithMultipleNumbersSelected(
            mySet: Set<String>, position: Int,
            @Nullable presentSelection: String, @Nullable name: String
        )
    }

    fun onMaximumLimitOfContacts(enable: Boolean) {
        isSelectionAllowed = enable
    }


    fun onNumberSelectedFromList(number: String, position: Int) {
        selectedList.add(contactsList[position])
        idToNumber.put(contactsList[position].id, number)
        contactSelectionListener.onContactSelected(true, contactsList[position], number)
        notifyItemChanged(position)
    }

}