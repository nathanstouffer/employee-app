package com.example.employees.list

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.employees.R
import com.example.employees.databinding.EmployeeListFragmentBinding
import com.example.employees.edit.ItemClickHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EmployeeListFragment : Fragment(), ItemClickHandler {

    private val TAG = "EmployeeListFragment"

    // binding to the app screen to access properties
    private var _binding: EmployeeListFragmentBinding? = null
    private val binding get() = _binding!!

    // get the view model for this fragment
    private val viewModel: EmployeeListViewModel by viewModels()

    // recycler view
    private lateinit var recyclerView: RecyclerView

    // adapter for the employees
    private val adapter = EmployeeAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "in onCreate()")
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            viewModel.getEmployees()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "in onCreateView()")
        // set up the binding and return the view
        _binding = EmployeeListFragmentBinding.inflate(inflater)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "in onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        viewModel.employees.observe(viewLifecycleOwner) { employees ->
            adapter.employees.clear()
            adapter.employees.addAll(employees)
            adapter.notifyDataSetChanged()
        }
        recyclerView = binding.employeeRecyclerView
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }

    override fun onResume() {
        Log.d(TAG, "in onResume()")
        super.onResume()
        viewModel.refreshEmployeeList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "in onCreateOptionsMenu()")
        inflater.inflate(R.menu.employee_list_menu, menu)
    }

    override fun onDestroyView() {
        Log.d(TAG, "in onDestroyView()")
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "in onOptionsItemSelected()")
        val ret = when (item.itemId) {
            R.id.refresh_employee_list_option -> {
                // user chose to refresh the page
                viewModel.refreshEmployeeList()
                true
            }

            R.id.create_employee_option -> {
                // user chose to create a new employee
                val action = EmployeeListFragmentDirections
                    .actionEmployeeListFragmentToCreateEmployeeFragment()
                binding.root.findNavController().navigate(action)
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

    override fun onItemClick(id: Int) {
        val action = EmployeeListFragmentDirections
            .actionEmployeeListFragmentToEmployeeDetailFragment(id)
        binding.root.findNavController().navigate(action)
    }

    override fun onItemLongClick(emp_id: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Delete employee?")
            .setPositiveButton(R.string.Yes,
                DialogInterface.OnClickListener { dialog, id ->
                    deleteEmployee(emp_id)
                })
            .setNegativeButton(R.string.No,
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        builder.create()
        builder.show()
    }

    private fun deleteEmployee(emp_id: Int) = GlobalScope.launch(Dispatchers.Main) {
        val success = viewModel.deleteEmployee(emp_id)

        if (success) {
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            viewModel.refreshEmployeeList()
        } else {
            Toast.makeText(
                context,
                "Network error: could not delete employee $emp_id",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

}