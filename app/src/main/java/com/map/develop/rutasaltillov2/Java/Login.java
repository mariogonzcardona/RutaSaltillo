package com.map.develop.rutasaltillov2.Java;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.map.develop.rutasaltillov2.Kotlin.LoginActivity;
import com.map.develop.rutasaltillov2.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.sql.DriverManager.println;


/**
 * Created by Mario on 08/12/2017.
 */

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE=777;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;

    //Datos de usuario
    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;

    private TextView welcomeLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Datos de usuario
        photoImageView=(ImageView) findViewById(R.id.photoImageView);
        nameTextView=(TextView) findViewById(R.id.nameTextView);
        emailTextView=(TextView)findViewById(R.id.emailTextView);
        //idTextView=(TextView)findViewById(R.id.idTextView);

        //Se inicializa opciones de entrada
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //Obtener un Token de firebase
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Aqui se Inicializa el cliente de Google
        googleApiClient=new GoogleApiClient.Builder(this)
                //AutManage se gestion el cicl ode vida del google api client con el activity
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        //Darle una accion al boton signInButton
        signInButton=(SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //oyente de firebase
                FirebaseUser user =firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    //goMainActivity();
                    hideUserLogin();
                    setUserData(user);
                    Toast.makeText(getApplicationContext(),"No se pudo iniciar sesión",Toast.LENGTH_LONG).show();
                    showUserData();
                }
                else
                {

                    showUserLogin();
                    hideUserData();
                }
            }
        };
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        ActionBar actionBar = getActionBar();
        actionBar.show();
    }

    private void hideUserLogin() {
        signInButton.setVisibility(View.GONE);
    }

    private void setUserData(FirebaseUser user) {
        Log.i("informacion","adios");

        String params = "{email:"+user.getEmail()+",nombre:"+user.getDisplayName()+",uid:"+user.getUid()+"}";
        try {
            String text = post("https://busmia.herokuapp.com/login",params);
            File path = getApplicationContext().getExternalFilesDir(null);
            File file = new File(path, "jwt.token");
            FileOutputStream stream = new FileOutputStream(file);
            Log.i("informacion","adios");
            try {
                stream.write(text.getBytes());
            } finally {
                stream.close();
            }                } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        nameTextView.setText("PENDEJO");
        emailTextView.setText(user.getEmail());
        idTextView.setText(user.getUid());
        Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
        //Toast.makeText(this,user.getUid(), Toast.LENGTH_SHORT).show();
    }

    private void showUserData() {
        nameTextView.setVisibility(View.VISIBLE);
        emailTextView.setVisibility(View.VISIBLE);
        photoImageView.setVisibility(View.VISIBLE);
    }

    private void showUserLogin() {
        signInButton.setVisibility(View.VISIBLE);
    }

    private void hideUserData() {
        nameTextView.setVisibility(View.GONE);
        emailTextView.setVisibility(View.GONE);
        photoImageView.setVisibility(View.GONE);
    }

    @Override
    protected void onStart(){
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener!=null)
        {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // se puede mostrar un mensaje cuando algo sale mal en la conexion
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN_CODE)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //Este metodo comprueba si la operacion fue exitosa
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess())
        {
            //Si es exitoso manda a llamar a MainActivity
            //goMainActivity();
            firebaseAuthWithGoogle(result.getSignInAccount());
        }else
        {
            Toast.makeText(this,"No se pudo iniciar sesión",Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount signInAccount) {
        //Muestra un progress bar para autenticacion en firebase y oculta el boton de google
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();

        signInButton.setVisibility(View.GONE);

        AuthCredential credential= GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                //aqui ponemos la llamada al server donde pasamos el UID, email, nombre del usuario.
                if(!task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"No se puede autenticar con Firebase",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    String post(String url, String json) throws IOException {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Context context = getApplicationContext();
        CharSequence texto = response.body().string();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, texto, duration);
        toast.show();

        return response.body().string();
    }
    //Seccion de Botones Log Out y Revoke
    public void logOut(View view)
    {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess())
                {
                    //goLoginActivity();
                    showUserLogin();
                    hideUserData();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No se pudo cerrar sesión",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    public void runMain()
    {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent =new Intent(Main.this,MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        },3000);
    }
    */

/*
    private void goMainActivity() {
        //que nunca se quede una atras de la otra
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Intent intent2 = new Intent(getApplication(), LoginActivity.class);
        getApplication().startActivity(intent2);
    }

*/

}
