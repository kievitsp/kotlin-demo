package java8;

import java.util.Date;
import java.util.Objects;

public class Trade {
    private final String id;
    private final Date date;

    public Trade(String id, Date date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trade Trade = (Trade) o;
        return Objects.equals(id, Trade.id) && Objects.equals(date, Trade.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id='" + id + '\'' +
                ", date=" + date +
                '}';
    }
}
