package com.example.smoviles.makecash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    val RC_SIGN_IN: Int = 1
    lateinit var signOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val signIn = findViewById<View>(R.id.btn_google) as SignInButton
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signOut = findViewById<View>(R.id.btn_login) as Button
        signOut.visibility = View.VISIBLE
        signIn.setOnClickListener{view: View? -> signInGoogle()
        }

        FirebaseAuth.getInstance().signOut()

        btn_login.setOnClickListener {
            var intent = Intent(this,FragmentosActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


/*
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }*/

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

       // var intent = Intent(this, MainActivity::class.java)
       // startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
            Toast.makeText(this, "FirebaseAuthWithGoogle", Toast.LENGTH_SHORT).show()
        }catch (e: ApiException) {
            updateUI (null)
            Toast.makeText(this, "UpdateUI  handle", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI (account: FirebaseUser?) {
        if (account != null) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Bienvenido a Makecash", Toast.LENGTH_SHORT).show()
        }else if (account == null) {
            Toast.makeText(this, "Falló el inicio de sesión", Toast.LENGTH_SHORT).show()
        }
        /*val dispTxt = findViewById<View>(R.id.tv_app) as TextView
        tv_app.text = account?.displayName
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener {
            view: View? -> mGoogleSignInClient.signOut().addOnCompleteListener {
            task: Task<Void> -> dispTxt.text = " "
            signOut.visibility = View.INVISIBLE
            }
        }*/
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                }else {
                    updateUI(null)
                }

            }
    }
}





