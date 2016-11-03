package com.example.jason.crudpeople;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jason on 11/2/2016.
 */

public class Person implements Serializable {
    public String firstName, lastName, zipCode, phoneNumber;
    public Date dateOfBirth;

    private void writeObject (java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(firstName);
        out.writeObject(lastName);
        out.writeObject(zipCode);
        out.writeObject(phoneNumber);
        out.writeObject(dateOfBirth);
    }

    private void readObject (java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        firstName = (String)in.readObject();
        lastName = (String)in.readObject();
        zipCode = (String)in.readObject();
        phoneNumber = (String)in.readObject();
        dateOfBirth = (Date)in.readObject();
    }
}