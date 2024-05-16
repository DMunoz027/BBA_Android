import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class FirebaseStorageHelper {

    private val storageRef = FirebaseStorage.getInstance().reference

    fun uploadUriImage(
        imageUri: Uri,
        folder: String,
        path: String,
        result: (String?) -> Unit
    ) {
        val fileRef = storageRef
            .child(folder)
            .child(path)

        val uploadTask = fileRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                result(imageUrl)
            }.addOnFailureListener {
                result(null)
            }
        }.addOnFailureListener { exception ->
            result(exception.message)
        }
    }

    fun uploadBitmapImage(
        imageBitmap: Bitmap,
        folder: String,
        path: String,
        result: (String?) -> Unit
    ) {
        val fileRef = storageRef
            .child(folder)
            .child(path)

        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = fileRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                result(imageUrl)
            }.addOnFailureListener {
                result(null)
            }
        }.addOnFailureListener { exception ->
            result(exception.message)
        }
    }
}
