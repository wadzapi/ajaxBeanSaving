package dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import orm.ProfileInfo;
import stubs.ProfileGenerator;

public class ProfileDao {


    public static final Log log = LogFactory.getLog(ProfileDao.class);

    public static ProfileInfo loadProfile(long profileId) {
        String baseMsg = " загрузки изображения из БД.";
        log.info("Начало" + baseMsg);
        try {
            ;//TODO Здесь запилить загрузку через ORM сущности из БД
            log.info("Конец" + baseMsg);
        } catch (Exception ex) {
            log.error("Ошибка" + baseMsg, ex);
        }
        //TODO Заменить стаб на загрузку из БД
        return ProfileGenerator.generate(profileId);
    }

    //TODO Запилить вместо массивка байтов нек. класс, который будет маппится с ORM В БД
    public static void saveProfile(String imgId, byte[] imgData) {
        String baseMsg = " сохранения изображения в БД.";
        log.info("Начало" + baseMsg);
        try {
            ;//TODO Здесь запилить проливку через ORM сущности в БД
            log.info("Конец" + baseMsg);
        } catch (Exception ex) {
            log.error("Ошибка" + baseMsg, ex);
        }
    }
}
