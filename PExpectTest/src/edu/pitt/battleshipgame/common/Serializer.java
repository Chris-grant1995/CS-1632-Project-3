package edu.pitt.battleshipgame.common;

import edu.pitt.battleshipgame.common.board.Board;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Serializer {
    public static byte [] toByteArray(Serializable obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject((obj));
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            System.err.println("Error writing bytes");
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                System.out.println("Error closing stream");
                e.printStackTrace();
            }
        }
        return bytes;
    }
    
    public static Object fromByteArray(byte [] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object obj = null;
        try {
            in = new ObjectInputStream(bis);
            obj = in.readObject();
        } catch (Exception e) {
            System.err.println("Error reading bytes to object.");
            e.printStackTrace();
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing input stream");
                e.printStackTrace();
            }
        }
        return obj;
    }
}