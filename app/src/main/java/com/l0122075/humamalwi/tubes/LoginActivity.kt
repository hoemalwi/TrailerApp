package com.l0122075.humamalwi.tubes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.l0122075.humamalwi.tubes.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var pref: preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        pref = preferences(this)

        binding.goRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val pass = binding.edtPasswordLogin.text.toString()

            if(email.isEmpty()){
                binding.edtEmailLogin.error = "Isi Email Terlebih Dahulu"
                binding.edtEmailLogin.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailLogin.error = "Email Tidak Valid"
                binding.edtEmailLogin.requestFocus()
                return@setOnClickListener
            }

            if(pass.isEmpty()){
                binding.edtPasswordLogin.error = "Isi Password Terlebih Dahulu"
                binding.edtPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            if(pass.length < 6){
                binding.edtPasswordLogin.error = "Password Minimal 6 Karakter"
                binding.edtPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            LoginFirebase(email, pass)
        }

    }

    private fun LoginFirebase(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    pref.prefStatus = true
                    if (email == "admin@gmail.com"){
//                        password = admin123
                        pref.prefEmail = email
                        Toast.makeText(this, "Selamat Datang Admin", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdminActivity::class.java)
                        startActivity(intent)
                    } else{
                        pref.prefEmail = email
                        Toast.makeText(this, "Selamat Datang $email", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, UserActivity::class.java)
                        startActivity(intent)
                    }
                    finish()

                } else{
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if(pref.prefStatus){
            var intent : Intent?
            if (pref.prefEmail == "admin@gmail.com"){
                intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else{
                intent =  Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
}