package com.example.employees.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.employees.R
import com.example.employees.network.Employee

class EmployeeAdapter() :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    val employees = mutableListOf<Employee>()

    class EmployeeViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {

        val employeeNameListing: TextView

        init {
            employeeNameListing = view.findViewById(R.id.employee_name_listing)
        }

        fun navigateToEmployeeDetailFragment(id: Int) {
            val action = EmployeeListFragmentDirections
                .actionEmployeeListFragmentToEmployeeDetailFragment(id)
            view.findNavController().navigate(action)
        }

        fun navigateToDeleteEmployeeFragment(id: Int): Boolean {
            val action = EmployeeListFragmentDirections
                .actionEmployeeListFragmentToDeleteEmployeeDialogFragment(id)
            view.findNavController().navigate(action)
            return true
        }

    }

    override fun getItemCount(): Int = employees.size

    override fun onCreateViewHolder(
        viewGroup: ViewGroup, viewType: Int
    ): EmployeeAdapter.EmployeeViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.employee_list_item, viewGroup, false)

        return EmployeeViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: EmployeeViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val emp = employees[position]
        viewHolder.employeeNameListing.setText("${emp.last}, ${emp.first}")
        viewHolder.employeeNameListing.setOnClickListener {
            viewHolder.navigateToEmployeeDetailFragment(
                emp.id
            )
        }
        viewHolder.employeeNameListing.setOnLongClickListener {
            viewHolder.navigateToDeleteEmployeeFragment(emp.id)
        }
    }

}