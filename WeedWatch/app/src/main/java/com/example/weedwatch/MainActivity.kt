package com.example.weedwatch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth and Firestore instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val nameEditText = findViewById<EditText>(R.id.etName)
        val phoneEditText = findViewById<EditText>(R.id.etPhone)

        val signUpButton = findViewById<Button>(R.id.btnSignUp)
        val loginRedirect = findViewById<Button>(R.id.btnGoToLogin)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()

            // Validate fields
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()) {
                // Register the user using Firebase Auth
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Save user data to Firestore
                            saveUserData(name, phone)

                            // Show success message and redirect to Login
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            // Show error message
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun saveUserData(name: String, phone: String) {
        // Get the current user's UID
        val userId = auth.currentUser?.uid ?: return

        // Create a user object
        val user = hashMapOf(
            "name" to name,
            "phone" to phone,
            "email" to auth.currentUser?.email
        )

        // Save user data to Firestore under the "users" collection
        db.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                // Data saved successfully
                Toast.makeText(this, "User data saved!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Error saving data
                Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
