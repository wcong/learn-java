package org.wcong.test.pattern.creational;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/1/2
 */
public class Multition {

	public static void main(String[] args){
		int[] nums = {1,3};
		System.out.println(index(0,nums.length,nums,0));
	}

	private static int index(int start,int end,int[] nums,int target){
		if( start == end ){
			if( nums[start] == target ){
				return start;
			}else if( nums[start] > target ){
				return start;
			}else{
				return start +1;
			}
		}
		int middle = start + (end - start)/2;
		if( nums[middle] == target ){
			return middle;
		}
		return index(start+1>middle?middle:start+1,end -1,nums,target);
	}

}
