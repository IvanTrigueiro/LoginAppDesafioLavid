package com.example.desafilperfil1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.desafilperfil1.Util.DATA_USERS
import com.example.desafilperfil1.Util.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            startActivity(HomeActivity.newIntent(this))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setTextChangeListener(usernameET, usernameTIL)
        setTextChangeListener(emailTextInputEditText, emailTextInputLayout)
        setTextChangeListener(passwordTextInputEditText, passwordTextInputLayout)

        signupProgressLayout.setOnTouchListener { v, event -> true }
    }

    fun setTextChangeListener(editText: EditText, textInputLayout: TextInputLayout){
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayout.isErrorEnabled = false
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    fun onSignup(v: View){
        var proceed = true
        if(usernameET.text.isNullOrEmpty()){
            usernameTIL.error = "Username é obrigatório"
            usernameTIL.isErrorEnabled = true
            proceed = false
        }
        if(emailTextInputEditText.text.isNullOrEmpty()){
            emailTextInputLayout.error = "Email é obrigatório"
            emailTextInputLayout.isErrorEnabled = true
            proceed = false
        }
        if(passwordTextInputEditText.text.isNullOrEmpty()){
            passwordTextInputLayout.error = "Senha é obrigatória"
            passwordTextInputLayout.isErrorEnabled = true
            proceed = false
        }
        if(proceed){
            signupProgressLayout.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(emailTextInputEditText.text.toString(), passwordTextInputEditText.text.toString())
                .addOnCompleteListener { task ->
                    if(!task.isSuccessful){
                        Toast.makeText(this@SignupActivity, "Signup error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    } else {
                        val email = emailTextInputEditText.text.toString()
                        val username = usernameET.text.toString()
                        val user = User(email, username, "", arrayListOf(), arrayListOf())
                        firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
                    }
                    signupProgressLayout.visibility = View.GONE
                }
        }
    }

    fun goToLogin(v: View){
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }
}