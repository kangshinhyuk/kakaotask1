import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class ContAdmin {
    public static void main(String[] args) throws ParseException, IOException {
        VoCont vo = null; //계약정보 Vo
        VoGd vGd = null; //상품정보 Vo
        GdInfo gi = new GdInfo(); //상품정보class
        IsValid iv = new IsValid();
        ComFunc cf = new ComFunc();//공통함슈 정리
        ContApi cApi = new ContApi();
        Scanner sc = new Scanner(System.in);

        String iptPlyno = "";
        boolean processEnd = false;
        String[] arrGdcd = new String[]{"TR", "PH"};
        String[] arrGdnm = new String[]{"여행자보험", "휴대폰보험"};
        while(!processEnd)
        {
            System.out.println("카카오보험 계약\n");
            System.out.println("1:계약생성 2:계약상세조회 3:계약변경, 4.간단보험료산출 , 5.상품담보변경  6.만기안내장 발송,9.종료");
            int sel1 = sc.nextInt();
            switch(sel1) {
                case 1: //계약생성
                    vo =  new VoCont();
                    vGd  = new VoGd();
                    String gdcd = "";
                    String gdnm = "";
                    System.out.println("!!!!!!!!!!계약 생성!!!!!!!!!");
                    gdcd = cf.selGd();
                    vGd.setGdcd(gdcd);
                    gdnm = cf.findCdNm(arrGdcd,arrGdnm,gdcd);
                    vGd = gi.inquiryGdInFo(vGd);
                    vo.setGdcd(gdcd);
                    vo.setGdnm(gdnm);
                    //보험시기 입력
                    System.out.println("보험시기를 선택해주세요");
                    String insSt = "";
                    for(int i = 0; i < 3; i++) //3번까지 날짜 틀리면 프로세스 종료
                    {
                        insSt = sc.next();
                        processEnd = true;
                        if(iv.insDateVal(insSt))
                        {
                            processEnd = false;
                            break;
                        }
                    }
                    if(processEnd)
                    {
                        System.out.println("종료합니다.");
                        return;
                    }
                    vo.setInsSt(insSt);
                    //보험기간 입력
                    vo.setInsTerm(cf.iptInsTerm(vGd,gdnm));

                    //담보선택 cvrSel:담보 하나 이상 있어야함  iptCvr:담보선택
                    if(iv.cvrSel(cf.iptCvr(vGd,vo)))
                    {
                        //계약생성
                        cApi.setCont(vo);
                    };


                    break;
                case 2://계약조회
                    vo =  new VoCont();

                    System.out.println("!!!!!!!계약상세조회!!!!!!!!!!!!");
                    System.out.println("계약번호를 입력하세요 ex)PH20220361706");
                    iptPlyno = sc.next();
                    int j=0;
                    while (iptPlyno.length() != 13)
                    {
                        ++j;
                        System.out.println("계약번호오류 13자리를 입력해주세요");
                        iptPlyno = sc.next();

                        if(j==10)
                            return;
                    }
                    vo.setPlyno(iptPlyno);
                    cApi.inquiryCont(vo);
                    cf.getContDetailDisplay(vo);
                    break;
                case 3://계약변경
                    vo =  new VoCont();
                    vGd  = new VoGd();
                    System.out.println("!!!!!!!계약변경!!!!!!!!!!!!");
                    System.out.println("계약번호를 입력하세요 ex)PH20220361706");
                    iptPlyno = sc.next();
                    j=0;
                    while (iptPlyno.length() != 13)
                    {
                        ++j;
                        System.out.println("계약번호오류 13자리를 입력해주세요");
                        iptPlyno = sc.next();

                        if(j==10)
                            return;
                    }
                    vo.setPlyno(iptPlyno);

                    System.out.println(iptPlyno.substring(0,2));
                    if(iptPlyno.substring(0,2).equals("TR"))
                    {
                        vo.setGdcd("TR");

                    }
                    else if(iptPlyno.substring(0,2).equals("PH"))
                    {
                        vo.setGdcd("PH");
                    }
                    vGd.setGdcd(vo.getGdcd());
                    vo.setGdnm(cf.findCdNm(arrGdcd,arrGdnm,vo.getGdcd()));   //상품코드로 상품명가져오기
                    vGd = gi.inquiryGdInFo(vGd);


                    cApi.inquiryCont(vo);
                    cf.getContDetailDisplay(vo);
                    boolean chgPcsEnd = false;
                    System.out.println("변경하고 싶은 항목을 선택하세요.모든변경을 한후 4번을 눌러야 최종 반영됩니다.");
                    while(!chgPcsEnd) {
                        System.out.println("1.담보추가삭제,2.계약기간변경,3.계약상태변경,4.계약변경후종료,9.종료");
                        int sel = sc.nextInt();
                        switch (sel) {
                            case 1:
                                //담보선택   iptCvr:담보선택
                                if(!iv.cvrSel(cf.iptCvr(vGd,vo)))
                                {
                                    chgPcsEnd = true;
                                }

                                break;
                            case 2:
                                //보험기간 입력
                                vo.setInsTerm(cf.iptInsTerm(vGd,vo.getGdnm()));

                                break;
                            case 3:
                                //계약상태변경
                                if(vo.getContStt().equals("03"))
                                {
                                    System.out.println("기간만료상태임으로 변경불가힙니다.");
                                }
                                else
                                {
                                    System.out.println("계약상태변경(번호를 입력해주세요)");
                                    System.out.println("1.정상 2.청약철회 3.기간만료");
                                    int selNum = sc.nextInt();
                                    switch (selNum)
                                    {
                                        case 1:
                                            vo.setContStt("01");
                                            break;
                                        case 2:
                                            vo.setContStt("02");
                                            break;
                                        case 3:
                                            vo.setContStt("03");
                                            break;
                                        default:
                                            System.out.println("계약상태변경이 없습니다.");
                                    }
                                }

                                break;
                            case 4:
                                cApi.updateCont(vo);
                                System.out.println("계약내용이 변경되었습니다.");
                                System.out.println("변경된 계약상세를 조회합니다.");
                                vo.setPlyno(iptPlyno);
                                cApi.inquiryCont(vo);
                                cf.getContDetailDisplay(vo);

                                chgPcsEnd = true;
                                break;
                            default:
                                chgPcsEnd = true;
                        }
                    }
                    break;
                case 4:
                    System.out.println("!!!!!간단보험료산출!!!!!!");
                    System.out.println("!!!!!상품을 선택해주세요!!!!!");
                    gdcd = cf.selGd();         //사용자로부터 상품입력받기
                    vGd.setGdcd(gdcd);
                    gdnm = cf.findCdNm(arrGdcd,arrGdnm,gdcd);
                    vGd = gi.inquiryGdInFo(vGd);
                    vo.setGdcd(gdcd);
                    vo.setGdnm(gdnm);
                    //보험기간 입력
                    vo.setInsTerm(cf.iptInsTerm(vGd,gdnm));

                    //담보선택 cvrSel:담보 하나 이상 있어야함  iptCvr:담보선택
                    if(iv.cvrSel(cf.iptCvr(vGd,vo)))
                    {
                        //보험료산출
                        System.out.println("총보험료는:" + cApi.calTotPrm(vo) +"원입니다.");
                    };
                    break;
                case 5://상품담보변경
                    System.out.println("상품담보추가,삭제,변경");
                    gdcd = cf.selGd();
                    System.out.println(cf.findCdNm(arrGdcd,arrGdnm,vGd.getGdcd()) + "의 담보정보입니다.");
                    gi.addGdCvr(gdcd);
                    break;
                case 6: //만기발송
                    if(!cApi.endPaparSend())
                    {
                        System.out.println("만기대상건이 없습니다.");
                    }
                    break;
                default:

            }
            processEnd = cf.endPrcs();
        }

    }


}
