package com.visk.android.stockmanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visk.android.stockmanager.db.User
import com.visk.android.stockmanager.repository.StockRepository
import com.visk.android.stockmanager.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel(){
    val allUser = repository.allUsers.asLiveData()

    fun insert(user : User) =viewModelScope.launch {
        repository.insertUser(user)
    }

    fun refresh()  {
        viewModelScope.launch {
        /*    repository.refresh().collect {


                val builder = java.lang.StringBuilder()
                it.getOrNull()?.forEach { builder.append(it.name +"\n") }

                Log.d("hjskwon", "${it.isFailure}"+it.getOrNull()?.size)
            }*/


        }
    }

    fun getStockInfo(stockId : String){
        viewModelScope.launch {
       //     val stock = StockRepository().getStockInfo(stockId)
        //    Log.d("hjskwon" ,"stock ${stock.currentPrice}")
        }
    }


    fun getStockInfoFlow(stockId : String){
        viewModelScope.launch {
            val stock = StockRepository().getStockInfoFlow(stockId).collect {
                Log.d("hjskwon" ,"stock ${it.currentPrice}")

            }
        }
    }


    class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

