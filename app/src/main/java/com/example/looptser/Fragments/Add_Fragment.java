package com.example.looptser.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.looptser.R;

import java.util.ArrayList;

import javax.xml.transform.OutputKeys;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.EasyPermissions;

public class Add_Fragment extends Fragment {


    Button btPick;
    RecyclerView recyclerView;
    ArrayList <Uri> arrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        btPick = btPick.findViewById(R.id.bt_pick);
        recyclerView = recyclerView.findViewById(R.id.recyclerView);

        btPick.setOnClickListener(view -> {
            String[] strings = {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE};

            if(EasyPermissions.hasPermissions(this,strings)){
                imagePicker();
            }else {
                EasyPermissions.requestPermissions(
                        this,
                        "App needs access to your camera and storage",
                        100,
                        strings
                );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == FilePickerConst.REQUEST_CODE_PHOTO){
                arrayList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }

    private void imagePicker() {
        FilePickerBuilder.getInstance()
                .setActivityTitle("Imagen seleccionada")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 4)
                .setMaxCount(4)
                .setSelectedFiles(arrayList)
                .setActivityTheme(R.style.CustomTheme)
                .pickPhoto(this);

    }


}