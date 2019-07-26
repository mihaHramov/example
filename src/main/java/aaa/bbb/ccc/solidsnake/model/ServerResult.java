package aaa.bbb.ccc.solidsnake.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerResult {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("result")
    @Expose
    private String result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
