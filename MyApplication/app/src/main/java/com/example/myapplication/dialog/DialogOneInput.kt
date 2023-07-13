package com.example.myapplication.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DialogOneInput : DialogFragment() {

    private var listener: DialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the hosting activity implements the listener interface
        if (context is DialogListener) listener = context
        else throw RuntimeException("$context must implement DialogListener")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_one_input, null)

        val textInputLayout = dialogView.findViewById<TextInputLayout>(R.id.DOI_til)

        if (tag == "AddOutlet") textInputLayout.hint = "Masukkan Nama Outlet"
        else textInputLayout.hint = "Masukkan Nama Anda"

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val inputedText = dialogView.findViewById<TextInputEditText>(R.id.DOI_til_tiet).text.toString()
                listener?.onDialogResult(inputedText)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }

    interface DialogListener {
        fun onDialogResult(value: String)
    }
}
