package com.example.apptemvaga;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;


import com.example.apptemvaga.Classes.Republica;
import com.example.apptemvaga.Classes.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void usuarioCadTeste(){
        Usuario user = new Usuario();
        user.setNome("Fulana");
        user.setTelefone("000000");
        user.setEmail("g@gmail.com");
        user.setSenha("aba");

        assertNotNull(user);

    }

    @Test
    public  void republiaCadTest(){
        Republica r = new Republica();
        r.setEndereco("Rua aqui");
        r.setTipo("feminino");
        r.setQuantidadeVagas("2");
        r.setReferencia("perto aqui");

        assertNotNull(r);

    }


}