import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GdInfo {
    //저장된 상품정보를 가져오는 class

    public VoGd inquiryGdInFo(VoGd vo) {
        boolean rslt = false;
        try {
            String[] cont = null;
            ComFunc cf = new ComFunc();

            File file = null;
            if (vo.getGdcd().equals("TR")) //여행자보험 상품파일
            {
                file = new File("GdTR.txt");
            } else if (vo.getGdcd().equals("PH")) //핸드폰보험 상품파일
            {
                file = new File("GdPH.txt");
            }

            FileReader fr1 = new FileReader(file);
            BufferedReader lfr = new BufferedReader(fr1);
            int lnCt = 0;
            while ( lfr.readLine() != null) {
                lnCt ++;
            }

            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = "";
            String tmpCvrcd[] = new String[lnCt];
            String tmpCvrnm[] = new String[lnCt];
            Double tmpIsAmt[] = new Double[lnCt];
            Double tmpStdAmt[] = new Double[lnCt];


            int i = 0;
            while ((line = bfr.readLine()) != null) {
                if (i == 0) { //상품 첫번째 행은 보험가입가능월
                    vo.setPbsInsTerm(Integer.parseInt(line)); //상품파일의 첫번째 행은 반드시 보험가입가능 월 (숫자타입 픽스)
                } else //두번째 줄부터 담보
                {
                    cont = null;
                    cont = line.split("\\|");

                    tmpCvrcd[i - 1] = cont[0]; //담보코드
                    tmpCvrnm[i - 1] = cont[1]; //담보명
                    tmpIsAmt[i - 1] = Double.parseDouble(cont[2]); //가입금액
                    tmpStdAmt[i - 1] = Double.parseDouble(cont[3]); //기준금액
                }
                ++i;
            }
            vo.setCvrCnt(i - 1);
            vo.setCvrnm(tmpCvrnm);
            vo.setCvrcd(tmpCvrcd);
            vo.setIsAmt(tmpIsAmt);
            vo.setStdAmt(tmpStdAmt);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
        return vo;
    }
    //상품정보 추가 삭제 API
    boolean addGdCvr(String gdcd)
    {
        boolean rlt = false;
        VoGd vGd  = new VoGd();
        ComFunc cf = new ComFunc();
        Scanner sc = new Scanner(System.in);
        System.out.println("상품담보추가,삭제,변경");
        vGd.setGdcd(gdcd); //사용자로부터 상품입력받기
        vGd = this.inquiryGdInFo(vGd);
        String fn = "";
        if(vGd.getGdcd().equals("TR"))
        {
            fn = "GdTR.txt";
        }else if(vGd.getGdcd().equals("PH"))
        {
            fn = "GdPH.txt";
        }
        else
        {
            return rlt;
        }

        for(int i = 0; i < vGd.getCvrCnt();++i)
        {
            System.out.println("담보코드:"+vGd.getCvrcd()[i]+"담보명:"+vGd.getCvrnm()[i] + ",가입금액:"+vGd.getIsAmt()[i]+",기준금액:"+vGd.getStdAmt()[i]);
        }
        System.out.println("1.담보추가,2.담보삭제,3.종료");
        int selNum = sc.nextInt();
//        FileWriter fw;
        BufferedWriter fw;
        switch (selNum) {
            case 1:
                System.out.println("담보코드를 입력해주세요(기존담보코드와 중복은 허용하지 않습니다)");
                String tmpStr = sc.next() + "|";
                System.out.println("담보명을 입력해주세요");
                tmpStr += sc.next() + "|";
                System.out.println("가입금액을 입력해주세요");
                tmpStr += sc.next() + "|";
                System.out.println("기준금액을 입력해주세요");
                tmpStr += sc.next();

                try {
//                    fw = new FileWriter(fn, true); // 파일이 있을경우 이어쓰기
                    fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn, true), "UTF-8"));
                    fw.write(tmpStr + "\n");
                    fw.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case 2:
                System.out.println("삭제할 담보코드 입력하세요");
                String tmpCvrcd = sc.next();

                try {
                    String[] cont = null;
//                    File file = new File(fn);
//                    FileReader fr = new FileReader(file);
//                    BufferedReader bfr = new BufferedReader(fr);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(fn), "UTF-8"));
                    //임시로 담을 리스트
                    List<String> tmpList = new ArrayList<String>();
                    String line = "";
                    while ((line = bfr.readLine()) != null) {
                        cont = line.split("\\|");

                        if (!tmpCvrcd.equals(cont[0])) {
                            tmpList.add(line);
                        }
                    }

//                    FileWriter nfr = new FileWriter(file);
//                    BufferedWriter nbfr = new BufferedWriter(nfr);
                    BufferedWriter nbfr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn), "UTF-8"));
                    for (String nLine: tmpList) {

                        nbfr.write(nLine);
                        nbfr.newLine();
                    }
                    nbfr.close();

                } catch (FileNotFoundException e) {

                } catch (IOException e) {

                }
                break;
            case 3:
                break;

            default:
        }
        rlt = true;
        return rlt;
    }
}
