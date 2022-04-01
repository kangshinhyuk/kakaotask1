public class VoGd {

    String gdcd = null;//상품코드
    String gdnm = null;//상품명
    int pbsInsTerm; //가입가능한 보험기간
    int cvrCnt = 0; //담보갯수
    String[] cvrcd = null; //담보
    String[] cvrnm = null; //담보명
    Double[] isAmt; //가입금액
    Double[] stdAmt; //기준금액


    //상품명
    public void setGdcd(String gdcd) {
        this.gdcd = gdcd;
    }
    public String getGdcd(){
        return gdcd;
    }
    //상품명
    public void setGdnm(String gdnm) {
        this.gdnm = gdnm;
    }
    public String getGdnm(){
        return gdnm;
    }
    //가입가능한 보험기간
    public void setPbsInsTerm(int pbsInsTerm) {
        this.pbsInsTerm = pbsInsTerm;
    }
    public int getPbsInsTerm() {
        return pbsInsTerm;
    }

    //담보갯수
    public void setCvrCnt(int cvrCnt) {
        this.cvrCnt = cvrCnt;
    }
    public int getCvrCnt(){
        return cvrCnt;
    }
    //담보코드
    public void setCvrcd(String[] cvrcd) {
        this.cvrcd = cvrcd;
    }
    public String[] getCvrcd(){
        return cvrcd;
    }
    //담보명
    public void setCvrnm(String[] cvrnm) {
        this.cvrnm = cvrnm;
    }
    public String[] getCvrnm(){
        return cvrnm;
    }
    //가입금액
    public void setIsAmt(Double[] isAmt) {
        this.isAmt = isAmt;
    }
    public Double[] getIsAmt(){
        return isAmt;
    }
    //기준금액
    public void setStdAmt(Double[] stdAmt) {
        this.stdAmt = stdAmt;
    }
    public Double[] getStdAmt(){
        return stdAmt;
    }

}
