package com.example.githubFollowers.repository

import android.util.Log
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
import org.json.JSONObject
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkQueue: NetworkQueue,
    private val dao: UserDao
) {


    //Stream live data as Flow from database
    val readUsers: Flow<List<UserData>> = dao.getOrderedNetworkDataFlow()


    val followersData = MutableLiveData<List<UserData>>()
    val userData = MutableLiveData<UserData>()

    //To store error code received from the network
    val errorCode = MutableLiveData<Int>()
    val pageNum = MutableLiveData<Int>()

    /**
     * This function insert network data to DB
     */
    fun insertData(data: UserData) {
        dao.insert(data)
    }

    /**
     * This function deletes data from table
     */
    fun clearAll() {
        dao.clearAll()
    }


    /**
     * This suspend function perform a network request using volley to get data from the WEB_URL
     * Using a coroutine to fetch network data through the IO Thread
     * Get data as Json Object and post to live data
     */
    suspend fun getFollowers(userName: String, page: Int = 1) = withContext(Dispatchers.IO) {
        val request = StringRequest(
            Request.Method.GET,
            "https://api.github.com/users/$userName/followers?page=$page",
            { response ->
                try {
                    //Get data as Json Object
                    val jsonData = JSONArray(response.toString())

                    val tempList = mutableListOf<UserData>()
                    for (each in 0 until jsonData.length()) {
                        val filterData = jsonData.getJSONObject(each)
                        tempList.add(
                            UserData(
                                id = filterData.getInt("id"),
                                login = filterData.getString("login"),
                                avatar_url = filterData.getString("avatar_url")
                            )
                        )

                    }
                    if (page > 1) {
                        if (tempList.isEmpty()) {
                            Timber.d(followersData.value.toString())
                        } else {
                            val copyData = followersData.value
                            val combinedData = copyData!!.toList() + tempList
                            followersData.postValue(combinedData.toSet().toList())
                        }
                    } else {
                        followersData.postValue(tempList)
                        pageNum.postValue(page)
                    }
                    pageNum.postValue(page)

                } catch (e: Exception) {
                    Timber.d("Error in response:%s", e.toString())
                }
            },
            { error ->
//                errorCode.postValue(error.networkResponse?.statusCode)
                Timber.d("Fail to get response: ${error.networkResponse?.statusCode}")
            })
        networkQueue.addToRequestQueue(request)
    }

    suspend fun getUser(userName: String) = withContext(Dispatchers.IO) {
        val request = StringRequest(
            Request.Method.GET,
            "https://api.github.com/users/$userName",
            { response ->
                try {
                    //Get data as Json Object
                    val jsonData = JSONObject(response.toString())


                    val tempUser = UserData(
                        id = jsonData.getInt("id"),
                        login = jsonData.getString("login"),
                        avatar_url = jsonData.getString("avatar_url"),
                        public_gists = jsonData.getInt("public_gists"),
                        public_repos = jsonData.getInt("public_repos"),
                        following = jsonData.getInt("following"),
                        followers = jsonData.getInt("followers"),
                        created_at = jsonData.getString("created_at"),
                        bio = jsonData.getString("bio"),
                        location = jsonData.getString("location"),
                        name = jsonData.getString("name")
                    )
                    userData.postValue(tempUser)


                } catch (e: Exception) {
                    Timber.d("Error in response:%s", e.toString())
                }
            },
            { error ->
                errorCode.postValue(error.networkResponse?.statusCode)
                Timber.d("Fail to get response: ${error.networkResponse?.statusCode}")
            })
        networkQueue.addToRequestQueue(request)
    }
}