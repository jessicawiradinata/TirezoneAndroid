package id.co.tirezone.tirezonemobile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jessica on 20-Jan-17.
 */

public class Item implements Parcelable{
    private String pattern;
    private String size;
    private int qty;
    private int price;

    public Item() {
    }

    public Item(String pattern, String size, int qty, int price) {
        this.pattern = pattern;
        this.size = size;
        this.qty = qty;
        this.price = price;
    }

    public Item(Parcel in) {
        pattern = in.readString();
        size = in.readString();
        qty = in.readInt();
        price = in.readInt();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pattern);
        dest.writeString(size);
        dest.writeInt(qty);
        dest.writeInt(price);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
