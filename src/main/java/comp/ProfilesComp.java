package comp;

import orm.ProfileInfo;
import stubs.ProfileGenerator;

import java.util.List;

public class ProfilesComp {

    private static final int PROFILES_STUB_SIZE = 20;

    public static List<ProfileInfo> loadProfiles() {
        //TODO Убрать стаб и запилить загрузку из БД
        return getProfileStubList(PROFILES_STUB_SIZE);
    }

    private static List<ProfileInfo> getProfileStubList(int stubSize) {
        return ProfileGenerator.generateList(stubSize);
    }
}
