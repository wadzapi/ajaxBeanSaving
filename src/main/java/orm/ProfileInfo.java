package orm;

public class ProfileInfo {

    private long id;
    private long imgId;
    private byte[] imgData;

    public ProfileInfo(long profileId, long profileImgId) {
        this.id = profileId;
        this.imgId = profileImgId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getImgId() {
        return imgId;
    }

    public void setImgId(long imgId) {
        this.imgId = imgId;
    }

    public byte[] getImgData() {
        return imgData;
    }

    public void setImgData(byte[] imgData) {
        this.imgData = imgData;
    }
}