package com.example.smoviles.makecash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var imageV: ImageView

    var currentPath: String? = null
    val TAKE_PICTURE = 1
    val SELECT_PICTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageV = findViewById(R.id.Iv_Foto)


        btn_camera.setOnClickListener {
            dispatchCameraIntent()
        }

        btn_gallery.setOnClickListener {
            dispatchGalleryIntent()
        }

        btn_guardar.setOnClickListener {

            savePublicacion()

            var intent = Intent(this, FragmentosActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val file = File(currentPath)
                val uri = Uri.fromFile(file)
                imageV.setImageURI(uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                imageV.setImageURI(uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun dispatchGalleryIntent(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_PICTURE)
    }

    fun dispatchCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null){
            var photoFile: File? = null
            try{
                photoFile = createImage()
            }catch (e: IOException){
                e.printStackTrace()
            }
            if (photoFile != null){
                var photoUri = FileProvider.getUriForFile(this,
                    "com.example.smoviles.makecash.fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, TAKE_PICTURE)
            }
        }

    }

    fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageName = "JPEG "+timeStamp+" "
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        return image
    }

    private fun savePublicacion() {
        val storage = FirebaseStorage.getInstance()
        val photoRef = storage.reference.child("usuarios").child(et_title_anuncio.text.toString())

        imageV.Iv_Foto.isDrawingCacheEnabled = true
        imageV.Iv_Foto?.buildDrawingCache()
        val bitmap = (imageV.Iv_Foto?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = photoRef.putBytes(data)

        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation photoRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d("Url", downloadUri.toString())
                savedb(downloadUri.toString())
            } else {
            }
        }
    }

    private fun savedb(urlFoto: String) {
        val titulo = et_title_anuncio.text.toString()
        val descripcion = et_descripcion.text.toString()
        val precio = et_precio.text.toString()

        val publicacion = Publicacion(
            titulo,
            descripcion,
            precio,
            urlFoto
        )

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Publicaciones")

        myRef.child(titulo).setValue(publicacion)
    }
}

