package edu.upc.dsa.utils;

import net.moznion.random.string.RandomStringGenerator;
import java.util.UUID;

public class RandomUtils {
    public static String getId() {
        return UUID.randomUUID().toString();
    }
}

