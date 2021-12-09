package com.pryimak;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class Cracker {
    long crackMultiplier(List<Long> numbers, long modulus) {
        long inverse_modulus = mod_inverse(numbers.get(1) - numbers.get(0), modulus);
        long multiplier = (numbers.get(2) - numbers.get(1)) * inverse_modulus % modulus;
        System.out.println("multiplier - " + multiplier);
        return multiplier;
    }

    long crackIncrement(List<Long> numbers, long modulus, long multiplier) {
        long increment = (numbers.get(1) - multiplier * numbers.get(0)) % modulus;
        System.out.println("increment - " + increment);
        return increment;
    }

    long makeBet(List<Long> numbers, long modulus, long multiplier, long increment) {
        long bet = (numbers.get(2) * multiplier + increment) % modulus;
        System.out.println("bet - " + bet);
        return bet;
    }

    public static long[] xgcd(long a, long b) {
        long[] retvals = {0, 0, 0};
        long aa[] = {1, 0}, bb[] = {0, 1}, q = 0;
        while (true) {
            q = a / b;
            a = a % b;
            aa[0] = aa[0] - q * aa[1];
            bb[0] = bb[0] - q * bb[1];
            if (a == 0) {
                retvals[0] = b;
                retvals[1] = aa[1];
                retvals[2] = bb[1];
                return retvals;
            }
            ;
            q = b / a;
            b = b % a;
            aa[1] = aa[1] - q * aa[0];
            bb[1] = bb[1] - q * bb[0];
            if (b == 0) {
                retvals[0] = a;
                retvals[1] = aa[0];
                retvals[2] = bb[0];
                return retvals;
            }
            ;
        }
    }

    public static long mod_inverse(long a, long modulus) {
        long[] xgcd = xgcd(a, modulus);
        if (xgcd[0] != 1) {
            throw new RuntimeException();
        }
        return xgcd[1] % modulus;
    }
}
