package com.woynex.parasayar.core.utils.custom_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.showToastMessage
import com.woynex.parasayar.databinding.DialogCustomEditBinding

class CustomEditDialog(
    private val titleText: String,
    private val isEditMode: Boolean,
    private val editText: String? = null,
    private val save: (String) -> Unit,
    private val update: (String) -> Unit
) : DialogFragment() {

    private lateinit var _binding: DialogCustomEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_custom_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogCustomEditBinding.bind(view)

        _binding.apply {
            if (editText != null && isEditMode) {
                inputText.editText?.setText(editText)
            }
            saveBtn.setOnClickListener {
                val text = inputText.editText?.text.toString()
                if (text.isNotBlank()) {
                    if (isEditMode) {
                        update(text)

                    } else {
                        save(text)
                    }
                    this@CustomEditDialog.dismiss()
                }
            }
            title.text = titleText
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }
}