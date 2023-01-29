package com.woynex.parasayar.feature_settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woynex.parasayar.core.utils.Constants
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    fun updateName(name: String) = viewModelScope.launch {
        val user = sharedPreferencesHelper.getUser()
        val firstName = name.split(" ")[0]
        val lastName = name.split(" ")[1]
        val db = Firebase.firestore
        db.collection(Constants.FIREBASE_FIRESTORE_USERS_COLLECTION)
            .document(user.id!!)
            .update("first_name", firstName)
            .addOnSuccessListener {
                sharedPreferencesHelper.updateName(firstName)
            }
        db.collection(Constants.FIREBASE_FIRESTORE_USERS_COLLECTION)
            .document(user.id)
            .update("last_name", lastName)
            .addOnSuccessListener {
                sharedPreferencesHelper.updateLastName(lastName)
            }
    }

    fun signOut() = viewModelScope.launch {
        Firebase.auth.signOut()
        sharedPreferencesHelper.signOut()
    }
}