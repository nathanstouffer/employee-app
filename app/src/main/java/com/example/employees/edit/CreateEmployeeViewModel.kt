package com.example.employees.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.employees.network.Employee
import com.example.employees.network.EmployeeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateEmployeeViewModel : ViewModel() {

    private val TAG = "CreateEmployeeViewModel"

    suspend fun submitEmployee(new_emp: Employee): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "in submitEmployee()")
        try {
            EmployeeApi.retrofitService.postEmployee(new_emp)
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}")
            return@withContext false
        }
    }

    // old method that I used
    /*fun submitEmployee(new_emp: Employee): Boolean {
        Log.d(TAG, "in submitEmployee()")
        viewModelScope.launch {
            try {
                EmployeeApi.retrofitService.postEmployee(new_emp)
            }
            catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
        }
        return true
    }*/
}