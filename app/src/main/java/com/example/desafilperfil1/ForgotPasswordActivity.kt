package com.example.desafilperfil1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }

    fun resetPassword(v: View){
            val email: String = emailForgotET.text.toString().trim { it <= ' ' }
            if(email.isNullOrEmpty()){
                Toast.makeText(this@ForgotPasswordActivity, "Por favor digite o endereço de email", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this@ForgotPasswordActivity, "O email foi enviado com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@ForgotPasswordActivity, "Este endereço de email não está cadastrado", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, ForgotPasswordActivity::class.java)
    }

}