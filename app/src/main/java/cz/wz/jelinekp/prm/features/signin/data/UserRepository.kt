package cz.wz.jelinekp.prm.features.signin.data

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import cz.wz.jelinekp.prm.features.signin.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val auth: FirebaseAuth,
    private val remoteConfig: FirebaseRemoteConfig,
    private val analytics: FirebaseAnalytics,
) {

    private val _userStream: MutableStateFlow<User?> = MutableStateFlow(null)
    val userStream = _userStream.map { user ->
        if (remoteConfig.getBoolean(HIDE_NAME_REMOTE_CONFIG_KEY)) {
            user?.copy(name = null)
        } else {
            user
        }
    }

    init {
        auth.addAuthStateListener { auth ->
            val user = auth.currentUser?.toUser()
            _userStream.value = user
            analytics.setUserId(user?.id)
        }
    }

    fun signIn(activity: Activity) {
        val scopes = listOf("email", "profile")
        val provider = OAuthProvider.newBuilder(GoogleAuthProvider.PROVIDER_ID, auth)
            .setScopes(scopes)
            .build()

        auth.startActivityForSignInWithProvider(activity, provider)
    }

    fun signOut() {
        auth.signOut()
    }

    private fun FirebaseUser.toUser(): User {
        // FirebaseUser can have multiple providers (like Google, Twitter, ...).
        // We are trying to find the first non-empty display name amongst them.
        val displayName = providerData.firstOrNull { !it.displayName.isNullOrBlank() }?.displayName
        return User(
            // uid is unique for a FirebaseUser. Even after you log out and log back in or if you reinstall the app.
            id = uid,
            name = displayName
        )
    }

    companion object {

        private const val HIDE_NAME_REMOTE_CONFIG_KEY = "hide_name"
    }
}