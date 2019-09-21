package com.example.laujame.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity   {

    //variable estatica
    private static final int RC_SIGN_IN =9001;
    private static final String TAG = "";

    EditText mEmailView1, mPasswordView1;
    Button btn_ini1, tv_registrar1;
    SignInButton btnGoogle;
    //crear los objetos de conexion para firebase
    FirebaseAuth firebaseAuth;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final TextView tv_registrar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //inicializar la variable de conexion con firebase
        firebaseAuth= FirebaseAuth.getInstance();

        mEmailView1 = findViewById(R.id.mEmailView);
        mPasswordView1 = findViewById(R.id.mPasswordView);
        btn_ini1= findViewById(R.id.btn_ini);
        tv_registrar1=findViewById(R.id.tv_registrar);
        btnGoogle=findViewById(R.id.btnGoogle);




        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        tv_registrar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta(mEmailView1.getText().toString().trim(),mPasswordView1.getText().toString());
            }
        });


        btn_ini1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login(mEmailView1.getText().toString().trim(),mPasswordView1.getText().toString());
            }
        });

        //se tomo de la guia
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);





    }








    public void crearCuenta(String user, String password){
        //aqui colocamos las instrucciones para agregar un usuario
        //a la cuenta de la firebase
        //validar campos
        if(user.isEmpty()){
            Toast.makeText(getApplicationContext(),"Campo usuario obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(getApplicationContext(),"Campo contrasena obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(getApplicationContext(),"Debe de contener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }


        //agregar usuario
        firebaseAuth.createUserWithEmailAndPassword(user,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //validar si el usuario se registro con exito
                //en la plataforma de firebase
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "se registro correctamente", Toast.LENGTH_SHORT).show();
                } else{

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Ya existe un usario con esos datos", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Usuario creado correctamente  ", Toast.LENGTH_SHORT).show();
                    }
                }
            }



        });



    }


    public void Login(final String user, final String password){
        if(TextUtils.isEmpty(user)){
            mEmailView1.setError("Campo obligatorio");
            mEmailView1.setFocusable(true);
            return;
        }
        if(TextUtils.isEmpty(password)){
            mPasswordView1.setError("Campo obligatorio");
            mPasswordView1.setFocusable(true);
            return;
        }

        if(password.length()<8){
            mPasswordView1.setError("Al menos 8 caracteres");
            mPasswordView1.setFocusable(true);
            return;
        }

        //enviar los datos a la plataforma y validad que exista
        //al menos un usuario y password reistrado
        firebaseAuth.signInWithEmailAndPassword(user,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //ir a la segunda actividad (menu)
                    Intent intent = new Intent(
                            getApplicationContext(),MenuActivity.class);
                    intent.putExtra("myUser",mEmailView1.getText().toString().trim());
                    intent.putExtra("myPassword",mPasswordView1.getText().toString());
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"Usuario y/o contrasena incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent=new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);


                            Toast.makeText(getApplicationContext(),"Sesion iniciada con exito",Toast.LENGTH_SHORT).show();
                        } else {
                            //If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(getApplicationContext(),"Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                            //Intent intent=new Intent(MainActivity.this, menuActivity.class);
                            //startActivity(intent);
                        }

                        // ...
                    }
                });
    }









}
