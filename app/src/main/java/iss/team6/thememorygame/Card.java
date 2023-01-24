package iss.team6.thememorygame;

public class Card {
    private String imageName;
    private int imageId;

    public void setSwapped(boolean swapped) {
        isSwapped = swapped;
    }

    private boolean isSwapped;
    private boolean isRemoved;
    private int x;
    private int y;


    public Card() {
        isSwapped=false;
        isRemoved=false;
    }

    public Card(String imageName, int imageId,int x,int y) {
        this.imageName = imageName;
        this.imageId = imageId;
        this.x=x;
        this.y=y;
        isSwapped=false;
        isRemoved=false;

    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isSwapped() {
        return isSwapped;
    }


    public boolean isRemoved() {
        return isRemoved;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public void swapCard()
    {
        isSwapped=true;
    }

    public void removeCard(){ isRemoved=true; }


}
