package jv.usinggdm;

import java.io.IOException;

public class TestAdmin {
    public static void main(String[] args) {
        try{
            Client client = new Client(1);
            client.start();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
