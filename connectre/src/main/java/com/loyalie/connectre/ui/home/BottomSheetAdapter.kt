package com.loyalie.connectre.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.ConnectRePref
import com.loyalie.connectre.ui.contact_us.ContactUsActivity
import com.loyalie.connectre.ui.dashboard.DashboardAct
import com.loyalie.connectre.ui.faq.FAQActivity
import com.loyalie.connectre.ui.feedback.FeedbackActivity
import com.loyalie.connectre.ui.rewards.NotiLeadsActivity
import com.loyalie.connectre.ui.tutorial.TutorialListingActivity
import com.loyalie.connectre.util.*
import kotlinx.android.synthetic.main.bottom_sheet_nav_item.view.*

class BottomSheetAdapter(
    val activity: AppCompatActivity,
    var vendor_id: String,
    val listner: onDissmissCallback
) :
    RecyclerView.Adapter<BottomSheetAdapter.DeveloperVH>() {

    private val navTitles = arrayOf(
        "Home", "Notifications", "Feedback", "App Tutorials",
        "Contact Us", "Privacy Policy","Referral T&C", "FAQ"/*, "Log Out"*/
    )
    private val navIcons = intArrayOf(
        R.drawable.ic_nav_home,
        R.drawable.ic_nav_notification,
        R.drawable.ic_feedback,
        R.drawable.ic_nav_app_tutorial,
        R.drawable.ic_nav_contact_us,
        R.drawable.ic_nav_privacy_policy,
        R.drawable.ic_nav_terms,
        R.drawable.ic_nav_faq/*,
        R.drawable.logout*/
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.bottom_sheet_nav_item, parent, false)
        return DeveloperVH(v)
    }

    override fun getItemCount(): Int {
        return navIcons.size
    }

    override fun onBindViewHolder(holder: DeveloperVH, position: Int) {
        holder.itemView.navItemTV.text = navTitles[position]
        holder.itemView.navItemIV.setImageResource(navIcons[position])
        vendor_id = ConnectRePref.getString(activity!!, "VendorID", "")!!
        holder.itemView.setSafeOnClickListener {
            when (navTitles[position]) {
                "Notifications" -> {
                    NotiLeadsActivity.start(activity, vendor_id)
                    activity.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
                }

                "App Tutorials" -> {
                    val intent = Intent(activity, TutorialListingActivity::class.java)
                    activity?.startActivity(intent)
                    activity.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
                }
                "Feedback" -> {
                    FeedbackActivity.start(activity, vendor_id)
                    activity.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
                }
                "FAQ" -> {
                    val intent = Intent(activity, FAQActivity::class.java)
                    activity?.startActivity(intent)
                    activity.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
                }

                "Privacy Policy" -> {
                    activity.openInBrowser(PRIVACY_POLICY_URL)
//                    val intent = Intent(activity, PrivacyPolicyActivity::class.java)
//                    activity?.startActivity(intent)
//                    activity.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
                }
               "Referral T&C"-> {
                   activity.openInBrowser(TERMS_SHAPOORJI_URL)
               }
                "Contact Us" -> {
                    val intent = Intent(activity, ContactUsActivity::class.java)
                    activity?.startActivity(intent)
                    activity.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
                }

                "Home" -> {
                    listner.onHomeCallback()
                }
                "Log Out" -> {
//                    LogoutDialog(activity, preferenceStorage).pop()
                    listner.onLogout()
                }
                "Dashboard" ->{
                    val intent = Intent(activity, DashboardAct::class.java)
                    activity?.startActivity(intent)
                    activity.overridePendingTransition(R.anim.enter_final, R.anim.exit_final)
                }
            }
        }
    }

    inner class DeveloperVH(v: View) : RecyclerView.ViewHolder(v) {

    }

    interface onDissmissCallback {
        fun onHomeCallback()
        fun onLogout()
    }
}