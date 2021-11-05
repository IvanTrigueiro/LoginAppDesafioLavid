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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_login)

        setTextChangeListener(emailTextInputEditText, emailTextInputLayout)
        setTextChangeListener(passwordTextInputEditText, passwordTextInputLayout)

        loginProgressLayout.setOnTouchListener { v, event -> true }
    }

    fun setTextChangeListener(editText: EditText,textInputLayout: TextInputLayout ){
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

    fun onLogin(v: View){
        var proceed = true
        if(emailTextInputEditText.text.isNullOrEmpty()){ //Checa se o usuário digitou as informações corretas
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
            loginProgressLayout.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(emailTextInputEditText.text.toString(), passwordTextInputEditText.text.toString())
                .addOnCompleteListener { task ->
                    if(!task.isSuccessful){
                        loginProgressLayout.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Erro ao entrar, esse usuário não existe ou os dados estão incorretos.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun goToSignup(v: View){
        startActivity(SignupActivity.newIntent(this))
        //finish()
    }

    fun goToForgotPassword(v: View){
        startActivity(ForgotPasswordActivity.newIntent(this))
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    private var counter = 0
    override fun onBackPressed() {
        counter++
        if(counter === 2) {
            super.onBackPressed()
        }
        Toast.makeText(this@LoginActivity, "Pressione novamente para sair.", Toast.LENGTH_SHORT).show()
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}