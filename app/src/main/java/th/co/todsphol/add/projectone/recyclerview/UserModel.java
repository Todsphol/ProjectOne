package th.co.todsphol.add.projectone.recyclerview;

public class UserModel {
    public String LAT, LON;
    public Integer key;

    public UserModel() {

    }

    public UserModel(String latitude, String longitude, Integer key) {
        this.LAT = latitude;
        this.LON = longitude;
        this.key = key;
    }
}
