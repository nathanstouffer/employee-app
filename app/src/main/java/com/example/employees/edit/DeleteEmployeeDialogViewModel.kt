package com.example.employees.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employees.network.EmployeeApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DeleteEmployeeDialogViewModel : ViewModel() {

    private val TAG = "DeleteEmployeeDialogViewModel"

    fun deleteEmployee(id: Int): Boolean {
        Log.d(TAG, "in deleteEmployee()")
        var success = false
        runBlocking {
            try {
                EmployeeApi.retrofitService.deleteEmployee(id)
                success = true
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
        }
        return success
    }

    // old method that I used
    /*fun deleteEmployee(id: Int) {
        val success = viewModelScope.launch {
            try {
                EmployeeApi.retrofitService.deleteEmployee(id)
                Log.d(TAG, "submitted delete request for employee $id")
            }
            catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
        }
    }*/

}