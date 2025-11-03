package net.mackk.metroview

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.database

object Database {

    // constants
    const val REF = "metroview"

    fun addItem() {
        // get reference
        val ref = Firebase.database.getReference(REF)

        // create new entry
        val newEntry = ref.push()

        // set value on new entry
        newEntry.setValue(null).addOnSuccessListener {
            Log.d("FirebaseLogging", "Entry added successfully")
        }.addOnFailureListener { error ->
            Log.e("FirebaseLogging", "Failed to add business", error)
        }
    }
}
