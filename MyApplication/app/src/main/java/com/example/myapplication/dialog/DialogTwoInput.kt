package com.example.myapplication.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.database.Goods
import com.google.android.material.textfield.TextInputEditText

class DialogTwoInput() : DialogFragment() {

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
        val dialogView = inflater.inflate(R.layout.dialog_two_input, null)
        val inputedNamaBarangTiet = dialogView.findViewById<TextInputEditText>(R.id.DTI_til_tiet_product_name)
        val inputedHargaBarangTiet = dialogView.findViewById<TextInputEditText>(R.id.DTI_til_tiet_product_price)

        val args = arguments
        val goodsData = args?.getParcelable<Goods>("goodsData") // Get the Goods object from the Bundle
        // populate if isEdit
        if (goodsData != null) {
            inputedNamaBarangTiet.text = Editable.Factory.getInstance().newEditable(goodsData.goodsName)
            inputedHargaBarangTiet.text = Editable.Factory.getInstance().newEditable(goodsData.goodsPrice.toString())
        }

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val inputedNamaBarang = inputedNamaBarangTiet.text.toString()
                val inputedHargaBarang = inputedHargaBarangTiet.text.toString().toDouble()

                if (goodsData == null) listener?.onDialogResult(inputedNamaBarang, inputedHargaBarang)
                else {
                    goodsData.goodsName = inputedNamaBarang
                    goodsData.goodsPrice = inputedHargaBarang
                    listener?.onDialogEditResult(goodsData)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }

    interface DialogListener {
        fun onDialogResult(namaProduk: String, hargaProduk: Double)
        fun onDialogEditResult(goods: Goods)
    }
}