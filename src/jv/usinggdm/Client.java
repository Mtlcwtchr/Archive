package jv.usinggdm;

import java.io.*;
import java.net.Socket;

public class Client extends Thread {

        private int ACCESS_MODIFIER = 0;
            private Socket socket;
                private BufferedReader reader;
                    private BufferedReader br;
                        private BufferedWriter bw;

            Client(int ACCESS_MODIFIER) throws IOException {
                this.ACCESS_MODIFIER = ACCESS_MODIFIER;
                System.out.println("Waiting for connection...");
                this.socket = new Socket("localhost", ArchiveServer.PORT_277);
                System.out.println("Successfully connected");
                    this.reader = new BufferedReader(new InputStreamReader(System.in));
                        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            Client() throws IOException{
                System.out.println("Waiting for connection...");
                this.socket = new Socket("localhost", ArchiveServer.PORT_277);
                System.out.println("Successfully connected");
                    this.reader = new BufferedReader(new InputStreamReader(System.in));
                        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }

                // Behaviours
            private void view()throws  IOException{
                String answr = br.readLine();
                System.out.println(answr);
                if(answr.equals("Accepted")){
                    String request = reader.readLine();
                        bw.write(request+"\n");
                        bw.flush();
                        String answer = br.readLine();
                        System.out.println(answer);
                        if(answer.equals("ID not found")) return;
                            for(int i=0; i<2;i++){
                                answer = br.readLine();
                                System.out.println(answer);
                            }
                            answer = br.readLine();
                            System.out.println(answer);
                            if(this.ACCESS_MODIFIER == 1) {
                                String contin = reader.readLine();
                                bw.write(contin+"\n");
                                bw.flush();
                                if (contin.toLowerCase().equals("modify")) {
                                    modify();
                                }
                            } else{
                                bw.write("Not enough rights\n");
                                bw.flush();
                            }
                }
            }
            private void add() throws IOException {
                String answr = br.readLine();
                System.out.println(answr);
                if(answr.equals("Accepted")){
                    String name = reader.readLine();
                    bw.write(name+"\n");
                    bw.flush();
                    String checker = br.readLine();
                    System.out.println(checker);
                    if(checker.equals("Incorrect input")) return;
                    String email = reader.readLine();
                    bw.write(email+"\n");
                    bw.flush();
                    checker = br.readLine();
                    System.out.println(checker);
                    if(checker.equals("Incorrect input")) return;
                    String performance = reader.readLine();
                    bw.write(performance+"\n");
                    bw.flush();
                    checker = br.readLine();
                    System.out.println(checker);
                    if(checker.equals("Incorrect input")) return;
                    String result = br.readLine();
                    System.out.println(result);
                } else {
                    System.out.println("Access denied");
                }
            }
            private void modify() throws IOException{
                    String answr = br.readLine();
                    System.out.println(answr);
                    if(answr.equals("Accepted")){
                        String name = reader.readLine();
                            bw.write(name+"\n");
                            bw.flush();
                            String checker = br.readLine();
                            System.out.println(checker);
                            if(checker.equals("Incorrect input")) return;
                        String email = reader.readLine();
                            bw.write(email+"\n");
                            bw.flush();
                            checker = br.readLine();
                        System.out.println(checker);
                        if(checker.equals("Incorrect input")) return;
                        String performance = reader.readLine();
                            bw.write(performance+"\n");
                            bw.flush();
                        checker = br.readLine();
                        System.out.println(checker);
                        if(checker.equals("Incorrect input")) return;
                        String result = br.readLine();
                        System.out.println(result);
                    } else {
                        System.out.println("Access denied\n");
                    }
            }
                // Behaviours

        @Override
        public void run(){
                try {
                    while (true) {
                        String request = reader.readLine();
                            bw.write(request+"\n");
                                bw.flush();
                        switch (request.toLowerCase()){
                            case "view" : view(); break;
                            case "add" : add(); break;
                            case "quit" :
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
