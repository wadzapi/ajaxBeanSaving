package customComponents;

import dao.ProfileDao;

import javax.faces.component.*;

@FacesComponent("profileChangeComponent")
public class profileChangeComponent extends UIComponentBase implements NamingContainer {
    @Override
    public String getFamily() {
        return "customComponents.imgChooseComponent";
    }

    public void chooseImg(String profileId) {
        byte[] dataStub = new byte[44]; //TODO Запилить получение данных и загрузку на ините через ImgCacheContainer
        ProfileDao.saveProfile(profileId, dataStub);
    }

}
