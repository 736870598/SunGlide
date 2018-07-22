package com.sunxy.sunglide;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void getHashCode(){
        String str = "abc";
        System.out.println(str.hashCode());
        String a = "ab";
        a+="c";
        System.out.println(a.hashCode() + ", " + (str.hashCode() == a.hashCode()));
    }



}