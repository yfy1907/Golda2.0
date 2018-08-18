package com.example.administrator.goldaapp.bean;

public class UserBean {
    // { 'uid': '42', 'realname': '王晓', 'resideprovince': '', 'residecity': '',
    // 'residedist': '', 'residecommunity': '' }
    private String uid;
    private String realname;
    private String resideprovince;
    private String residecity;
    private String residedist;
    private String residecommunity;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getResideprovince() {
        return resideprovince;
    }

    public void setResideprovince(String resideprovince) {
        this.resideprovince = resideprovince;
    }

    public String getResidecity() {
        return residecity;
    }

    public void setResidecity(String residecity) {
        this.residecity = residecity;
    }

    public String getResidedist() {
        return residedist;
    }

    public void setResidedist(String residedist) {
        this.residedist = residedist;
    }

    public String getResidecommunity() {
        return residecommunity;
    }

    public void setResidecommunity(String residecommunity) {
        this.residecommunity = residecommunity;
    }

    @Override
    public String toString() {
        return "UserBean [uid=" + uid + ", realname=" + realname
                + ", resideprovince=" + resideprovince + ", residecity="
                + residecity + ", residedist=" + residedist
                + ", residecommunity=" + residecommunity + "]";
    }

}
