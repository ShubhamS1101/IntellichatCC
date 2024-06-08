package com.example.afinal

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.afinal.data.UserData
import com.example.afinal.data.usernode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth,
    var db: FirebaseFirestore
): ViewModel(){

    var inProcess = mutableStateOf(false)
    val eventMutablestate= mutableStateOf<Events<String>?>(null)
    var signIn= mutableStateOf(false)
    var userData= mutableStateOf<UserData?>(null)

    init {
        val currentuser=auth.currentUser
            signIn.value = currentuser!=null
            currentuser?.uid?.let{
                getUserData(it)
            }
    }



    fun SignUp(name:String, number: String, email: String, password: String){
        inProcess.value =true
        if(name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
            handlexception(customMessage = "Please Fill All Fields")
            return
        }
        inProcess.value = true
        db.collection(usernode).whereEqualTo("number", number).get().addOnSuccessListener {
            if(it.isEmpty){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{


                    if(it.isSuccessful){
                        signIn.value= true
                        createOrUpdateProfile(name, number)
                    }
                    else{
                        handlexception(it.exception, customMessage = "Sign Up failed!")
                    }
                }

            }
        }

    }

    fun createOrUpdateProfile(name: String?=null, number: String?=null, imageurl: String?=null){

        var uid=auth.currentUser?.uid
        val userData =UserData(
            userId = uid,
            name= name?: userData.value?.name,
            number= number?: userData.value?.number,
            imageurl= imageurl?: userData.value?.imageurl

        )

        uid?.let{
            inProcess.value=true

            db.collection(usernode).document(uid).get().addOnSuccessListener {
                if(it.exists()){
                    //update user data
                }
                else{
                    db.collection(usernode).document(uid).set(userData)
                    inProcess.value=false
                    getUserData(uid)

                }
            }
                .addOnFailureListener {
                    handlexception(it, "Cannot Retrieve User")
                }


        }



    }
    private fun getUserData(uid: String){
        inProcess.value= true
        db.collection(usernode).document(uid).addSnapshotListener{
                value, error->
            if(error!= null){
                handlexception(error, "Cannot Retreive User")
            }

            if(value!=null){
                var user= value.toObject<UserData>()
                userData.value=user
                inProcess.value=false
            }
        }

    }


    fun handlexception(exception: Exception?=null, customMessage: String=""){

        Log.e("FinalApp", "FinalAppException: ", exception )
        exception?.printStackTrace()
        val errorMsg= exception?.localizedMessage ?: ""
        val message =if(customMessage.isNullOrEmpty()) errorMsg else customMessage

        eventMutablestate.value=Events(message)
        inProcess.value=false

    }


}












