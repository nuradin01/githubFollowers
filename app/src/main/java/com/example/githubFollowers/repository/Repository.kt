package com.example.githubFollowers.repository

import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.githubFollowers.NetworkQueue
import com.example.githubFollowers.interfaces.UserDao
import com.example.githubFollowers.models.UserData
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject

class Repository @Inject constructor(private val networkQueue: NetworkQueue,
                                     private val dao: UserDao) {



    //Stream live data as Flow
    val allData : Flow<List<UserData>> = dao.getOrderedNetworkDataFlow()

    //To store webData received from the network
    val webData = MutableLiveData<List<UserData>>()
    //To store error code received from the network
    val errorCode = MutableLiveData<Int>()

    /**
     * This function insert network data to DB
     */
    fun insertData(data:UserData){
        dao.insert(data)
    }

    /**
     * This function deletes data from table
     */
    fun clearAll(){
        dao.clearAll()
    }


    /**
     * This suspend function perform a network request using volley to get data from the WEB_URL
     * Using a coroutine to fetch network data through the IO Thread
     * Get data as Json Object and post to live data
     */
    suspend fun performNetworkRequest(userName:String) = withContext(Dispatchers.IO){
        val request = StringRequest(
            Request.Method.GET,
            "https://api.github.com/users/$userName/following",
            { response ->
                try {
                    //Get data as Json Object
                    val jsonData =JSONArray(response.toString())
                    Timber.d("Data is :%s",jsonData)

                    val tempList = mutableListOf<UserData>()
                    for(each in 0 until jsonData.length()){
                        val filterData = jsonData.getJSONObject(each)
                        tempList.add(UserData(
                            id = filterData.getInt("id"),
                            login = filterData.getString("login"),
                            avatar_url = filterData.getString("avatar_url")
                        ))

                    }
                    webData.postValue(tempList)

                } catch (e: Exception) {
                    Timber.d("Error in response:%s",e.toString())
                }
            },
            { error ->
                errorCode.postValue(error.networkResponse?.statusCode)
                Timber.d("Fail to get response: ${error.networkResponse?.statusCode}")
            })
        networkQueue.addToRequestQueue(request)
    }
}