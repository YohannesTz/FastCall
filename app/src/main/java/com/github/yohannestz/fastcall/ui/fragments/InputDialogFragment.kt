package com.github.yohannestz.fastcall.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.github.yohannestz.fastcall.R

class InputDialogFragment: AppCompatDialogFragment() {
    private var listener: InputDialogFragmentListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.input_dialog_layout, null)

        val contactNameEditText: EditText = view.findViewById(R.id.contact_name_input)
        val phoneNumberEditText: EditText = view.findViewById(R.id.phone_number)

        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle("Add Contact")
            .setNegativeButton("Cancel") {_, _, ->}
            .setPositiveButton("Ok") {_, _, ->
                val contactName = contactNameEditText.text.toString()
                val phoneNumber = phoneNumberEditText.text.toString()
                listener!!.applyTexts(contactName, phoneNumber)
            }
            .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as InputDialogFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() +
                        "must implement ExampleDialogListener"
            )
        }
    }

    interface InputDialogFragmentListener {
        fun applyTexts(name: String, phoneNum: String)
    }
}