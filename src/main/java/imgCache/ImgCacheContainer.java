package imgCache;

import java.util.HashMap;
import java.util.Map;

public class ImgCacheContainer {
    //TODO Запилить вместо массивка байтов нек. класс, который будет маппится с ORM В БД
    //TODO Запилить инициализацию при инициализации контекста FacesServlet приложения
    public static final int INIT_CACHE_SIZE = 5;

    private Map<String,byte[]> imgCache;

    public ImgCacheContainer() {
        imgCache=new HashMap<>(INIT_CACHE_SIZE);
    }

    public byte[] getImgData(String imgId) {
        return imgCache.get(imgId);
    }

    public void setImgData(String imgId, byte[] imgData) {
        imgCache.put(imgId, imgData);
    }
}
