package com.example.employees.edit

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.employees.R

class DeleteEmployeeDialogFragment : DialogFragment() {

    // get the view model for this fragment
    private val viewModel: DeleteEmployeeDialogViewModel by viewModels()

    private var emp_id: Int = -1

    companion object {
        const val EMPLOYEE_ID = "employee_id"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            emp_id = it.getInt(DeleteEmployeeDialogFragment.EMPLOYEE_ID)
        }
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Delete employee?")
                .setPositiveButton(R.string.Yes,
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.deleteEmployee(emp_id)
                    })
                .setNegativeButton(R.string.No,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}