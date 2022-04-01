import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ComFunc {
    // LPAD 함수
    String setLPad(String strContext, int iLen, String strChar) {
        String strResult = ""; StringBuilder sbAddChar = new StringBuilder();
        for( int i = strContext.length(); i < iLen; i++ )
        { //
            sbAddChar.append( strChar );
        }
        strResult = sbAddChar + strContext;
        return strResult;
    }

    // 보험종기
    String calInsFs(String insSt,int insTerm) throws ParseException {
        //보험종기 구하기
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        Date tmpInsSt = null;
        Calendar cal = Calendar.getInstance();
        try {
            tmpInsSt = ft.parse(insSt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String result;
        cal.setTime(tmpInsSt);

        cal.add(Calendar.MONTH,insTerm);
        result =  ft.format(cal.getTime());

        return result;
    }
    //파일저장을 위해 문자열로 변환
    String makeContStr(VoCont vo)
    {
        //상품(2) + 계약번호(11) + 보험시기(8) + 보험종기(8) + 계약기간(2) + 계약상태(2) + 총보험료(15) + 계약담보
        String saveCont = new String();
        //파일시스템 길이 맞취기
        String strInsTerm =Integer.toString(vo.getInsTerm());
        String strTotPrm = Double.toString(vo.getTotPrm());
        String strCvrcd = new String();
        //담보저장
        int cvrcdLen = vo.getCvrcd().length;

        for(int i = 0;i < cvrcdLen; ++i)
        {
            if(i == 0) {
                strCvrcd = strCvrcd + (vo.getCvrcd()[i]);
            }
            else {
                strCvrcd = strCvrcd + "|" + (vo.getCvrcd()[i]);
            }
        }
        saveCont = vo.getPlyno() +"|"
                + vo.getInsSt() +"|"
                + vo.getInsFs() +"|"
                + strInsTerm + "|"
                + vo.getContStt() +"|"
                + strTotPrm + "|"
                + strCvrcd;
        return saveCont;
    }
    //코드성 계약 정보 디스플레이
    void getContDetailDisplay(VoCont vo) throws IOException {
        String[] insStCd = new String[]{"01", "02", "03"};
        String[] insStNm = new String[]{"정상계약", "청약철회", "기간만료"};
        File file = null;
        String plyno = vo.getPlyno();
        String gdnm = "";
        if(plyno.substring(0,2).equals("TR")) {
            file = new File("GdTR.txt");
            gdnm = "여행자보험";
        }else if(plyno.substring(0,2).equals("PH")) {
            file = new File("GdPH.txt");
            gdnm = "핸드폰보험";
        }
        else
        {
            System.out.println("증번을 잘못입력했습니다.");
            return;
        }

        String line = "";
        int cnt = vo.getCvrCt();
        String[] strCvrnm = new String[cnt];
        String[] strIsAmt = new String[cnt];

        for(int i = 0;i<cnt;++i) {
            String cont[] = null;

            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tmpCvrcd = vo.getCvrcd()[i];
            while ((line = bfr.readLine()) != null) {

                cont = line.split("\\|");

                if (tmpCvrcd.equals(cont[0])) {
                    //상품(2) + 계약번호(11) + 보험시기(8) + 보험종기(8) + 계약기간(2) + 계약상태(2) + 총보험료(15) + 계약담보
                    strCvrnm[i]= cont[1];
                    strIsAmt[i]= cont[2];
                }
            }

        }
        System.out.println("상품명 :" + gdnm);
        System.out.println("계약번호 : " + vo.getPlyno());
        System.out.println("보험시기 : " + vo.getInsSt());
        System.out.println("보험종기 : " + vo.getInsFs());
        System.out.println("보험기간 : " + vo.getInsTerm() +"개월");
        System.out.println("계약상태 : " + this.findCdNm(insStCd,insStNm,vo.getContStt()));
        System.out.println("담보내역 : " );
        for(int i = 0;i<cnt;++i)
        {
            System.out.println(strCvrnm[i]+ "  가입금액:" +strIsAmt[i] +"원");
        }

        System.out.println("총보험료 :" + vo.getTotPrm());

    }
    //코드배열과 크드명 배열, 코드를 입력받아 코드명을 출력
    String findCdNm(String[] cd,String[] cm,String tgCd)
    {
        int idx = Arrays.asList(cd).indexOf(tgCd);
        return cm[idx];
    }
    //코드배열과 크드명 배열, 코드를 입력받아 코드명을 출력
    Double findCdNmDouble(String[] cd,Double[] cm,String tgCd)
    {
        int idx = Arrays.asList(cd).indexOf(tgCd);
        return cm[idx];
    }
    //보험기간 입력 및 유효성 체크
    int iptInsTerm(VoGd vo,String gdnm)
    {
        //보험기간 입력
        Scanner sc = null;
        int insTerm = 0;
        for(int i = 0; i < 3; i++) //3번까지 보험기간 틀리면종료
        {

            System.out.println("보험기간(월단위)입력하세요 ex) 3개월-->3 입력 ");
            System.out.println(gdnm +"의 보험기간은 최대 "+ vo.getPbsInsTerm() +"개월입니다." );
            try{
                sc = new Scanner(System.in); //exception 후 반복문에서 다시 입력받기 위해 객체 재생성
                insTerm = sc.nextInt();
                if(insTerm <= vo.getPbsInsTerm()||insTerm ==0)
                {
                    return insTerm;
                }
                else
                {
                    System.out.println("보험기간을 확인하고 재입력해주세요");
                }
            }
            catch(Exception e)
            {
                System.out.println("숫자를 입력해주세요");

            }

        }

        return -1;
    }
    //담보세팅 입력받은 담보를 저장을 위해 변수에 세팅
    //vo 에 담보를 세팅함
    int iptCvr(VoGd gVo,VoCont vo)
    {
        //담보선택
        Scanner sc = new Scanner(System.in);
        String[] cvrcd = null;
        System.out.println(vo.getGdnm() +"의 담보중 원하는 담보를 고르세요. 담보종류는 총"+ gVo.getCvrCnt()+"종입니다.");
        System.out.println("반드시 하나이상의 담보를 선택하세요.");


        ArrayList <String> tpCvrcd = new ArrayList<String>();
        ArrayList <String> tpCvrnm = new ArrayList<String>();
        ArrayList <Double> tpIsAmt = new ArrayList<Double>();
        ArrayList <Double> tpStdAmt = new ArrayList<Double>();
        String strCvrnm = "";
        for(int i=0; i< gVo.getCvrCnt();++i)
        {
            System.out.println("담보명:"+ gVo.getCvrnm()[i]+ ",가입금액:" + gVo.getIsAmt()[i]);
            System.out.println("이담보를 선택하시려면 y를 선택안하시려면 아무키나 누르세요");
            String selCvr = sc.next();

            if(selCvr.equals("y")||selCvr.equals("Y"))
            {

                tpCvrcd.add(gVo.getCvrcd()[i]);
                tpCvrnm.add(gVo.getCvrnm()[i]);
                tpIsAmt.add(gVo.getIsAmt()[i]);
                tpStdAmt.add(gVo.getStdAmt()[i]);
                strCvrnm += gVo.getCvrnm()[i] +",";
            }
        }
        int tmpCt = tpCvrcd.size();
        if(tmpCt != 0) {
            vo.setCvrCt(tmpCt);
            vo.setCvrcd(tpCvrcd.toArray(new String[tmpCt]));
            vo.setCvrnm(tpCvrnm.toArray(new String[tmpCt]));
            vo.setIsAmt(tpIsAmt.toArray(new Double[tmpCt]));
            vo.setStdAmt(tpStdAmt.toArray(new Double[tmpCt]));
        }
        return tmpCt;

    }
    //입력값에 따라 상품코드 리턴
    String selGd()
    {
        Scanner sc = new Scanner(System.in);
        String gdcd = "";
        System.out.println("상품을 선택하세요. 1.여행자보험,2.휴대폰보험");
        //상품선탣
        System.out.println("1.여행자보험,2.휴대폰보험");
        int sel2 = sc.nextInt();
        switch(sel2) {
            case 1:
                gdcd = "TR";
                break;
            case 2:
                gdcd = "PH";

                break;
            default:

        }
        return gdcd;
    }
    boolean endPrcs()
    {
        boolean endPrcs = false;
        Scanner sc = new Scanner(System.in);
        System.out.println("프로세스종료는 Y(y), 처음으로 돌아가려면 아무키나 누르시고 엔터");
        String ch = sc.next();
        if(ch.equals("y")||ch.equals("Y"))
        {
            endPrcs = true;
        }
        return endPrcs;
    }
}
