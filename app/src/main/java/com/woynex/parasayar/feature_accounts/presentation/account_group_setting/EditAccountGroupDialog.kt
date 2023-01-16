package com.woynex.parasayar.feature_accounts.presentation.account_group_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.woynex.parasayar.R
import com.woynex.parasayar.core.utils.showToastMessage
import com.woynex.parasayar.databinding.DialogAddEditAccountGroupBinding
import com.woynex.parasayar.feature_accounts.domain.model.AccountGroup

class EditAccountGroupDialog(
    private val titleText: String,
    private val isEditMode: Boolean,
    private val accountGroup: AccountGroup? = null,
    private val save: (AccountGroup) -> Unit,
    private val update: (AccountGroup) -> Unit
) : DialogFragment() {

    private lateinit var _binding: DialogAddEditAccountGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_edit_account_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogAddEditAccountGroupBinding.bind(view)

        _binding.apply {
            if (accountGroup != null && isEditMode){
                inputText.editText?.setText(accountGroup.name)
            }
            saveBtn.setOnClickListener {
                val text = inputText.editText?.text.toString()
                if (text.isNotBlank()) {
                    if (isEditMode) {
                        accountGroup?.let {
                            update(it.copy(name = text))
                        }
                    } else {
                        save(
                            AccountGroup(
                                name = text
                            )
                        )
                    }
                } else {
                    requireContext().showToastMessage(getString(R.string.name_cannot_be_empty))
                }
                this@EditAccountGroupDialog.dismiss()
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