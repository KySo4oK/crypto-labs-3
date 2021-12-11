package com.pryimak;

import java.lang.reflect.Field;

public class BetterMtCracker {

    MT19937_32 createNewGeneratorByStates(int[] states) {
        states = untemper(states);
        final MT19937_32 clone = new MT19937_32();
        Field mt;
        try {
            mt = clone.getClass().getDeclaredField("MT");
            mt.setAccessible(true);
            mt.set(clone, states);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not create a clone", e);
        }
        return clone;
    }

    private int[] untemper(int[] states) {
        for (int i = 0; i < 624; i++) {
            states[i] = untemper(states[i]);
        }
        return states;
    }

    private int untemper(int value) {
        value = unBitshiftRightXor(value, 18);
        value = unBitshiftLeftXor(value, 15, 0xefc60000);
        value = unBitshiftLeftXor(value, 7, 0x9d2c5680);
        value = unBitshiftRightXor(value, 11);
        return value;
    }

    int unBitshiftRightXor(int value, int shift) {
        // we part of the value we are up to (with a width of shift bits)
        int i = 0;
        // we accumulate the result here
        int result = 0;
        // iterate until we've done the full 32 bits
        while (i * shift < 32) {
            // create a mask for this part
            int partMask = (-1 << (32 - shift)) >>> (shift * i);
            // obtain the part
            int part = value & partMask;
            // unapply the xor from the next part of the integer
            value ^= part >>> shift;
            // add the part to the result
            result |= part;
            i++;
        }
        return result;
    }

    int unBitshiftLeftXor(int value, int shift, int mask) {
        // we part of the value we are up to (with a width of shift bits)
        int i = 0;
        // we accumulate the result here
        int result = 0;
        // iterate until we've done the full 32 bits
        while (i * shift < 32) {
            // create a mask for this part
            int partMask = (-1 >>> (32 - shift)) << (shift * i);
            // obtain the part
            int part = value & partMask;
            // unapply the xor from the next part of the integer
            value ^= (part << shift) & mask;
            // add the part to the result
            result |= part;
            i++;
        }
        return result;
    }
}
