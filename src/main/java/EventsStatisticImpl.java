import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventsStatisticImpl implements EventsStatistic {
    private final Clock clock;
    private final List<Pair<String, Instant>> events;

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
        this.events = new ArrayList<>();
    }

    @Override
    public void incEvent(String name) {
        events.add(new Pair<>(name, clock.now()));
    }

    @Override
    public double getEventStatisticByName(String name) {
        Instant lastHourCheckpoint = clock.now().minus(1, ChronoUnit.HOURS);
        return events.stream()
                .dropWhile(p -> p.getSecond().isBefore(lastHourCheckpoint))
                .filter(p -> p.getFirst().equals(name))
                .count() / 60.0;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        return events.stream()
                .map(Pair::getFirst)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), this::getEventStatisticByName));
    }

    @Override
    public void printStatistic() {
        events.stream()
                .collect(Collectors.groupingBy(Pair::getFirst))
                .forEach((s, l) -> {
                    Instant first = l.get(0).getSecond();
                    Instant last = l.get(l.size() - 1).getSecond();
                    Duration between = Duration.between(first, last);
                    String rpm =
                            between.toNanos() == 0 ?
                                    "infinity" : Double.toString(l.size() * 1e9 * 60 / between.toNanos());
                    System.out.println(s + " has rpm " + rpm);
                });
    }
}