package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
        isEmpty = true
        spCV = new Condition2();
        ltCV = new Condition2();
        returnCV = new Condition2();
        communicatorLock = new Lock();
        
    }
    
    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
        System.out.println("["+Machine.timer().getTime()+"]"+"["+KThread.currentThread()+"]is speaking now. speak("+word+")");
        communicatorLock.acquire();
        if (isEmpty == false ){
             System.out.println("["+Machine.timer().getTime()+"]"+"["+KThread.currentThread()+"]is need to wait for listen. can not speak yet");
            ltCV.wake();
            spCV.sleep();            
        }
        
        this.data = word;
        isEmpty = false;
        ltCV.wake();
        returnCV.sleep();
        communicatorLock.release();
         System.out.println("["+Machine.timer().getTime()+"]"+"["+KThread.currentThread()+ " speak has been complete. ");
        
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */    
    public int listen() {
        int dataToreturn;
         System.out.println("["+Machine.timer().getTime()+"]"+"["+KThread.currentThread()+"]is listening now. ");
        communicatorLock.acquire();
        if (isEmpty == true){
             System.out.println("["+Machine.timer().getTime()+"]"+"["+KThread.currentThread()+"] need to wait for speak. can not listen yet.");
            spCV.wake();
            ltCV.sleep();
            //retrun 0; not sure yet
        }
        dataToreturn = data;
        isEmpty = true;
        returnCV.wake();
        communicatorLock.release();
         System.out.println("["+Machine.timer().getTime()+"]"+"["+KThread.currentThread()+"] listen has been completed. the word that has spoken is " + dataToreturn);
        return dataToreturn;
    }
    
    public static void selfTest(){
        
    }
    
    private boolean isEmpty;
    private Condition2 spCV;
    private Condition2 ltCV;
    private Condition2 returnCV;
    private int data;
    private Lock communicatorLock;
}
