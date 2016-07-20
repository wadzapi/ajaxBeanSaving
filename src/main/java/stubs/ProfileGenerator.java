package stubs;

import orm.ProfileInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileGenerator {

    private static final int PROFILES_SIZE = 15;

    private static Random rnd = new Random();

    public static ProfileInfo generate() {
        return generate(rnd.nextLong());
    }

    public static ProfileInfo generate(long profileId) {
        return new ProfileInfo(profileId, rnd.nextLong());
    }

    public static List<ProfileInfo> generateList(int listSize) {
        List<ProfileInfo> profileList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            profileList.add(generate());
        }
        return profileList;
    }

    public static List<ProfileInfo> generateList() {
        return generateList(PROFILES_SIZE);
    }
}
