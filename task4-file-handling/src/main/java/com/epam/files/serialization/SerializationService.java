package com.epam.files.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationService {

    public static byte[] serialize(Object obj) throws IOException {
        try (var baos = new ByteArrayOutputStream();
             var oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (var bais = new ByteArrayInputStream(data);
             var ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        }
    }
}
