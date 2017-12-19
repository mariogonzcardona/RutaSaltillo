package com.map.develop.rutasaltillov2.Kotlin

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.map.develop.rutasaltillov2.R
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var googleApiClient: GoogleApiClient? = null
    private var signInButton: SignInButton? = null
    val SIGN_IN_CODE = 777
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseAuthListener: FirebaseAuth.AuthStateListener? = null
    private var progressBar: ProgressBar? = null

    //Datos de usuario
    private var photoImageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var emailTextView: TextView? = null
    private val idTextView: TextView? = null

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Datos de usuario
        photoImageView = findViewById<View>(R.id.photoImageView) as ImageView
        nameTextView = findViewById<View>(R.id.nameTextView) as TextView
        emailTextView = findViewById<View>(R.id.emailTextView) as TextView
        //idTextView=(TextView)findViewById(R.id.idTextView);


        //Se inicializa opciones de entrada
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //Obtener un Token de firebase
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        //Aqui se Inicializa el cliente de Google
        googleApiClient = GoogleApiClient.Builder(this)
                //AutManage se gestion el cicl ode vida del google api client con el activity
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        //Darle una accion al boton signInButton
        signInButton = findViewById<View>(R.id.signInButton) as SignInButton
        signInButton!!.setSize(SignInButton.SIZE_WIDE)
        signInButton!!.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, SIGN_IN_CODE)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            //oyente de firebase
            val user = firebaseAuth.currentUser
            if (user != null) {
                //goMainActivity();
                hideUserLogin()
                setUserData(user)
                showUserData()
                runMain()
            } else {
                showUserLogin()
                hideUserData()
            }
        }
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar

    }
    private fun hideUserLogin() {
        signInButton!!.visibility = View.GONE
    }

    private fun setUserData(user: FirebaseUser?) {
        val params = "{email:" + user!!.email + ",nombre:" + user.displayName + ",uid:" + user.uid + "}"
        post().execute()
        nameTextView!!.text = user!!.displayName
        emailTextView!!.text = user.email
        //idTextView!!.text = user.uid
        Glide.with(this).load(user.photoUrl).into(photoImageView!!)
    }

    private fun showUserData() {
        nameTextView!!.visibility = View.VISIBLE
        emailTextView!!.visibility = View.VISIBLE
        photoImageView!!.visibility = View.VISIBLE
    }

    private fun showUserLogin() {
        signInButton!!.visibility = View.VISIBLE
    }

    private fun hideUserData() {
        nameTextView!!.visibility = View.GONE
        emailTextView!!.visibility = View.GONE
        photoImageView!!.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(firebaseAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (firebaseAuthListener != null) {
            firebaseAuth!!.removeAuthStateListener(firebaseAuthListener!!)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    //Este metodo comprueba si la operacion fue exitosa
    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            //Si es exitoso manda a llamar a MainActivity
            //goMainActivity();
            firebaseAuthWithGoogle(result.signInAccount)
        } else {
            Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(signInAccount: GoogleSignInAccount?) {
        //Muestra un progress bar para autenticacion en firebase y oculta el boton de google
        progressBar!!.visibility = View.VISIBLE
        signInButton!!.visibility = View.GONE

        val credential = GoogleAuthProvider.getCredential(signInAccount!!.idToken, null)
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            progressBar!!.visibility = View.GONE
            //signInButton!!.visibility = View.VISIBLE
            //aqui ponemos la llamada al server donde pasamos el UID, email, nombre del usuario.
            if (!task.isSuccessful) {
                Toast.makeText(applicationContext, "No se puede autenticar con Firebase", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Seccion de Botones Log Out y Revoke
    fun logOut(view: View) {
        firebaseAuth!!.signOut()
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { status ->
            if (status.isSuccess) {
                //goLoginActivity();
                showUserLogin()
                hideUserData()
            } else {
                Toast.makeText(applicationContext, "No se pudo cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun runMain() {
        Handler().postDelayed({
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }, 2000)
    }
    inner class post: AsyncTask<String,String,String>() {

        override fun doInBackground(vararg params: String?): String {
            val url = "https://busmia.herokuapp.com/login"
            val JSON = MediaType.parse("application/json; charset=utf-8")
            val client = OkHttpClient()
            val body = RequestBody.create(JSON, "")
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()
            val response = client.newCall(request).execute()
            return response.body()!!.string()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val path = getApplicationContext().getExternalFilesDir(null)
            val file = File(path, "jwt.token")
            val stream = FileOutputStream(file)
                stream.write(result!!.toByteArray())
        }
    }

}
