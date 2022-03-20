public class Main {
    public static void main(String[] args) {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(new NormalClock());

        String name = "abacaba";
        for (int i = 1; i <= 3; i++) {
            eventsStatistic.incEvent(name);
        }

        String name2 = "abacabadabacaba";
        eventsStatistic.incEvent(name2);

        eventsStatistic.printStatistic();
    }
}