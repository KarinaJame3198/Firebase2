package com.example.laujame.firebase2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import modelodedatos.Upload;

public class StorangeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST=1;

    Button btnUpload,btnChooseFile,btnShowImages;
    ImageView imageView;
    EditText txtFileName;
    ProgressBar progressBar;



    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;


    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storange);

        btnChooseFile=findViewById(R.id.btnChooseFile);
        btnUpload=findViewById(R.id.btnUpload);
        imageView=findViewById(R.id.imgView);
        txtFileName=findViewById(R.id.txtFileName);
        progressBar=findViewById(R.id.progressBar);
        btnShowImages=findViewById(R.id.btnShowImages);

        btnChooseFile.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnShowImages.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("uploads");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.btnChooseFile:
                seleccionarImagen();
                break;

            case R.id.btnUpload:
                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(), getString(R.string.msgInProgress), Toast.LENGTH_SHORT).show();
                }else{
                    subirImagen();
                }

                break;

            case R.id.btnShowImages:

                break;


        }
    }

    private void subirImagen() {
        //validar que si tenemos una imagen

        if(mImageUri != null){
            //subimos archivo
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"." + getFileExtension(mImageUri));

            //abrir conexion o tarea para subir el archivo
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //abrir el hilo de conexxion
                    Handler handrel= new Handler();
                    handrel.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },5000);

                    Toast.makeText(getApplicationContext(),getString(R.string.msgSuccess),Toast.LENGTH_SHORT).show();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //usamos nuestro modelo de datos para crear la estructura que subiremos a firabase dentro de la base de datos
                            Upload upload = new Upload(txtFileName.getText().toString().trim(),uri.toString());
                            String uploadId = mDataBaseRef.push().getKey();
                            mDataBaseRef.child(uploadId).setValue(upload);
                        }
                    });

                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int p= (int) (100 *
                            (
                                    taskSnapshot.getBytesTransferred()
                                            /
                                            taskSnapshot.getTotalByteCount()
                            )
                    );
                    progressBar.setProgress(p);
                }
            });




        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.msgNotFileSelected),Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(mImageUri));

    }

    private void seleccionarImagen() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST
                && resultCode==RESULT_OK
                && data!=null
                && data.getData()!=null){

//consultamo la informacion que regresa el chooser de android
            mImageUri=data.getData();
            ///previsualizacion de la imagen
            imageView.setImageURI(mImageUri);
        }



    }





}
