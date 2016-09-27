package qianfeng.a6_2letterindexview2;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class User {
    private String username;
    private String pinyin;
    private String FirstLetter;
    private int userface;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", FirstLetter='" + FirstLetter + '\'' +
                ", userface=" + userface +
                '}';
    }

    public User(String username, String pinyin, String firstLetter, int userface) {
        this.username = username;
        this.pinyin = pinyin;
        FirstLetter = firstLetter;
        this.userface = userface;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return FirstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        FirstLetter = firstLetter;
    }

    public int getUserface() {
        return userface;
    }

    public void setUserface(int userface) {
        this.userface = userface;
    }
}
