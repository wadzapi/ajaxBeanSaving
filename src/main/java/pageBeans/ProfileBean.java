package pageBeans;

import orm.ProfileInfo;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.Map;

@ManagedBean(name = "profileBean")
public class ProfileBean {

    private ProfileInfo profileInfo;

    @PostConstruct
    public void initBean() {
        this.profileInfo = new ProfileInfo(getParamFromRequest("profile_id"), getParamFromRequest("img_id"));
    }

    private static Long getParamFromRequest(String paramId) {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        return params.get(paramId) == null ? null : Long.parseLong(params.get(paramId));
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }
}
