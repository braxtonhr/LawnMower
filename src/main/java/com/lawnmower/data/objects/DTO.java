package com.lawnmower.data.objects;

import com.lawnmower.util.R;
import org.json.JSONObject;


public abstract class DTO {

    private String type = "";

    public DTO(String type) {
        this.type = type;
    }

    public boolean isPerson() {
        return type.equals(R.PERSON);
    }

    public boolean isReport() {
        return type.equals(R.REPORT);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract JSONObject toJSON();


}
