import java.time.Instant;

public class ChangeableClock implements Clock {
    private Instant now;

    public ChangeableClock(Instant now) {
        setNow(now);
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    @Override
    public Instant now() {
        return now;
    }
}