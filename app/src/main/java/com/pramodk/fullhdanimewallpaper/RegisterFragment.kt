package com.pramodk.fullhdanimewallpaper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup Nav Controller
        navController = Navigation.findNavController(view)

        //Check if User Already Loggedin
        if(firebaseAuth.currentUser == null){
            //Not Logged in, Create new account
            txt_view.text = "Creating New Account"
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if(it.isSuccessful){
                    //Account Created, go to home
                    txt_view.text = "Account Created, Logging in"
                    navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    //Error
                    txt_view.text = "Error : ${it.exception!!.message}"
                }
            }
        } else {
            //Logged in, send back to homepage
            navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }



}