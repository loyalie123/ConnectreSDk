package com.loyalie.connectre.custom_views

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.res.ResourcesCompat
import com.loyalie.connectre.R
import java.util.*
import java.util.List



class MultiSelectionSpinner : AppCompatSpinner, OnMultiChoiceClickListener {
    interface OnMultipleItemsSelectedListener {
        fun selectedIndices(indices: MutableList<Int>)
        fun selectedStrings(strings: MutableList<String>)
    }

    private var listener: OnMultipleItemsSelectedListener? = null
    var _items: Array<String>? = null
    var mSelection: BooleanArray? = null
    var mSelectionAtStart: BooleanArray? = null
    var _itemsAtStart: String? = null
    var simple_adapter: ArrayAdapter<String?>

    constructor(context: Context?) : super(context!!) {
        simple_adapter = ArrayAdapter(
            context!!,
            R.layout.custom_spinner
        )
        super.setAdapter(simple_adapter)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        simple_adapter = ArrayAdapter(
            context!!,
            R.layout.custom_spinner
        )
        super.setAdapter(simple_adapter)
    }

    fun setListener(listener: OnMultipleItemsSelectedListener?) {
        this.listener = listener
    }

    override fun performClick(): Boolean {
        val builder: AlertDialog.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                AlertDialog.Builder(context, R.style.AlertDialogTheme)
            } else {
                AlertDialog.Builder(context)
            }
        builder.setTitle("Choose a project")
        builder.setMultiChoiceItems(_items, mSelection, this)
        _itemsAtStart = getSelectedItemsAsString()
        val textView = TextView(context)
        textView.text = "Choose a project"
        textView.setPadding(20, 30, 20, 30)
        textView.textSize = 14f
        textView.setBackgroundColor(resources.getColor(R.color.camel))
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER
        builder.setCustomTitle(textView)

        val font = ResourcesCompat.getFont(context, R.font.sinkinsans_700bold)
        textView.setTypeface(font)
        builder.setMultiChoiceItems(_items, mSelection, this)
        builder.setPositiveButton("Submit",
            DialogInterface.OnClickListener { dialog, which ->

                System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection!!.size)
                listener!!.selectedIndices(getSelectedIndices())
                listener!!.selectedStrings(getSelectedStrings())
            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                simple_adapter.clear()
                simple_adapter.add(_itemsAtStart)
                System.arraycopy(
                    mSelectionAtStart,
                    0,
                    mSelection,
                    0,
                    mSelectionAtStart!!.size
                )
            })
        if (_items!!.size != 0)
            builder.show()
        return true
    }

    override fun onClick(
        dialog: DialogInterface,
        which: Int,
        isChecked: Boolean
    ) {
        if (mSelection != null && which < mSelection!!.size) {
            mSelection!![which] = isChecked
            simple_adapter.clear()
            simple_adapter.add(buildSelectedItemString())
        } else {
            throw IllegalArgumentException(
                "Argument 'which' is out of bounds."
            )
        }
    }


    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException(
            "setAdapter is not supported by MultiSelectSpinner."
        )
    }

    fun setItems(items: Array<String>?) {
        _items = items
        mSelection = BooleanArray(_items!!.size)
        mSelectionAtStart = BooleanArray(_items!!.size)
        simple_adapter.clear()
        simple_adapter.add(_items!![0])
        Arrays.fill(mSelection, false)
        mSelection!![0] = true
        mSelectionAtStart!![0] = true
    }

    fun setItems(items: ArrayList<String>) {
        _items = items.toTypedArray()
        mSelection = BooleanArray(_items!!.size)
        mSelectionAtStart = BooleanArray(_items!!.size)
        simple_adapter.clear()
        simple_adapter.add("Select Project Name")
        Arrays.fill(mSelection, false)

//
//        mSelection!![0] = true
    }

    fun setSelection(selection: Array<String>) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        for (cell in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == cell) {
                    mSelection!![j] = true
                    mSelectionAtStart!![j] = true
                }
            }
        }
        simple_adapter.clear()
        simple_adapter.add(buildSelectedItemString())
    }

    fun setSelectionColor() {

    }

    fun setSelection(selection: List<String>) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        for (sel in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == sel) {
                    mSelection!![j] = true
                    mSelectionAtStart!![j] = true
                }
            }
        }
        simple_adapter.clear()
        simple_adapter.add(buildSelectedItemString())
    }

    override fun setSelection(index: Int) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        if (index >= 0 && index < mSelection!!.size) {
            mSelection!![index] = true
            mSelectionAtStart!![index] = true
        } else {
            throw IllegalArgumentException(
                "Index " + index
                        + " is out of bounds."
            )
        }
        simple_adapter.clear()
        simple_adapter.add(buildSelectedItemString())
    }

    fun setSelection(selectedIndices: IntArray) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
            mSelectionAtStart!![i] = false
        }
        for (index in selectedIndices) {
            if (index >= 0 && index < mSelection!!.size) {
                mSelection!![index] = true
                mSelectionAtStart!![index] = true
            } else {
                throw IllegalArgumentException(
                    "Index " + index
                            + " is out of bounds."
                )
            }
        }
        simple_adapter.clear()
        simple_adapter.add(buildSelectedItemString())
    }


    fun getSelectedStrings(): MutableList<String> {
        val selection: MutableList<String> =
            LinkedList()
        for (i in 0 until _items!!.size) {
            if (mSelection!![i]) {
                selection.add(_items!![i])
            }
        }
        return selection
    }

    fun getSelectedIndices(): MutableList<Int> {
        val selection: MutableList<Int> = LinkedList()
        for (i in 0 until _items!!.size) {
            if (mSelection!![i]) {
                selection.add(i)
            }
        }
        return selection
    }

    private fun buildSelectedItemString(): String? {
        val sb = StringBuilder()
        var foundOne = false
        for (i in 0 until _items!!.size) {
            if (mSelection!![i]) {
                if (foundOne) {
                    sb.append(", ")
                }
                foundOne = true
                sb.append(_items!![i])
            }
        }
        return sb.toString()
    }

    fun getSelectedItemsAsString(): String? {
        val sb = java.lang.StringBuilder()
        var foundOne = false
        for (i in 0 until _items!!.size) {
            if (mSelection!![i]) {
                if (foundOne) {
                    sb.append(", ")
                }
                foundOne = true
                sb.append(_items!![i])
            }
        }
        return sb.toString()
    }
}