import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.BufferedReader;

//import static com.sun.tools.javac.util.Constants.format;

public class ContApi {
    /*
       계약생성API
       계약번호 생성, 보험종기생성, 보험료 계산 후 저장
      */
    public boolean setCont(VoCont vo) throws ParseException, IOException {
        ComFunc cf = new ComFunc();
        String plyno = null;
        //계약번호 만들기 위한 임시변수
        String plyGd = null;
        String yyyyMm = null;
        String rdNum = null;
        /********************************************************************************/
        //계약번호 생성
        //계약번호는 상품+년월+5자리 난수로 만들어짐
        //여행자보험 TR, 휴대폰보험 PH + 년월 + 10자리난수
        plyGd = vo.getGdcd();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        yyyyMm = dateFormat.format(cal.getTime()).substring(0, 6);
        boolean chkPlyno = true; //증번 체크 난수로 생성된 게약번호(증권번호)가 같으면 재생성 틀릴때까지 제생성
        int count = 0; //1000번까지 생성했다가 계속 같은증번이 나오면 에러처리
        while (chkPlyno) //계약번호 생성 동일계약번호 생성시 제생성
        {
            chkPlyno = false;
            ++count;
            if (count == 100) {
                return false; //최대 100번까지만 루핑
            }
            String[] cont = null;
            File file = new File("Cont.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);
            String line = "";
            Random cRd = new Random();
            int rd = cRd.nextInt(99998) + 1;
            String strLPad = cf.setLPad(Integer.toString(rd), 5, "0"); // lpad
            plyno = plyGd + yyyyMm + strLPad;

            while ((line = bfr.readLine()) != null && !chkPlyno) {
                cont = line.split("\\|");
                if (plyno.equals(cont[0])) {
                    chkPlyno = true;
                }
            }

        }
        vo.setPlyno(plyno);

        /********************************************************************************/
        //보험기간구하기
        int tmpTerm = vo.getInsTerm();
        vo.setInsFs(cf.calInsFs(vo.getInsSt(), tmpTerm)); //보험종기 가져오기
        /********************************************************************************/


        /********************************************************************************/
        //계약상태
        vo.setContStt("01"); //정상
        /********************************************************************************/
        //총보험료 구하기
        double tmpPrm = this.calTotPrm(vo);
        vo.setTotPrm(tmpPrm);
        /********************************************************************************/
        //파일시스템에 저장

        String saveCont = cf.makeContStr(vo);//파일에 저장하기 위해 문자열로 변경

        FileWriter fw;
        try {
            fw = new FileWriter("Cont.txt", true); // 파일이 있을경우 이어쓰기
            fw.write(saveCont + "\n");
            fw.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        /**************************************************************************************/
        //계약사항 display
        cf.getContDetailDisplay(vo);
        return true;
    }
    /*
    계약변경API
    담보추가,변경,삭제, 보험기간, 상태변경가능
   */
    public boolean inquiryCont(VoCont vo){
        boolean rslt = false;
        try{
            String[] cont = null;
            ComFunc cf = new ComFunc();
            File file = new File("Cont.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);
            String line = "";
            String plyno = vo.getPlyno();
            GdInfo gi = new GdInfo();
            VoGd vGd = new VoGd();
            vGd.setGdcd(plyno.substring(0,2));
            vGd = gi.inquiryGdInFo(vGd);


            while((line = bfr.readLine())!= null)
            {

                cont = line.split("\\|");
                int cvrCnt = cont.length -6; //전체컬럼중 담보시작위치계산
                if(plyno.equals(cont[0]))
                {
                    //상품(2) + 계약번호(11) + 보험시기(8) + 보험종기(8) + 계약기간(2) + 계약상태(2) + 총보험료(15) + 계약담보

                    vo.setInsSt(cont[1]);
                    vo.setInsFs(cont[2]);
                    vo.setInsTerm(Integer.parseInt(cont[3]));
                    vo.setContStt(cont[4]);
                    vo.setTotPrm(Double.parseDouble(cont[5]));
                    //  System.out.println("lng:"+cont.length);
                    String[] tmpCvrcd = new String[cvrCnt];
                    Double[] tmpIsAmt = new Double[cvrCnt];
                    Double[] tmpStdAmt = new Double[cvrCnt];
                    for(int i = 0;i < cvrCnt;++i)
                    {
                        tmpCvrcd[i] = cont[i+6];
                        tmpIsAmt[i] = cf.findCdNmDouble(vGd.getCvrcd(),vGd.getIsAmt() ,cont[i+6]);
                        tmpStdAmt[i] = cf.findCdNmDouble(vGd.getCvrcd(),vGd.getStdAmt() ,cont[i+6]);
                    }
                    vo.setCvrCt(cvrCnt);
                    vo.setCvrcd(tmpCvrcd);
                    vo.setIsAmt(tmpIsAmt);
                    vo.setStdAmt(tmpStdAmt);

                }
            }

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException  e){

        }
        return rslt;
    }
    /*
     계약변경API
     담보추가,변경,삭제, 보험기간, 상태변경가능
    */
    public boolean updateCont(VoCont vo) {
        boolean rslt = false;
        FileWriter fw;
        try {
            String[] cont = null;
            File file = new File("Cont.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            //임시로 담을 리스트
            List<String> tmpList = new ArrayList<String>();
            String plyno = vo.getPlyno();
            ComFunc cf = new ComFunc();

            /********************************************************************************/
            //보험종기구하기
            int tmpTerm = vo.getInsTerm();
            vo.setInsFs( cf.calInsFs(vo.getInsSt(),tmpTerm)); //보험종기 가져오기
            /********************************************************************************/
            //총보험료 구하기
            double tmpPrm = this.calTotPrm(vo);
            vo.setTotPrm(tmpPrm);

            String chgCont = cf.makeContStr(vo);//파일에 저장하기 위해 문자열로 변경

            String line = "";
            while ((line = bfr.readLine()) != null) {
                cont = line.split("\\|");

                if (plyno.equals(cont[0])) {
                    tmpList.add(chgCont);

                    rslt = true;
                } else {
                    tmpList.add(line);
                }
            }
            if(!rslt)
            {
                return rslt;
            }

            FileWriter nfr = new FileWriter(file);
            BufferedWriter nbfr = new BufferedWriter(nfr);
            for (String nLine: tmpList) {

                nbfr.write(nLine);
                nbfr.newLine();
            }
            nbfr.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rslt;
    }
    /* 
     보험료계산API
     상품코드 ,담보코드, 보험기간으로 보험료 계산
    */
    double calTotPrm(VoCont vo)
    {
        /********************************************************************************/
        //총보험료 구하기
        double tmpPrm = 0.0;
        GdInfo cV = new GdInfo();
        int insTerm = vo.getInsTerm();//보험기간
        int cvrcdLen = vo.getCvrCt();

        for(int i = 0;i < cvrcdLen; ++i)
        {
            //보험료 = 납입기간*가입금액/기준금액)
            tmpPrm += insTerm * vo.getIsAmt()[i] / vo.getStdAmt()[i];
        }

        tmpPrm = Math.floor(tmpPrm*100)/100; //소수3번째 자리서 절사
        return tmpPrm;
    }
    /* 
     만기안내장 발송 
     현재날짜 일주일전 건을 찾아 만기 안내장을 발송함
     만기건이 없으면 종료
    */
    public boolean endPaparSend() {
        boolean rslt = false;
        FileWriter fw;
        try {
            String[] cont = null;
            File file = new File("Cont.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            //임시로 담을 리스트
            List<String> tmpList = new ArrayList<String>();
            Date today = new Date();
            String line = "";
            Calendar cal = Calendar.getInstance();

            int cnt = 0;
            while ((line = bfr.readLine()) != null) {
                cont = line.split("\\|");

                ComFunc cf = new ComFunc();
                SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
                Date dt = ft.parse(cont[2]);
                cal.setTime(dt);
                cal.add(Calendar.DATE,7);



                if(ft.format(today).compareTo(ft.format(cal.getTime()))==0)
                {
                    ++cnt;
                    rslt = true;
                    System.out.println("만기안내입니다.");
                    int cvrCnt = cont.length -6; //전체컬럼중 담보시작위치계산
                    VoCont vo = new VoCont();
                    vo.setPlyno(cont[0]);
                    vo.setInsSt(cont[1]);
                    vo.setInsFs(cont[2]);
                    vo.setInsTerm(Integer.parseInt(cont[3]));
                    vo.setContStt(cont[4]);
                    vo.setTotPrm(Double.parseDouble(cont[5]));
                    //  System.out.println("lng:"+cont.length);
                    String[] tmpStr = new String[cvrCnt];
                    for(int i = 0;i < cvrCnt;++i)
                    {
                        tmpStr[i] = cont[i+6];
                    }
                    vo.setCvrCt(cvrCnt);
                    vo.setCvrcd(tmpStr);
                    cf.getContDetailDisplay(vo);
                    System.out.println("곧만기됨으로 갱신하시기바랍니다.");
                }
            }

            if(cnt == 0)
            {
                return rslt;
            }

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rslt;
    }
}
