package infStrike.objects;

public class mapInfo {
    String name;
    int width;
    int height;

    public mapInfo(String arg1, int arg2, int arg3) {
        name = arg1;
        width = arg2;
        height = arg3;
    }

    public String getName() {
        return name;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
} 