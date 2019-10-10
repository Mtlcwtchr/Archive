package jv.usinggdm.Profile;

import java.io.Serializable;

public class Profile implements Serializable {

        private int ID;
            private String name;
                private String email;
                    private int[] performance = new int[5];

                    public Profile(int ID){
                        this.ID = ID;
                    }

    // Getters and Setters
    public int getID() {
        return ID;
    }
    public void setID(int ID){
        this.ID = ID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int[] getPerformance() {
        return performance;
    }
    public void setPerformance(int[] performance) {
        this.performance = performance;
    }
    // Getters and Setters

}
