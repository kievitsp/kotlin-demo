# Kotlin - making the JVM great again

```java
import java.util.Date;
import java.util.Objects;

public class Trade {
    private final String id;
    private final Date date;

    public Trade(String id, Date date) {
        this.id = id;
        this.data = date;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id='" + id + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trade trade = (Trade) o;
        return Objects.equals(id, trade.id) && Objects.equals(date, trade.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
```

```kotlin
import java.util.Date

data class Trade(
    val id: String,
    val date: Date,
)
```

```java
import java.net.URL;

class Example {
    public Future<String> httpGet(URL url){
        // method here...
    }
}
```


```kotlin
suspend fun httpGet(url: URL) : String  {
    // method here
}
```