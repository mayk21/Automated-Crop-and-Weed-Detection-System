package com.example.weedwatch

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.UserProfileChangeRequest

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)

        val btnSave = findViewById<Button>(R.id.btnSave)

        // Populate EditTexts with current user data
        auth.currentUser?.let { user ->
            // Fetch name and email from Firestore
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    // Set the values from Firestore to EditTexts
                    etName.setText(document.getString("name"))
                    etEmail.setText(user.email)  // Email is always available from FirebaseAuth
                    etPhone.setText(document.getString("phone"))
                }
        }


        // Save changes when "Save Changes" button is clicked
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                // Update Firebase Authentication (for name and email)
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Update Firestore data
                            updateFirestoreData(name, email, phone)
                        } else {
                            Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFirestoreData(name: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid ?: return

        val userUpdates = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone
        )

        db.collection("users").document(userId).set(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(this, "User data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error updating user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
