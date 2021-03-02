package cgx.core.random;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomTool {

    public double nextDouble(double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }
}
