package lab.mobile.frontend.data.repository

import android.graphics.Bitmap
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lab.mobile.frontend.data.remote.AuthApi
import lab.mobile.frontend.data.remote.PhotoUploadDto
import lab.mobile.frontend.domain.model.User
import java.io.ByteArrayOutputStream

class UserRepository(private val api: AuthApi) {
    suspend fun getUser(): User? {
        return try {
            api.getUser()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun uploadProfilePicture(bitmap: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            try {

                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                val byteArray = outputStream.toByteArray()
                val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

                api.updatePhoto(PhotoUploadDto(image = base64String))
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}