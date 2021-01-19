package com.leticiafraga.receitas;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {
    private static FirebaseAuth auth;
    private static DatabaseReference referencia;
    private static StorageReference storage;

    public static String getIdUsuario() {
        return getAuth().getCurrentUser().getUid();
    }

    public static DatabaseReference getFirebase() {
        if (referencia == null) {
            referencia = FirebaseDatabase.getInstance().getReference();
        }
        return referencia;
    }

    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static StorageReference getStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

}
