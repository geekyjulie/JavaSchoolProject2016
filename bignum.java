/*
 * CSCI6010 Introduction to computer science
 * The George Washington University
 * Project 1: Infinite Precision Arithmetic
 * Author: Julie Yu(JUNG HYUN YU)
 * GWid: G45401691
 * Date: Feb 29 2016
 * My Pledge: 
 * ÒI pledge that this assignment has been completed in compliance with the Graduate Honor Code and that I have neither given nor received any unauthorized aid on this work.
 * Further, I did not use any source codes from any other unauthorized sources, either modified or unmodified.
 * The submitted programming assignment is solely done by me and is my original work.Ó
 */

import java.util.*;
import java.io.*;

public class bignum{
    
    public static void main(String args[]){
        
        System.out.println("Welcome to BigInt calculator!");
        System.out.println();
        try{
        FileReader fr = new FileReader("input.txt");
        BufferedReader bfreader = new BufferedReader(fr);
        
        String read_str;
        while ((read_str = bfreader.readLine()) != null){
            
            System.out.println("Expression: " + read_str);
            String[] str = read_str.split(" ");
            //System.out.println(str);
        
            Stack<String> st_operand = new Stack();
            char oper;
            Stack<ArrayList<Integer>> operand_st = new Stack();
            Stack<ArrayList<Integer>> result_st = new Stack();
        
            String temp;
        
            ArrayList<Integer> operand1 = new ArrayList<>();
            ArrayList<Integer> operand2 = new ArrayList<>();
            ArrayList<Integer> temp_result = new ArrayList<>();
                
            boolean exp_flag = false;
            for (int k=0; k < str.length; k++){
                //System.out.println("operand_St");
                //System.out.println(operand_st);
                //System.out.println("result_st");
                //System.out.println(result_st);
                String current = str[k];
                
                char first_char = current.charAt(0);
                
                //This means it is operand, not operator
                if (first_char != '+' && first_char != '*' && first_char != '^'){
                    //System.out.println("Operand");
                    //System.out.println(current);
                    operand_st.push(putInArray(current));
                }
                
                //This means it is operator.
                else{
                    //System.out.println("Operator");
                    oper=current.charAt(0); //"+", "*", "^"
                    //System.out.println(oper);
                    
                    if (operand_st.size() == 0){
                        if (result_st.size() <= 1){
                            System.out.println("Error! Wrong expression!");
                            break;    
                        }else{
                            operand1 = result_st.pop();
                            operand2 = result_st.pop();
                        }
                    }    
                    
                    else {
                        operand1 = operand_st.pop();
                        if (operand_st.size() == 0){
                            operand2 = result_st.pop();
                        }else{
                            operand2 = operand_st.pop();
                        }
                    }
                    
                    //System.out.println(operand1);
                    //ystem.out.println(operand2);
                    //if operand1.size is 1, then it is for exponential operation
                    if (operand1.size() != 1){
                        
                    int diff;    
                    if (operand1.size() > operand2.size()){
                        diff = operand1.size() - operand2.size();
                        operand2 = makeSameLen(operand2, diff);
                    }else{
                        diff = operand2.size() - operand1.size();
                        operand1 = makeSameLen(operand1,diff);
                    }
                    }
                    
                    switch(oper){
                        case('+'):
                            //System.out.println("Addition");
                            temp_result = addition(operand1, operand2, operand1.size());
                            //System.out.println(temp_result);
                            result_st.push(temp_result);
                            break;
                        case('*'):
                            //System.out.println("Multiplication");
                            temp_result = multi(operand1, operand2, operand1.size());
                            //System.out.println(temp_result);
                            result_st.push(temp_result);
                            break;
                        case('^'):
                            //System.out.println("Exponential");
                            temp_result = expon(operand2, operand1.get(0));
                            //System.out.println(temp_result);
                            result_st.push(temp_result);
                            break;
                        
                    }
                    
                }
            }
            
            ArrayList<Integer> before = result_st.pop();
            int zero_index = 0;
            
            
            for (int i=before.size()-1; i > 0; i--){
                int c = before.get(i);
                if (c != 0){
                    zero_index = i;
                    //System.out.println(zero_index);
                    break;
                }
            }
        
            String after_result="";
            
            for (int t=zero_index; t >= 0; t--){
                
                int tempint = before.get(t);
                //System.out.println(tempint);
                after_result += Integer.toString(tempint);
            }
            System.out.println("The result is: " + after_result);
            System.out.println();
            
        }
        }catch (FileNotFoundException e){
            System.err.println("Unable to find the file");
        } catch (IOException e) {
            System.err.println("Unable to read the file");
        }
        
    }
    
