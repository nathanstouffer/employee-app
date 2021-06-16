package com.example.employees

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employees.network.Employee
import com.example.employees.network.EmployeeApi
import kotlinx.coroutines.launch
import java.lang.Exception

class EmployeeDetailViewModel : ViewModel() {

    private var TAG = "EmployeeDetailViewModel"

    private var _employee = MutableLiveData<Employee>()

    val employee: LiveData<Employee> = _employee!!

    fun getEmployee(id: Int) {
        viewModelScope.launch {
            try {
                var emp = EmployeeApi.retrofitService.getEmployee(id)
                _employee.postValue(emp)
            }
            catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
        }
    }

}