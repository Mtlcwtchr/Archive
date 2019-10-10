package jv.usinggdm;

import java.io.IOException;

public class TestUser {
    public static void main(String[] args) {
        try{
            Client client = new Client();
            client.start();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
