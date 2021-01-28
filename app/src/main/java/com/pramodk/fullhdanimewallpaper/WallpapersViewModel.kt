package com.pramodk.fullhdanimewallpaper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class WallpapersViewModel:ViewModel(){
    private val firebaseRepository: FireBaseRepository = FireBaseRepository()

    private val wallpapersList: MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpapersList(): LiveData<List<WallpapersModel>> {
        return wallpapersList
    }

    fun loadWallpapersData() {
        //Query Data from Repo
        firebaseRepository.queryWallpapers().addOnCompleteListener {
            if(it.isSuccessful){
                val result = it.result
                if(result!!.isEmpty){
                    //No More Results To load, Reached at bottom of page
                } else {
                    //Results are ready to load
                    if(wallpapersList.value == null){
                        //Loading First Page
                        wallpapersList.value = result.toObjects(WallpapersModel::class.java)
                    } else {
                        //Loading Next Page
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpapersModel::class.java))
                    }

                    //Get the last Document
                    val lastItem: DocumentSnapshot = result.documents[result.size() - 1]
                    firebaseRepository.lastVisible = lastItem
                }
            } else {
                // Error
                Log.d("VIEW_MODEL_LOG", "Error : ${it.exception!!.message}")
            }
        }
    }
}