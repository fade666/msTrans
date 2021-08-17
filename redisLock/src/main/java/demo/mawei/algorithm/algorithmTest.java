package demo.mawei.algorithm;

import org.junit.Test;

public class algorithmTest {

    /**
     * 二分查找
     */
    @Test
    public void erfen(){
        //有序队列
        int[] arr = new int[]{1,3,4,5,6,8,9};
        //低位
        int l = 0;
        //高位
        int h = arr.length-1;

        //查询的数据
        int a = 3;

        int mid;
        while(l<=h){
            mid = (l+h)/2;
            if(arr[mid]==a){
                System.out.println(mid+1);
                break;
            }else if(a>arr[mid]){
                l=mid+1;
            }else{
                h=mid-1;
            }
        }

    }


    /**
     * 冒泡排序
     */
    @Test
    public void maopao(){
        int[] arr = new int[]{9,5,3,8,2,1};

        for(int i=0;i<arr.length;i++){
            for(int j=i+1;j<arr.length;j++){
                if(arr[i]>arr[j]){
                    int temp = arr[i];
                    arr[i]=arr[j];
                    arr[j]=temp;
                }
            }
        }

        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]);
        }

    }

    /**
     * 插入排序
     */
    @Test
    public void charu(){
        int[] arr = new int[]{9,5,3,8,2,1};

        for(int i=1;i<=arr.length;i++){

            while (arr[i]<arr[i-1]){

            }

        }
    }

}
