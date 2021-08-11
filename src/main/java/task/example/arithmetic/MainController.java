package task.example.arithmetic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;


@Controller
public class MainController {

    @GetMapping("/dashboard")
    public String getInput(Model model) {

        //load object to the template
        model.addAttribute("data", new Params());
        return "dashboard";
    }

    @PostMapping("/dashboard")
    public String postInput(@ModelAttribute Params params, Model model) throws NumberFormatException {
        Integer result1              = null;
        Integer result2              = null;
        Result result                = new Result();
        Integer nSixth               = 6;
        Integer nTh                  = params.getInputN();
        String[] inputStr            = params.getInputArithmetic().split(" ");
        ArrayList<Integer> inputInt  = new ArrayList<>();


        for(int i=0; i<inputStr.length; i++){
            try {
                inputInt.add(Integer.parseInt(inputStr[i]));

            }catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }

        if(inputInt.size() > 1){
            if(checkArithmetic(inputInt)){
                int a = inputInt.get(0);
                int d = inputInt.get(1) - inputInt.get(0);
                result1 = a +(nSixth-1)*d;
                result.setResult1(result1);
                if(nTh != null){
                    result2 = a +(nTh-1)*d;
                    result.setResult2(result2);
                }
            }else if(checkQuadratic(inputInt)){
                processQuadraticSeq(inputInt,result,nTh);
                result.setResult1(result.getResult1());
                result.setResult2(result.getResult2());

            }else if(checkQubic(inputInt)){

                processQubicSeq(inputInt,result,nTh);
            }else{
                return null;
            }
        }else{
            return null;
        }

        result.setnTh(nTh);
        model.addAttribute("result", result);
        return "result";

    }

    public boolean checkArithmetic(ArrayList<Integer> arrInt){
        int x = arrInt.get(1) - arrInt.get(0);
        for(int i=1; i< arrInt.size(); i++)
            if (arrInt.get(i) - arrInt.get(i-1) != x)
            return false;

        return true;
    }

    public boolean checkQuadratic(ArrayList<Integer> arrInt){

        ArrayList<Integer> newSeq = new ArrayList<>();
        newSeq = findDiff(arrInt);

        int x = newSeq.get(1) - newSeq.get(0);
        for(int i=1; i<newSeq.size(); i++)
            if (newSeq.get(i) - newSeq.get(i-1) != x)
                return false;

        return true;
    }

    public boolean checkQubic(ArrayList<Integer> arrInt){

        ArrayList<Integer> secondSeq    = new ArrayList<>();
        ArrayList<Integer> thirdSeq     = new ArrayList<>();
        secondSeq = findDiff(arrInt);
        thirdSeq  = findDiff(secondSeq);

        int x = thirdSeq.get(1) - thirdSeq.get(0);
        for(int i=1; i<thirdSeq.size(); i++)
            if (thirdSeq.get(i) - thirdSeq.get(i-1) != x)
                return false;

        return true;
    }

    public void processQuadraticSeq(ArrayList<Integer> arrInt,Result result,Integer inputN){
        ArrayList<Integer> secondSeq     = new ArrayList<>();
        ArrayList<Integer> d             = new ArrayList<>();
        Integer commonDiff1              = null;
        Integer commonDiff2              = null;
        Integer nSixth                   = 6;

        secondSeq = findDiff(arrInt);
        for(int i=1; i<secondSeq.size(); i++)
            commonDiff1 = (secondSeq.get(i) - secondSeq.get(i-1));

        for(int i=1; i<=arrInt.size(); i++){
            d.add(((commonDiff1/2)*i*i)-arrInt.get(i-1));

        }
        for(int i=1; i<d.size(); i++)
            commonDiff2 = (d.get(i) - d.get(i-1));


        result.setResult1((2*(nSixth*nSixth))+(commonDiff2*nSixth-(commonDiff2-d.get(1))));
        if(inputN != null){
            result.setResult2((2*(inputN*inputN))+(commonDiff2*inputN-(commonDiff2-d.get(1))));
        }
    }
    public void processQubicSeq(ArrayList<Integer> arrInt,Result result,Integer inputN){
        ArrayList<Integer> secondSeq     = new ArrayList<>();
        ArrayList<Integer> thirdSeq      = new ArrayList<>();
        Integer a                        = null;
        Integer b                        = null;
        Integer c                        = null;
        Integer d                        = null;
        Integer commonDif                = null;

        secondSeq = findDiff(arrInt);
        thirdSeq  = findDiff(secondSeq);

        for(int i=1; i<thirdSeq.size(); i++)
            commonDif = (thirdSeq.get(i) - thirdSeq.get(i-1));

        a = findA(commonDif);
        b = findB(a,thirdSeq.get(0));
        c = findC(a,b,secondSeq.get(0));
        d = findD(a,b,c,arrInt.get(0));

        int nSixth = a * (6*6*6) + b*(6*6) + c*6 + d;
        int nTh    = a * (inputN * inputN * inputN) + b*(inputN*inputN) + c*inputN + d;
        result.setResult1(nSixth);
        result.setResult2(nTh);
    }

    public ArrayList<Integer> findDiff(ArrayList<Integer> arrayList){
        ArrayList<Integer> diff = new ArrayList<>();
        for(int i=1; i<arrayList.size(); i++)
            diff.add(arrayList.get(i) - arrayList.get(i-1));

        return diff;

    }
    public Integer findA(Integer x){
        return x/6;
    }
    public Integer findB(Integer a,Integer x){
       int b =x - (12*a);
       return b/2;
    }
    public Integer findC(Integer a, Integer b,Integer x){
        a = 7*a;
        b = 3*b;
        return x-(a+b);
    }

    public Integer findD(Integer a,Integer b,Integer c,Integer x){
        int sum = a+b+c;
        return x-sum;
    }
}
