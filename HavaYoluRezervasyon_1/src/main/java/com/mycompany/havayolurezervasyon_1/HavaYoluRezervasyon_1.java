package com.mycompany.havayolurezervasyon_1;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class HavaYoluRezervasyon_1 {

    static HashMap<Integer, Boolean> seats = new HashMap<>();

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();

        for (int i = 1; i <= 5; i++) { //5 koltuk
            seats.put(i, false);
        }

        for (int i = 1; i < 30; i++) {
            new ReaderThread(1, i, lock).start();
            new WriterThread(1, i, lock).start();
        }
    }
    static class WriterThread extends Thread {

        private int threadIndex;
        private int seatId;
        private ReentrantLock lock;

        WriterThread(int seatId, int threadIndex, ReentrantLock lock) {
            this.seatId = seatId;
            this.threadIndex = threadIndex;
            this.lock = lock;
        }

        @Override
        public void run() {
            makeReservation(seatId, threadIndex);
        }

        private void makeReservation(int seatId, int threadIndex) {
            LocalDateTime now = LocalDateTime.now();
            System.out.println(now + "\nWriter" + threadIndex + " tries to book the seat " + seatId + "\n");
            try {
                //lock.lock(); 

                if (!seats.get(seatId)) {
                    System.out.println(now + "\nWriter" + threadIndex + " booked seat " + seatId + " successfully.\n");
                    seats.put(seatId, true);
                } else {
                    System.out.println(now + "\nWriter" + threadIndex + " could not booked seat number " + seatId + " since it has been already booked.\n");
                }

            } catch (Exception e) {
                System.out.println(e);
            } finally {
                //lock.unlock();
            }
        }
        
    private void cancelReservation(int seatId) {
        try {
           // lock.lock(); 

            if (seats.get(seatId)) { 
                seats.put(seatId, false); 
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            //lock.unlock(); 
        }
    }
    }
    static class ReaderThread extends Thread {

        private int threadIndex;
        private int seatId;
        private ReentrantLock lock;

        ReaderThread(int seatId, int threadIndex, ReentrantLock lock) {
            this.seatId = seatId;
            this.threadIndex = threadIndex;
            this.lock = lock;
        }

        @Override
        public void run() {
            queryReservation(threadIndex);
        }

        private synchronized void queryReservation(int threadIndex) {
            LocalDateTime now = LocalDateTime.now();
            try {
                lock.lock(); 
                System.out.println(now + "\nReader" + threadIndex + " looks for available seats. State of the seats are:");
                for (int i = 0; i < seats.size(); i++) {
                    System.out.print("Seat No: " + (i + 1) + " " + seats.get(i + 1) + " ");
                }
                System.out.println("\n");

            } catch (Exception e) {
                System.out.println(e);
            } finally {
                lock.unlock(); 
            }
        }
    }
}
