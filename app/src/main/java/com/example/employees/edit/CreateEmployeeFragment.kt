package com.example.employees.edit

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.employees.R
import com.example.employees.databinding.CreateEmployeeFragmentBinding
import com.example.employees.network.Employee
import com.example.employees.network.Position
import kotlinx.coroutines.*

class CreateEmployeeFragment : Fragment() {

    private val TAG = "CreateEmployeeFragment"

    // binding to the app screen to access properties
    private var _binding: CreateEmployeeFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateEmployeeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "in onCreate()")
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "in onCreateView()")
        // set up binding
        _binding = CreateEmployeeFragmentBinding.inflate(inflater)
        // return the view
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "in onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        binding.submitEmployeeButton.setOnClickListener { submitEmployee() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "in onCreateOptionsMenu()")
        inflater.inflate(R.menu.create_employee_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "in onOptionsItemSelected()")
        val ret = when (item.itemId) {
            R.id.cancel_new_employee_option -> {
                // user chose to cancel creating a new employee
                cancelEmployee()
                true
            }

            R.id.submit_employee_option -> {
                // user chose to submit a new employee
                submitEmployee()
                true
            }

            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }
        return ret
    }

    private fun submitEmployee() {
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        if (validInput()) {
            val new_emp: Employee = createEmployee()
            val success = scope.async { viewModel.submitEmployee(new_emp) }.await()
            if (success) {
                Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Network error: could not submit", Toast.LENGTH_SHORT)
                    .show()
            }
            val action =
                CreateEmployeeFragmentDirections.actionCreateEmployeeFragmentToEmployeeListFragment()
            binding.root.findNavController().navigate(action)
        }
    }

    private fun createEmployee(): Employee {
        // get text input fields
        val last: String = binding.lastNameInput.getText().toString()
        val first: String = binding.firstNameInput.getText().toString()
        val age: Int = binding.ageInput.getText().toString().toInt()
        val salary: Int = binding.salaryInput.getText().toString().toInt()
        // interpret radio button input
        val pos: Position = when (binding.jobRadioGroup.checkedRadioButtonId) {
            R.id.engineer_option -> Position.ENGINEER
            R.id.intern_option -> Position.INTERN
            else -> Position.INTERN
        }
        val senior: Boolean = when (binding.seniorRadioGroup.checkedRadioButtonId) {
            R.id.yes_option -> true
            R.id.no_option -> false
            else -> false
        }
        return Employee(-1, last, first, pos, age, salary, senior)
    }

    private fun validInput(): Boolean {
        val fillFields = Toast.makeText(context, "Fill out all fields", Toast.LENGTH_SHORT)
        val invalidInput =
            Toast.makeText(context, "Invalid setting: senior intern", Toast.LENGTH_SHORT)
        if (emptyField()) {
            // invalid setting detected, notify user and return false
            fillFields.show()
            return false
        }
        if (binding.jobRadioGroup.checkedRadioButtonId == R.id.intern_option
            && binding.seniorRadioGroup.checkedRadioButtonId == R.id.yes_option
        ) {
            // invalid setting detected, notify user and return false
            invalidInput.show()
            return false
        }
        // passed all checks, return true
        return true
    }

    private fun emptyField(): Boolean {
        // check to see if any text fields are empty
        when ("") {
            binding.firstNameInput.getText().toString() -> return true
            binding.lastNameInput.getText().toString() -> return true
            binding.ageInput.getText().toString() -> return true
            binding.salaryInput.getText().toString() -> return true
        }
        // check to see if a radio button is empty
        when (-1) {
            binding.jobRadioGroup.checkedRadioButtonId -> return true
            binding.seniorRadioGroup.checkedRadioButtonId -> return true
        }
        // passed all checks, return false
        return false
    }

    private fun cancelEmployee() {
        Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
        val action =
            CreateEmployeeFragmentDirections.actionCreateEmployeeFragmentToEmployeeListFragment()
        binding.root.findNavController().navigate(action)
    }

}