    public static ArrayList<Integer> putInArray(String num){
        
        //System.out.println("Putting into integer array");
        int num_length = num.length();
        boolean flag = false;
        int flagnum=0;
        
        ArrayList arlist = new ArrayList();
        
        //check if the number has multi zeros
        for (int p=0; p < num_length; p++){
            int ele = Character.getNumericValue(num.charAt(p));
            if (ele != 0){
                flag = true;
                flagnum=p;
                break;
            }
        }
        
        for (int i=num_length-1; i >= flagnum; i--){
            int ele = Character.getNumericValue(num.charAt(i));
            arlist.add(ele);
            
        }
        return arlist;
    }
    //make two arrays the same length with filling up with 0s
    public static ArrayList<Integer> makeSameLen(ArrayList<Integer> ar, int diff){
        
        ArrayList<Integer> newar = new ArrayList<>();
        int len = ar.size();
        for (int i=0; i < diff; i++){
            ar.add(0);
        }
        /*
        for (int k=0; k < len; k++){
            newar.add(ar.get(k));
        }*/
        return ar;
    }
    public static ArrayList<Integer> expon(ArrayList<Integer> ar,int num){
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        //System.out.println(temp);
        
        if (num == 0){
            temp.add(1);
        }else{
            temp = ar;
            for (int k=1; k < num; k++){
                temp = multi(temp,ar,ar.size());
                //System.out.println(temp);
            }
        }
        //System.out.println("Result from exponential");
        //System.out.println(temp);
        return temp;
    }
    public static ArrayList<Integer> multi(ArrayList<Integer> ar1, ArrayList<Integer> ar2, int len){
        
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        ArrayList<Integer> first_re = new ArrayList<Integer>();
        ArrayList<Integer> temp_re2 = new ArrayList<Integer>();
        
        temp.add(0);
        temp.add(0); //temp =[0,0]
        
        for (int i=0; i < len; i++){ //ar2
            temp.set(0,0);
            //System.out.println("i:" + Integer.toString(i));
            if (i >= 1){
                //System.out.println(temp_re2);
                for (int k=0; k < i; k++){
                    //System.out.println(k);
                    //System.out.println("Adding first 0's in temp_re2");
                    temp_re2.add(0);
                    //System.out.println(temp_re2);
                }
                //System.out.println("Done with adding 0's!");
            }
            for (int j=0; j < len; j++){ //ar1
                
                int ele = ar2.get(i);
                int num1, num2;
                String multiplied = Integer.toString(ar1.get(j) * ele);
                if (multiplied.length() == 1){
                    num2 = 0;
                    num1 = Character.getNumericValue((multiplied.charAt(0)));
                }else{  
                    num1 = Character.getNumericValue((multiplied.charAt(1)));
                    num2 = Character.getNumericValue((multiplied.charAt(0)));
                }
                /*System.out.println("num1: " +Integer.toString(num1));
                System.out.println("num2: " +Integer.toString(num2));
                System.out.println("/n");
                System.out.println("temp0: " +Integer.toString(temp.get(0)));
                System.out.println("temp1: " +Integer.toString(temp.get(1)));
                System.out.println("/n");*/
                int sum_num1 = temp.get(0) + num1;
                int sum_num2 = temp.get(1) + num2;

                
                //System.out.println(sum_num1);
                if (i==0){
                    if (sum_num1 >= 10){
                        String str = Integer.toString(sum_num1);
                        int lastdigit = Character.getNumericValue(str.charAt(1));
                        first_re.add(lastdigit);
                        num2 += 1;
                    }else {
                        first_re.add(sum_num1);
                    }
                }else{
                    if (sum_num1 >= 10){
                        String str = Integer.toString(sum_num1);
                        if (j == len-1){
                            int lastdigit = Character.getNumericValue(str.charAt(1));
                            temp_re2.add(lastdigit);
                            temp_re2.add(sum_num2+1);
                        }else{
                            int lastdigit = Character.getNumericValue(str.charAt(1));
                            temp_re2.add(lastdigit);
                        }
                        num2 += 1;
                    }else {
                        if (j == len-1){
                            temp_re2.add(sum_num1);
                            temp_re2.add(sum_num2);
                        }else{
                            temp_re2.add(sum_num1);
                        }
                    }
                    

                }
                //System.out.println(temp_re2);
                temp.set(0, num2);
                //System.out.println(temp);
                
            }
            
            
            
            //add first_re + temp_re2
            if (i >= 1){
                
                //first_re.add(0);
                //ArrayList new_ar = new ArrayList<>();
                int diff_len = temp_re2.size() - first_re.size();
                //System.out.println(diff_len);
                for (int p=0; p < diff_len; p++){
                    first_re.add(0);  
                }
                /*System.out.println("first_re");
                System.out.println(first_re);
                System.out.println("temp_re2");
                System.out.println(temp_re2);
                System.out.println("Addition starts betwen first_re and temp_re2");*/
            
                for (int q=0; q < first_re.size(); q++){
                   
                   int sum = first_re.get(q) + temp_re2.get(q);    
                   String str = Integer.toString(sum);
                   //System.out.println("Sum: " + Integer.toString(sum));
                   if (sum >= 10){
                       if (q == first_re.size()-1){
                           
                           int firstdigit = Character.getNumericValue(str.charAt(0));
                           int lastdigit = Character.getNumericValue(str.charAt(1));
                           first_re.set(q,lastdigit);
                           first_re.set(q,firstdigit);
                        } else{
                         first_re.set(q+1, first_re.get(q+1)+1);
                         int lastdigit = Character.getNumericValue(str.charAt(1));
                         first_re.set(q,lastdigit);
                       }
                   }else{
                        
                    first_re.set(q, sum);
                   }
                }
                //System.out.println(first_re);
            }
            temp_re2.clear();   
        }
        //System.out.println("Result from multiplication");
        //System.out.println(first_re);    
        return first_re;
        
    }
    public static ArrayList<Integer> addition(ArrayList<Integer> ar1, ArrayList<Integer> ar2, int len){
        
        ArrayList<Integer> result = new ArrayList<Integer>();
        int length=len;
        int sum;
        
        for (int i=0; i < length; i++){
        
            int num1 = ar1.get(i);
            int num2 = ar2.get(i);
            sum = num1 + num2;
            //System.out.println(num1);
            //System.out.println(num2);
            String sum_to_str = Integer.toString(sum);
            //System.out.println(sum);
            //System.out.println("\n");
            if (sum >= 10){
                int ld = Character.getNumericValue(sum_to_str.charAt(1));
                result.add(ld);
                if (i == length-1){
                    result.add(Character.getNumericValue(sum_to_str.charAt(0)));
                }else{
                    ar1.set(i+1, ar1.get(i+1)+1);
                }
            }
            else{
                int ld = Character.getNumericValue(sum_to_str.charAt(0));
                result.add(ld);
            }
            //System.out.println(result);
        }
        //System.out.println("Result from addition");
        //System.out.println(result);
         return result;
        

    }
    
    
}
