package jv.usinggdm;

import jv.usinggdm.Profile.Profile;
import jv.usinggdm.Profile.ProfileAggregator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ArchiveServer{

            public static int  PORT_277 = 277;
        private static ProfileAggregator profileAggregator;
            private static ArrayList<Session> sessions;
                private static ServerSocket serverSocket;
                    private static BufferedReader reader;
                        private static ArchiveServer instance;

            private ArchiveServer(){
                try {
                    System.out.println("Server was launched");
                    sessions = new ArrayList<>();
                    serverSocket = new ServerSocket(PORT_277);
                    profileAggregator = new ProfileAggregator();
                    readFromFile();
                    runLogic();
                } catch (IOException ex){
                    System.out.println("IOException caught");
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex){
                    System.out.println("ClassNotFoundException caught");
                    ex.printStackTrace();
                }
            }
                public static ArchiveServer getInstance(){
                        if(instance == null)
                            instance = new ArchiveServer();
                        return instance;
                }


            private void readFromFile() throws IOException, ClassNotFoundException {
                FileInputStream fis;
                ObjectInputStream ois = null;
                   fis =  new FileInputStream("Database\\profiles.dat");
                    if(fis.available()>0) {
                        ois = new ObjectInputStream(fis);
                        while (fis.available() > 0) {
                            profileAggregator.append((Profile) ois.readObject());
                        }
                        System.out.println("Success");
                        ois.close();
                    }
            }
            private static void writeInFile() {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Database\\profiles.dat"));
                    for (int i = 0; i < profileAggregator.getProfiles().length; i++)
                        oos.writeObject(profileAggregator.getProfiles()[i]);
                    System.out.println("Success");
                    oos.flush();
                } catch (IOException ex){
                    System.out.println("File writing error");
                }
            }

            private void runLogic(){
                try {
                    while (true) {
                            if(sessions.size()<8) {
                                Session session = new Session(serverSocket.accept());
                                System.out.println("Socket accepted");
                                sessions.add(session);
                                session.start();
                            }
                    }
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }

        private class Session extends Thread{

                private Socket socket;
                    private BufferedReader br;
                        private BufferedWriter bw;

                Session(Socket socket) throws IOException{
                        this.socket = socket;
                            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                            this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
                    }

                    // Behaviours
            private void view() throws  IOException{
                        reader = new BufferedReader(new InputStreamReader(System.in));
                        if (reader.readLine().toLowerCase().equals("accept")) {
                            bw.write("Accepted\n");
                            bw.flush();
                            String request = br.readLine();
                            Profile viewingProfile = null;
                            try {
                                   viewingProfile = profileAggregator.getProfiles()[profileAggregator.findID(request)];
                            } catch (ArrayIndexOutOfBoundsException ex){
                                    System.out.println("ID not found");
                                    bw.write("ID not found\n");
                                    bw.flush();
                                    return;
                            }
                            String answer = new String();
                            answer = "Name: ";
                            answer += viewingProfile.getName();
                            bw.write(answer + "\n");
                            bw.flush();
                            answer = "Email: ";
                            answer += viewingProfile.getEmail();
                            bw.write(answer + "\n");
                            bw.flush();
                            answer = "Academic performance: ";
                            for (int i : viewingProfile.getPerformance()) {
                                answer += i;
                                answer += " ";
                            }
                            bw.write(answer + "\n");
                            bw.flush();
                            bw.write("Success\n");
                            bw.flush();
                            String contin = br.readLine();
                            System.out.println("Client request: " + contin);
                            if (contin.toLowerCase().equals("modify")) {
                                modify(viewingProfile);
                            }
                        } else {
                            bw.write("Access denied\n");
                            bw.flush();
                        }

            }
            private synchronized void add() throws IOException{
                reader = new BufferedReader(new InputStreamReader(System.in));
                if(reader.readLine().toLowerCase().equals("accept")){
                    bw.write("Accepted\n");
                    bw.flush();
                    Profile profile = new Profile(1);
                        String name = br.readLine();
                            if(name.matches("[A-Za-z]+? ([A-Z]\\. )*([A-Z]\\.)")) {
                                profile.setName(name);
                                bw.write("Input accepted\n");
                                bw.flush();
                            } else{
                                bw.write("Incorrect input\n");
                                bw.flush();
                                return;
                            }
                        String email = br.readLine();
                            if(email.matches("[A-Za-z0-9\\-._ ]+?@[a-z]+?\\.[a-z]+")){
                                profile.setEmail(email);
                                bw.write("Input accepted\n");
                                bw.flush();
                            } else{
                                bw.write("Incorrect input\n");
                                bw.flush();
                                return;
                            }
                        String performance = br.readLine();
                            if(performance.matches("[0-5] [0-5] [0-5] [0-5] [0-5]")) {
                                int[] perf = new int[5];
                                    perf[0] = Integer.valueOf(String.valueOf(performance.toCharArray()[0]));
                                    for(int i=1, j=2; i<perf.length; i++, j+=2){
                                        perf[i] = Integer.valueOf(String.valueOf(performance.toCharArray()[j]));
                                    }
                                profile.setPerformance(perf);
                                bw.write("Input accepted\n");
                                bw.flush();
                            } else{
                                bw.write("Incorrect input\n");
                                bw.flush();
                                return;
                            }
                            ArchiveServer.profileAggregator.append(profile);
                            System.out.println("Success");
                            bw.write("Success\n");
                            bw.flush();
                } else {
                    bw.write("Access denied\n");
                    bw.flush();
                }
            }
            private synchronized void modify(Profile profile) throws IOException{
                reader = new BufferedReader(new InputStreamReader(System.in));
                if(reader.readLine().toLowerCase().equals("accept")){
                    bw.write("Accepted\n");
                    bw.flush();
                    String name = br.readLine();
                    if(name.matches("[A-Za-z]+? ([A-Z]\\. )*([A-Z]\\.)")) {
                        profile.setName(name);
                        bw.write("Input accepted\n");
                        bw.flush();
                    } else{
                        bw.write("Incorrect input\n");
                        bw.flush();
                        return;
                    }
                    String email = br.readLine();
                    if(email.matches("[A-Za-z0-9\\-._ ]+?@[a-z]+?\\.[a-z]+")){
                        profile.setEmail(email);
                        bw.write("Input accepted\n");
                        bw.flush();
                    } else{
                        bw.write("Incorrect input\n");
                        bw.flush();
                        return;
                    }
                    String performance = br.readLine();
                    if(performance.matches("[0-5] [0-5] [0-5] [0-5] [0-5]")) {
                        int[] perf = new int[5];
                        perf[0] = Integer.valueOf(String.valueOf(performance.toCharArray()[0]));
                        for(int i=1, j=2; i<perf.length; i++, j+=2){
                            perf[i] = Integer.valueOf(String.valueOf(performance.toCharArray()[j]));
                        }
                        profile.setPerformance(perf);
                        bw.write("Input accepted\n");
                        bw.flush();
                    } else{
                        bw.write("Incorrect input\n");
                        bw.flush();
                        return;
                    }
                    bw.write("Success\n");
                    bw.flush();
                } else {
                    bw.write("Access denied\n");
                    bw.flush();
                }
            }
                    // Behaviours

                @Override
                public void run(){
                        try {
                            System.out.println("Session runs for: "+socket);
                            while (true) {
                                String firstRequest = br.readLine();
                                System.out.println("Client request: " + firstRequest);
                                switch (firstRequest.toLowerCase()){
                                        case "view" : view(); break;
                                        case "add" : add(); break;
                                        case "quit" :
                                            ArchiveServer.writeInFile();
                                            ArchiveServer.sessions.remove(this);
                                            Thread.currentThread().interrupt();
                                            return;
                                            default:
                                                System.out.println("Unacceptable command");
                                                    break;
                                    }
                            }
                        } catch (IOException ex){
                            ex.printStackTrace();
                        }
                }


        }

}
