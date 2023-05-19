package com.example.looptser.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.looptser.HomeActivity;
import com.example.looptser.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private FirebaseStorage mStorage;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private String currUserId;

    String typeImg;

    private String mParam1;
    private String mParam2;
    boolean isClicked = false;
    private ImageView uPortada, uPerfil, dot;
    private ImageView updatePortada;
    private RelativeLayout aboutMe, userState;
    private TextView activeLabel;
    Dialog popUp_Dialog;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile_Fragment newInstance(String param1, String param2) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        mStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUserId = mAuth.getCurrentUser().getUid();
        typeImg = "";

        HomeActivity homeActivity = (HomeActivity) getActivity();
        assert homeActivity != null;

        uPerfil = view.findViewById(R.id.imgPerfil);
        ((ImageView) uPerfil).setImageBitmap(homeActivity.getuBitPerfil());
        uPortada = view.findViewById(R.id.portada);
        ((ImageView) uPortada).setImageBitmap(homeActivity.getuBitBg());

/*
        uPortada = view.findViewById(R.id.portada);
        Glide.with(getActivity())
                .load(R.drawable.perfil)
                .into(uPortada);

        uPerfil = view.findViewById(R.id.imgPerfil);
        Glide.with(this)
                .load(R.drawable.perfil)
                .into(uPerfil);
*/

        updatePortada = view.findViewById(R.id.updatePortada);
        updatePortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeImg = "bg";
                updateImage();
            }
        });

        uPerfil = view.findViewById(R.id.imgPerfil);
        uPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeImg = "profile";
                updateImage();
            }
        });

        //Mostrar PopUp (Sobre mi)
        aboutMe = view.findViewById(R.id.SobreMi);
        popUp_Dialog = new Dialog(requireContext());
        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp_Dialog.setContentView(R.layout.pop_up_about_me);
                popUp_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUp_Dialog.show();
            }
        });

        //Cambio de estado del usuario
        userState = view.findViewById(R.id.Active);
        dot = view.findViewById(R.id.dot_active);
        activeLabel = view.findViewById(R.id.ActivoLabel);
        userState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClicked){
                    dot.setImageResource(R.drawable.dot_active);
                    activeLabel.setText(R.string.active);
                    activeLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                    isClicked = false;
                }else{
                    dot.setImageResource(R.drawable.dot_disabled);
                    activeLabel.setText(R.string.disabled);
                    activeLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    isClicked = true;
                }
            }
        });
        return view;
    }

    //Acceso a la galeria mediante intent
    public void updateImage(){
        Intent intentUP = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentUP.setType("image/");
        startActivityForResult(intentUP.createChooser(intentUP,"Seleccione"),10);
    }

    //Verificación de la función e implementación de la nueva imagen
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path = data.getData();

            if(typeImg.equals("profile")) {
                StorageReference imgRef =  mStorage.getReference().child("users").child(currUserId).child(currUserId+"profile.jpg");
                DatabaseReference urlImg = mDatabase.getReference().child("user").child(currUserId).child("imgUriProfile");

                imgRef.delete();
                imgRef.putFile(path);
                uPerfil.setImageURI(path);

                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadPhotoUrl) {
                        urlImg.setValue(downloadPhotoUrl.toString());
                    }
                });
            } else {
                StorageReference imgRef =  mStorage.getReference().child("users").child(currUserId).child(currUserId+"bg.jpg");
                DatabaseReference urlImg = mDatabase.getReference().child("user").child(currUserId).child("imgUriBackground");

                imgRef.delete();
                imgRef.putFile(path);
                uPortada.setImageURI(path);

                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadPhotoUrl) {
                        urlImg.setValue(downloadPhotoUrl.toString());
                    }
                });
            }
        }
    }
}