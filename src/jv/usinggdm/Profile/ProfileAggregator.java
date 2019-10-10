package jv.usinggdm.Profile;

import java.util.Arrays;

public class ProfileAggregator {

        private Profile[] profiles; // Always sorted by ID, ID = index in aggregator

            public ProfileAggregator(Profile[] profiles){
                this.profiles = profiles;
            }
                public ProfileAggregator(){}

        public void append(Profile profile){
                if(this.profiles == null) {
                    this.profiles = new Profile[1];
                    this.profiles[0] = profile;
                }
                else{
                    this.profiles = Arrays.copyOf(this.profiles, this.profiles.length+1);
                        this.profiles[this.profiles.length-1] = profile;
                        profile.setID(this.profiles.length-1);
                }
        }
        public void remove(){
                this.profiles = Arrays.copyOf(this.profiles, this.profiles.length-1);
        }
        public void remove(String mode){
                try {
                    remove(findID(mode));
                } catch (ArrayIndexOutOfBoundsException ex){
                    System.out.println("ID not found");
                }
        }
        public void remove(int ID){
                split(ID);
            this.profiles = Arrays.copyOf(this.profiles, this.profiles.length-1);
        }
        private void split(int ID){
                for(int i = ID; i<this.profiles.length-1; i++){
                    Profile p = this.profiles[i];
                    this.profiles[i] = this.profiles[i+1];
                    this.profiles[i+1] = p;
                }
        }
        public int findID(String mode){
                if(profiles==null) throw new ArrayIndexOutOfBoundsException();
                if(mode.matches("[A-Za-z0-9\\-._ ]+?@[a-z]+?\\.[a-z]+"))
                    for(int i=0; i<profiles.length; i++)
                        if(profiles[i].getEmail().equals(mode))
                            return i;
                if(mode.matches("[A-Za-z]+? ([A-Z]\\. )*([A-Z]\\.)"))
                    for(int i=0; i<profiles.length; i++)
                        if(profiles[i].getName().equals(mode))
                            return i;
                throw new ArrayIndexOutOfBoundsException();
        }
        public Profile[] getProfiles(){
                return this.profiles;
        }

}
