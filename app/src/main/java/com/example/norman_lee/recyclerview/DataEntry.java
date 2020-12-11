package com.example.norman_lee.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataEntry extends AppCompatActivity {

    EditText editTextNameEntry;
    Button buttonSelectImage;
    Button buttonOK;
    ImageView imageViewSelected;
    Bitmap bitmap;
    final static int REQUEST_IMAGE_GET = 2000;
    final static String KEY_PATH = "Image";
    final static String KEY_NAME = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        editTextNameEntry = findViewById(R.id.editTextNameEntry);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        buttonOK = findViewById(R.id.buttonOK);

        //TODO 12.2 Set up an implicit intent to the image gallery (standard code)
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }

            }
        });

        //TODO 12.4 When the OK button is clicked, set up an intent to go back to MainActivity
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write an explicit intent
                int resultCode = Activity.RESULT_OK;
                Intent intent = new Intent(DataEntry.this,MainActivity.class);
                //extract from edittext widget
                String name = String.valueOf(editTextNameEntry.getText());
                String path = Utils.saveToInternalStorage(bitmap,name,DataEntry.this);
                intent.putExtra(KEY_NAME,name);
                intent.putExtra(KEY_PATH,path);
                setResult(resultCode,intent);
                finish();
            }
        });

        //TODO 12.5 --> Go back to MainActivity


    }

    //TODO 12.3 Write onActivityResult to get the image selected
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            //if image selected is passed as the uri
            Uri fullphotoUri = data.getData();
            //display image selected
            imageViewSelected.setImageURI(fullphotoUri);
            //get bitmap image from the uri
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),fullphotoUri);
            } catch (FileNotFoundException ex){
                Toast.makeText(DataEntry.this,"Please enter a valid image",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(DataEntry.this, "Please select valid image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
