package com.example.employees.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employees.R
import com.example.employees.edit.ItemClickHandler
import com.example.employees.network.Employee

class EmployeeAdapter(private var itemClickHandler: ItemClickHandler) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    val employees = mutableListOf<Employee>()

    class EmployeeViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {

        val employeeNameListing: TextView

        init {
            employeeNameListing = view.findViewById(R.id.employee_name_listing)
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
            itemClickHandler.onItemClick(emp.id)
        }
        viewHolder.employeeNameListing.setOnLongClickListener {
            itemClickHandler.onItemLongClick(emp.id)
            return@setOnLongClickListener true
        }
    }

}