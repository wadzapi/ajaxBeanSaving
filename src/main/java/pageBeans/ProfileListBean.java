package pageBeans;

import comp.ProfilesComp;
import orm.ProfileInfo;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;

@ManagedBean(name = "profileListBean")
public class ProfileListBean {

    private List<ProfileInfo> usrProfiles;

    @PostConstruct
    public void initBean() {
        //TODO загрузка через DAO в ORM из БД
        usrProfiles = ProfilesComp.loadProfiles();
    }

    public List<ProfileInfo> getUsrProfiles() {
        return usrProfiles;
    }

    public void setUsrProfiles(List<ProfileInfo> usrProfiles) {
        this.usrProfiles = usrProfiles;
    }
}