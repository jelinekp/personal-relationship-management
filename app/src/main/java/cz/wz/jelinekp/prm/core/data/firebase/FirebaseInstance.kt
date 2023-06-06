package cz.wz.jelinekp.prm.core.data.firebase

import com.google.firebase.database.FirebaseDatabase

class FirebaseInstance {

    fun getInstance() = FirebaseDatabase.getInstance()

}
