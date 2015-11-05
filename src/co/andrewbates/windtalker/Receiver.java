package co.andrewbates.windtalker;

interface Receiver {
    public void receive(String message, boolean self);
}