package com.example.employees.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employees.network.Employee
import com.example.employees.network.EmployeeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmployeeListViewModel : ViewModel() {

    private val TAG = "EmployeeListViewModel"

    // internal data to store most recent request from the network
    private val _employees = MutableLiveData<List<Employee>>()

    // accessible/immutable variable for employees
    val employees: LiveData<List<Employee>> = _employees

    fun refreshEmployeeList() {
        getEmployees()
    }

    // function to connect to the server and obtain the employee list
    fun getEmployees() {
        viewModelScope.launch {
            try {
                var employees = EmployeeApi.retrofitService.getEmployees()
                _employees.postValue(employees)
                Log.d(TAG, "received employees from server")
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                _employees.postValue(listOf())
            }
        }
    }

    suspend fun deleteEmployee(id: Int): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "in deleteEmployee()")
        try {
            EmployeeApi.retrofitService.deleteEmployee(id)
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
            return@withContext false
        }
    }

}