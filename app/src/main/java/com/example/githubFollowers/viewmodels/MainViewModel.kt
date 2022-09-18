package com.example.githubFollowers.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubFollowers.models.UserData
import com.example.githubFollowers.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //Get live network data records from room DB as Flow
    val readUsers: LiveData<List<UserData>> = repository.readUsers.asLiveData()

    //Get data from web as live data and expose to view for observing
    val followersData: LiveData<List<UserData>> = repository.followersData
    val userData: LiveData<UserData> = repository.userData
    val errorCode: LiveData<Int> = repository.errorCode
    val pageNum: LiveData<Int> = repository.pageNum

    /**
     * Launch network request to fetch data
     */
    fun getFollowers(userName: String, page: Int = 1) = viewModelScope.launch {
        repository.getFollowers(userName, page)
    }

    //
    fun getUser(userName: String) = viewModelScope.launch {
        repository.getUser(userName)
    }


    /**
     * Insert data into room DB
     */
    fun insertData(data: UserData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(data)
    }

    /**
     * Delete data from room DB
     */
    fun deleteData() = viewModelScope.launch(Dispatchers.IO) {
        repository.clearAll()
    }
}