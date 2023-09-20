package tutorial;


import bugwars.user.*;

/* class for storing counters in the shared memory array
   The usage is simple, choose a position of the array where you want to declare a counter,
   make sure that no other class or method uses the 3 next positions (counters use 4 space),
   and call functions passing the chosen position.

   Example:
   counter.increaseValueByOne(ARRAY_INDEX)
   counter.read(ARRAY_INDEX)

   As a better example, check unit counters in this sample project

   Class implementation:
   The class stores 4 values [round1, round2, value1, value2]. The idea is that we want to read
   previous round counter if we are at round 2 we want to read the value of round 1. We do that
   in order to allow all the units to increase the value of the counter (and not only the ones
   that play before the current unit). When reading a value, we check that the round stored
   is equal to the current round - 1 (with that, we know we are reading a valid value).

   Before increasing the value of a counter, we need to check that the round stored is equal to
   the current round, if not, we reset the value to 0 before increasing, since we are the first
   unit to increase the value in this round.

 */
public class Counter {

    UnitController uc;
    public final int COUNTERS_SPACE = 4;

    Counter(UnitController unitController) {
        uc = unitController;
    }
    
    public void reset(int key) {
        for(int i = 0; i < COUNTERS_SPACE; i++) {
            uc.write(key + i, 0);
        }
    }

    private void roundClear(int key) {
        int shift = uc.getRound() & 1;
        if(uc.read(key + shift) != uc.getRound()) {
            uc.write(key + shift, uc.getRound());
            uc.write(key + shift + 2, 0);
        }
    }

    public void increaseValue(int key, int amount) {
        this.roundClear(key);

        int shift = uc.getRound() & 1;

        int realId = key + 2 + shift;
        int value = uc.read(realId);
        uc.write(realId, value + amount);
    }

    public void increaseValueByOne(int key) {
        this.increaseValue(key, 1);
    }

    public int read(int key) {
        int lshift = uc.getRound() & 1;
        int rshift = (uc.getRound() + 1) & 1;

        int left = 0;
        int right = 0;

        if(uc.read(key + lshift) == uc.getRound()) {
            left = uc.read(key + lshift + 2);
        }

        if(uc.read(key + rshift) == uc.getRound() - 1) {
            right = uc.read(key + rshift + 2);
        }

        return Math.max(left, right);
    }

    public int readThisRoundOnly(int key) {
        int lshift = uc.getRound() & 1;
        int left = 0;
        if(uc.read(key + lshift) != uc.getRound()) {
            left = uc.read(key + lshift + 2);
        }
        return left;
    }

}
