

public class VoCont {
    String plyno = null;//증권번호
    String gdcd = null;//상품코드
    String gdnm = null;//상품명
    String insSt =null; //보험시기
    String insFs = null; //보험종기
    int insTerm = 0;
    int cvrCt = 0;
    String[] cvrcd = null; //담보
    String[] cvrnm = null; //담보명
    Double[] isAmt; //가입금액
    Double[] stdAmt; //기준금액
    Double totPrm; //총보험료
    String contStt = null; //계약상태



    //증권번호
    public void setPlyno(String plyno) {
        this.plyno = plyno;
    }
    public String getPlyno(){
        return plyno;
    }
    //보험시기
    public void setInsSt(String insSt) {
        this.insSt = insSt;
    }
    public String getInsSt(){
        return insSt;
    }
    //보험종기
    public void setInsFs(String insFs) {
        this.insFs = insFs;
    }
    public String getInsFs(){
        return insFs;
    }
    //담보갯수
    public void setCvrCt(int cvrCt) {
        this.cvrCt = cvrCt;
    }
    public int getCvrCt(){
        return cvrCt;
    }
    //상품코드
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

    //담보갯수
    public void setInsTerm(int insTerm) {
        this.insTerm = insTerm;
    }
    public int getInsTerm(){
        return insTerm;
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
    //총보험료
    public void setTotPrm(Double totPrm) {
        this.totPrm = totPrm;
    }
    public Double getTotPrm(){
        return totPrm;
    }
    //보험상태
    public void setContStt(String contStt) {
        this.contStt = contStt;
    }
    public String getContStt(){
        return contStt;
    }
}
