/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bar;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arlin
 */
public class Test {
    
    private static Semaphore semVuoto = new Semaphore(5);
    private static Semaphore semPieno = new Semaphore(0);
    
    protected static int nCaffe; 
    private static int incasso;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException{
        // TODO code application logic here
        
        Bar b = new Bar(5);
        ArrayList <ClienteProduttore> listaC = new ArrayList<>();
        CameriereConsumatore c = new CameriereConsumatore(b, "Cameriere");
        
        System.out.println("Bar aperto");
        
       
        for(int i=0; i<5; i++){
        
            listaC.add(new ClienteProduttore(b, "Cliente " +(i+1)));
              
        }
        
        for(int i=0; i<5; i++){
        
            listaC.get(i).start();
        }
        
        for(int i=0; i<5; i++){
        
            listaC.get(i).join();
        }
        
        //c.join();
        
        System.out.println("Incasso: " +incasso +" euro");
        
        
  
        
        
        
        
    }
    
    public static class Bar{
    
        private int posti;
        
        public Bar(int posti){
        
            this.posti = posti;
        }
        
        public void clienteEntra(){
        
            this.posti--;
        }
        
        public void clienteEsce(){
        
            this.posti++;
        }
    }
    
    public static class ClienteProduttore extends Thread{
    
        private Bar bar;
         
        
        public ClienteProduttore(Bar b, String name){
        
            setName(name);
            this.bar = b;
           
            //start();
        }
        
        public void run(){
        
            while(true){
            
                try {
                    sleep((int)(Math.random() * 500));
                } catch (InterruptedException e) {
                }
                
                try {
                    semVuoto.acquire();
                } catch (InterruptedException e) {
                }
                
                bar.clienteEntra();
                nCaffe = (int)((Math.random() * 4)+1);
                System.out.println(getName() +": è entrato e ha ordinato " +nCaffe +" Caffè");
                incasso += nCaffe;
                semPieno.release();
            }
            
            
        }
    }
    
    public static class CameriereConsumatore extends Thread{
        
        private Bar bar;
        
        public CameriereConsumatore(Bar b, String nome){
        
            setName(nome);
            this.bar = b;
            start();
        }
        
        public void run(){
        
            while(true){
            
                try {
                    semPieno.acquire();
                    
                } catch (InterruptedException e) {
                }
                
                //bar.clienteEsce();
                System.out.println(getName() +" ha prelevato un ordine da " +nCaffe +" Caffè");
                semVuoto.release();
              
            }
        }
    
        
    }
    
}
