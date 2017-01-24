package id.co.tirezone.tirezonemobile;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jessica on 20-Jan-17.
 */

public class Cart implements Parcelable {
    private List<Item> items = new ArrayList<Item>();
    int totalprice;


    public Cart() {
    }

    public Cart(List<Item> items, int totalPrice) {
        this.items = items;
        this.totalprice = totalPrice;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getTotalPrice() {
        return totalprice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalprice = totalPrice;
    }

    public Cart(Parcel in) {
        items = new ArrayList<Item>();
        in.readList(items, getClass().getClassLoader());
        totalprice = in.readInt();
    }

    public int getCartSize() {
        return items.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(items);
        dest.writeInt(totalprice);
    }

    public static final Parcelable.Creator<Cart> CREATOR = new Parcelable.Creator<Cart>() {
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };
}
