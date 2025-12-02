package net.mackk.metroview

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

object Database {

    // constants
    const val REF = "metroview"

    suspend fun toggleStation(username: String, combinedStationCode: String) {
        // get reference
        val ref = Firebase.database.getReference("$REF/${usernameToId(username)}/stations")

        // create set
        val stations = getSavedStations(username).toMutableSet()

        // add / remove
        if (stations.contains(combinedStationCode)) {
            stations.remove(combinedStationCode)
        } else {
            stations.add(combinedStationCode)
        }

        // set value on new entry
        ref.setValue(stations.toList()).addOnSuccessListener {
            Log.d("FirebaseLogging", "Entry added successfully")
        }.addOnFailureListener { error ->
            Log.e("FirebaseLogging", "Failed to add entry", error)
        }
    }

    suspend fun addStation(username: String, combinedStationCode: String) {
        // get reference
        val ref = Firebase.database.getReference("$REF/${usernameToId(username)}/stations")

        // create set
        val stations = getSavedStations(username).toMutableSet()
        stations.add(combinedStationCode)

        // set value on new entry
        ref.setValue(stations.toList()).addOnSuccessListener {
            Log.d("FirebaseLogging", "Entry added successfully")
        }.addOnFailureListener { error ->
            Log.e("FirebaseLogging", "Failed to add entry", error)
        }
    }

    suspend fun removeStation(username: String, combinedStationCode: String) {
        // get reference
        val ref = Firebase.database.getReference("$REF/${usernameToId(username)}/stations")

        // create set
        val stations = getSavedStations(username).toMutableSet()
        stations.remove(combinedStationCode)

        // set value on new entry
        ref.setValue(stations.toList()).addOnSuccessListener {
            Log.d("FirebaseLogging", "Entry removed successfully")
        }.addOnFailureListener { error ->
            Log.e("FirebaseLogging", "Failed to remove entry", error)
        }
    }

    suspend fun getSavedStations(username: String): List<String> {
        // get reference
        val ref = Firebase.database.getReference("$REF/${usernameToId(username)}/stations")

        // value to return
        val snapshot = ref.get().await()
        return snapshot.children.mapNotNull { it.getValue(String::class.java) }
    }

    private fun usernameToId(username: String): String {
        return username.replace("@", "_at_")
            .replace(".", "_dot_")
            .replace("#", "_hash_")
            .replace("$", "_dollar_")
            .replace("[", "_open_bracket_")
            .replace("]", "_close_bracket_")
            .replace("/", "_slash_")
    }

}
