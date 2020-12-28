package com.sworks.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sworks.api.ResponseWrapper
import com.sworks.ui.home.repository.HomeRepository

class HomeViewModel @ViewModelInject constructor(private val repository: HomeRepository) :
    ViewModel() {


    fun getClinetList() = liveData {
        emit(ResponseWrapper.loading())
        emit(repository.getClientList())
    }

}