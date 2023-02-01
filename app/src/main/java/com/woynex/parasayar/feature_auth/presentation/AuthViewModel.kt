package com.woynex.parasayar.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woynex.parasayar.core.data.repository.CurrencyRepository
import com.woynex.parasayar.core.domain.model.Currency
import com.woynex.parasayar.core.domain.model.User
import com.woynex.parasayar.core.utils.Constants.FIREBASE_FIRESTORE_USERS_COLLECTION
import com.woynex.parasayar.core.utils.Resource
import com.woynex.parasayar.core.utils.SharedPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val repo: CurrencyRepository
) : ViewModel() {

    private val _isAuth = MutableStateFlow(false)
    val isAuth = _isAuth.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _signUpResponse = MutableStateFlow<Resource<User>>(Resource.Empty())
    val signUpResponse = _signUpResponse.asStateFlow()

    private val _signInResponse = MutableStateFlow<Resource<User>>(Resource.Empty())
    val signInResponse = _signInResponse.asStateFlow()

    init {
        _isAuth.value = Firebase.auth.currentUser != null
        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
        }
    }

    fun updateCurrentUser(user: User) = viewModelScope.launch {
        sharedPreferencesHelper.setUser(user)
    }

    fun signUpWithEmail(
        user: User,
        password: String,
        selectedCurrency: Currency
    ) = viewModelScope.launch {
        _signUpResponse.value = Resource.Loading<User>()
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(user.email!!, password)
            .addOnSuccessListener {
                viewModelScope.launch {
                    repo.insertCurrency(currency = selectedCurrency)
                }
                it.user?.let { newUser ->
                    createUser(
                        user.copy(id = newUser.uid)
                    )
                }
            }
            .addOnFailureListener {
                _signUpResponse.value = Resource.Error<User>(it.localizedMessage ?: "Error")
            }
    }

    fun signInWithEmail(
        email: String, password: String,
    ) = viewModelScope.launch {
        _signInResponse.value = Resource.Loading<User>()
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                it.user?.let { user ->
                    getUser(user.uid)
                }
            }
            .addOnFailureListener {
                _signInResponse.value = Resource.Error<User>(it.localizedMessage ?: "Error")
            }
    }

    private fun getUser(id: String) {
        _signInResponse.value = Resource.Loading<User>()
        val db = Firebase.firestore
        db.collection(FIREBASE_FIRESTORE_USERS_COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                user?.let {
                    updateCurrentUser(it)
                    _signInResponse.value = Resource.Success<User>(it)
                }
            }
            .addOnFailureListener {
                _signInResponse.value = Resource.Error<User>(it.localizedMessage ?: "Error")
            }
    }

    private fun createUser(
        user: User
    ) {
        _signUpResponse.value = Resource.Loading<User>()
        val db = Firebase.firestore
        db.collection(FIREBASE_FIRESTORE_USERS_COLLECTION)
            .document(user.id!!)
            .set(user)
            .addOnSuccessListener {
                updateCurrentUser(user)
                _signUpResponse.value = Resource.Success<User>(user)
            }
            .addOnFailureListener { e ->
                _signUpResponse.value = Resource.Error<User>(e.localizedMessage ?: "Error")
            }
    }

    fun forgotPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    fun clear() = viewModelScope.launch {
        _signInResponse.value = Resource.Empty<User>()
    }
}