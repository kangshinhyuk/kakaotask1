import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IsValid {
    //입력받은 보험시기의 유효성 체크
    boolean insDateVal (String arg)
    {
        boolean rtn = false;
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        Date  dt = null;
        Date crrDt = new Date();
        Calendar cal = Calendar.getInstance();

        try {
            ft.setLenient(false);// 날짜유효성체크
            dt = ft.parse(arg);

        }
        catch (ParseException e)
        {
            System.out.println("날짜가 유효하지 않습니다.다시입력해주세요.(3회 오류시 프로세스종료됩니다.)");
            return rtn;
        }
        String crDt =  ft.format(crrDt);
        //현재 날짜보다 작으면 리턴

        if(arg.compareTo(crDt)<0)
        {
            System.out.println("보험시기는 현재날짜보다 커야 합니다.");
            return rtn;
        }
        rtn = true;
        return rtn;
    }
    //담보 갯수체크
    boolean cvrSel(int cnt)
    {
        boolean rlt = false;
        if(cnt==0)
        {
            System.out.println("담보를 선택하지 않았습니다.처음으로 돌아깁니다.");
            return rlt;
        }
        rlt = true;
        return rlt;

    }
}
