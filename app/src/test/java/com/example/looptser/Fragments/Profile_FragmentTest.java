package com.example.looptser.Fragments;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class Profile_FragmentTest {

        @Test
        public void calcularEdad() {
            // actual date
            int yearActual = 2023;
            int monthActual = 5;
            int dayActual = 23;

            // actual user date
            int yearUser = 1990;
            int monthUser = 8;
            int dayUser = 15;

            int edad = yearActual - yearUser;
            if (monthActual < monthUser) {
                edad--;
            } else if (monthActual == monthUser && dayActual < dayUser) {
                edad--;
            }

            assertEquals("La edad deberia ser 32",32, edad);
            Assert.assertTrue(edad  >= 18 );
        }
}