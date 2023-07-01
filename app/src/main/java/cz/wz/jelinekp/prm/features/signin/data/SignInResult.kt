package cz.wz.jelinekp.prm.features.signin.data

data class SignInResult(
	val data: UserData?,
	val errorMessage: String?
)

data class UserData(
	val userId: String,
	val username: String?,
	val profilePictureUrl: String?
)
