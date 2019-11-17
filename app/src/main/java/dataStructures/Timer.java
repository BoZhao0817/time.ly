package dataStructures;
public class Timer {

    public String timeString;
    public State state;
    public boolean done;
    public int seconds;
    public int count = 0;

    public Timer(int s) {
        timeString = Presentation.toStringTime(s);
        state = State.PLAYING;
        seconds = s;
    }

    public void updateTimer() {
        switch(state) {
            case PAUSED:
                break;
            case PLAYING:
                seconds--;
                timeString = Presentation.toStringTime(seconds);
                if(seconds == 0) {
                    count++;
                    done = true;
                }
                break;
            default:
                break;
        }
    }
}
