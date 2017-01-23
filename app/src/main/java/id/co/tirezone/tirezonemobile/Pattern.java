package id.co.tirezone.tirezonemobile;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jessica on 13-Jan-17.
 */

public class Pattern {
    private String name;
    private String description;
    private List<String> sizes = new ArrayList<>();

    public Pattern() {
    }

    public Pattern(String name, String description, List<String> sizes) {
        this.name = name;
        this.description = description;
        this.sizes = sizes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }
}
