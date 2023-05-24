package com.example.looptser.Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.looptser.HomeActivity;
import com.example.looptser.R;
import com.example.looptser.users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    String userName, userEmail, userBgUri, userProfileUri;
    private String mParam1;
    private String mParam2;
    private TextView name, email, genderOut, dateOut, ageOut;
    private ImageView uPortada, uPerfil, dot, editButton, backButton;
    private ImageView updatePortada;
    private RelativeLayout aboutMe, userState;
    private LinearLayout editView, dataView;
    private TextView activeLabel;
    Dialog popUp_Dialog;
    private Button submitData;
    private EditText valueGender, valueDate;
    private Boolean stateUser = true;

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

    @SuppressLint("MissingInflatedId")
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

        name = view.findViewById(R.id.user_name);
        userName = homeActivity.getUserName();
        name.setText(userName);

        email = view.findViewById(R.id.user_email);
        userEmail = homeActivity.getUserEmail();
        email.setText(userEmail);

        userBgUri = homeActivity.getUserBgUri();
        userProfileUri = homeActivity.getUserProfileUri();
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
        //Cambio de estado del usuario
        userState = view.findViewById(R.id.Active);
        dot = view.findViewById(R.id.dot_active);
        activeLabel = view.findViewById(R.id.ActivoLabel);
        userState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateUser) {
                    // El usuario está activo
                    dot.setImageResource(R.drawable.dot_active);
                    activeLabel.setText(R.string.active);
                    activeLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                    stateUser = false;
                } else {
                    // El usuario está desactivado
                    dot.setImageResource(R.drawable.dot_disabled);
                    activeLabel.setText(R.string.disabled);
                    activeLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    stateUser = true;
                }

                DatabaseReference userRef = mDatabase.getReference().child("user").child(currUserId).child("stateUser");
                userRef.setValue(stateUser);
            }
        });

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

                editButton =popUp_Dialog.findViewById(R.id.edit);
                backButton = popUp_Dialog.findViewById(R.id.backButton);
                dataView = popUp_Dialog.findViewById(R.id.dataLayout);
                editView = popUp_Dialog.findViewById(R.id.editLayout);

                submitData = popUp_Dialog.findViewById(R.id.data_button);
                valueGender = popUp_Dialog.findViewById(R.id.input_gender);
                valueDate = popUp_Dialog.findViewById(R.id.input_date);

                DatabaseReference userRef = mDatabase.getReference().child("user").child(currUserId);

                genderOut = popUp_Dialog.findViewById(R.id.Gender);
                dateOut = popUp_Dialog.findViewById(R.id.Date);
                ageOut = popUp_Dialog.findViewById(R.id.Age);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dataView.setVisibility(View.GONE);
                        editView.setVisibility(View.VISIBLE);
                        backButton.setVisibility(View.VISIBLE);
                        editButton.setVisibility(View.INVISIBLE);
                    }
                });
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editView.setVisibility(View.GONE);
                        dataView.setVisibility(View.VISIBLE);
                        backButton.setVisibility(View.INVISIBLE);
                        editButton.setVisibility(View.VISIBLE);
                    }
                });
                submitData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String gender = valueGender.getText().toString().trim();
                        String date = valueDate.getText().toString().trim();

                        DatabaseReference userRef = mDatabase.getReference().child("user").child(currUserId);

                        userRef.child("gender").setValue(gender);
                        userRef.child("date").setValue(date)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Error al enviar los datos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        // Cerrar el diálogo después de enviar los datos
                        popUp_Dialog.dismiss();
                    }
                });

                /* Show data user */
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String gender = dataSnapshot.child("gender").getValue(String.class);
                            String dateString = dataSnapshot.child("date").getValue(String.class);

                            if (gender != null && dateString != null) {
                                genderOut.setText(gender);
                                dateOut.setText(dateString);

                                Calendar calendarActual = Calendar.getInstance();

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Date fechaAlmacenada;
                                try {
                                    fechaAlmacenada = sdf.parse(dateString);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }

                                Calendar calendarAlmacenada = Calendar.getInstance();
                                calendarAlmacenada.setTime(fechaAlmacenada);

                                int yearActual = calendarActual.get(Calendar.YEAR);
                                int monthActual = calendarActual.get(Calendar.MONTH) + 1;
                                int dayActual = calendarActual.get(Calendar.DAY_OF_MONTH);

                                int yearUser = calendarAlmacenada.get(Calendar.YEAR);
                                int monthUser = calendarAlmacenada.get(Calendar.MONTH) + 1;
                                int dayUser = calendarAlmacenada.get(Calendar.DAY_OF_MONTH);

                                int edad = yearActual - yearUser;
                                if (monthActual < monthUser) {
                                    edad--;
                                } else if (monthActual == monthUser && dayActual < dayUser) {
                                    edad--;
                                }
                                String age = String.valueOf(edad);
                                ageOut.setText(age);
                            }else{
                                userRef.child("gender").setValue("N");
                                userRef.child("date").setValue("22-11-0000");
                                genderOut.setText(gender);
                                dateOut.setText(dateString);
                                ageOut.setText("0");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Error al leer los datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
            DatabaseReference reference = mDatabase.getReference().child("user").child(currUserId);
            Uri path = data.getData();

            if(typeImg.equals("profile")) {
//                UPLOAD THE PROFILE IMG
                StorageReference imgRef =  mStorage.getReference().child("users").child(currUserId).child(currUserId+"profile.jpg");
                DatabaseReference urlImg = mDatabase.getReference().child("user").child(currUserId).child("imgUriProfile");

                uPerfil.setImageURI(path);

                imgRef.putFile(path).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String finalImgUri = uri.toString();
                                Users users = new Users(currUserId, userName, userEmail, finalImgUri, userBgUri);

                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Imagen de perfil agredada ✨", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Error al agregar modificar la imagen 😰", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            } else {
//                UPLOAD THE BG
                StorageReference imgRef =  mStorage.getReference().child("users").child(currUserId).child(currUserId+"bg.jpg");
                DatabaseReference urlImg = mDatabase.getReference().child("user").child(currUserId).child("imgUriBackground");

                uPortada.setImageURI(path);

                imgRef.putFile(path).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String finalImgUri = uri.toString();
                                Users users = new Users(currUserId, userName, userEmail, userProfileUri, finalImgUri);

                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Imagen de portada agredada ✨", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Error al agregar modificar la imagen 😰", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }
}