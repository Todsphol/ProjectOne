package th.co.todsphol.add.projectone;

public class UserModel {
    public String LAT, LON, key;

    public UserModel() {

    }

    public UserModel(String latitude, String longitude, String key) {
        this.LAT = latitude;
        this.LON = longitude;
        this.key = key;
    }
}
