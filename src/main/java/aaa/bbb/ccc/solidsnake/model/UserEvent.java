package aaa.bbb.ccc.solidsnake.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEvent {
    @SerializedName("goal")
    @Expose
    String goal;
    @SerializedName("offerid")
    @Expose
    String offerid;

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Double getDepsum() {
        return depsum;
    }

    public void setDepsum(Double depsum) {
        this.depsum = depsum;
    }

    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("currency")
    @Expose
    String currency;

    @SerializedName("pid")
    @Expose
    String pid;

    @SerializedName("sum")
    @Expose
    Double sum;

    @SerializedName("depsum")
    @Expose
    Double depsum;


}
