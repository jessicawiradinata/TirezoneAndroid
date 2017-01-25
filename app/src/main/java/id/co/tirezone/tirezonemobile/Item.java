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
    private int subtotal;

    public Item() {
    }

    public Item(String pattern, String size, int qty, int subtotal) {
        this.pattern = pattern;
        this.size = size;
        this.qty = qty;
        this.subtotal = subtotal;
    }

    public Item(Parcel in) {
        pattern = in.readString();
        size = in.readString();
        qty = in.readInt();
        subtotal = in.readInt();
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

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
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
        dest.writeInt(subtotal);
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
