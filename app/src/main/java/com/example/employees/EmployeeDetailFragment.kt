package com.example.employees

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.employees.databinding.EmployeeDetailFragmentBinding
import com.example.employees.network.Employee
import com.example.employees.network.Position

class EmployeeDetailFragment : Fragment() {

    private val TAG = "EmployeeDetailFragment"

    // set up the binding
    private var _binding: EmployeeDetailFragmentBinding? = null
    private val binding get() = _binding!!

    // get the view model for this fragment
    private val viewModel: EmployeeDetailViewModel by viewModels()

    private var emp_id: Int = -1

    companion object {
        const val EMPLOYEE_ID = "employee_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "in onCreate()")
        super.onCreate(savedInstanceState)
        arguments?.let {
            emp_id = it.getInt(EMPLOYEE_ID)
        }
        if (savedInstanceState == null) {
            viewModel.getEmployee(emp_id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "in onCreateView()")
        // set up the binding and return the view
        _binding = EmployeeDetailFragmentBinding.inflate(inflater)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "in onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        viewModel.employee.observe(viewLifecycleOwner) { employee -> populateEmployeeInfo(employee) }
    }

    override fun onDestroyView() {
        Log.d(TAG, "in onDestroyView()")
        super.onDestroyView()
    }

    private fun populateEmployeeInfo(employee: Employee) {
        // set up text info for employee
        binding.nameField.text = "${employee.first} ${employee.last}"
        binding.jobField.text = when (employee.pos) {
            Position.ENGINEER -> "Engineer"
            Position.INTERN -> "Intern"
        }
        binding.ageField.text = employee.age.toString()
        binding.salaryField.text = employee.salary.toString()
        binding.seniorField.text = when (employee.senior) {
            true -> "Yes"
            false -> "No"
        }
    }

}