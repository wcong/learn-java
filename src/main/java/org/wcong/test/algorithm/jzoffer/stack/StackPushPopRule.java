package org.wcong.test.algorithm.jzoffer.stack;

import org.springframework.util.Assert;

/**
 * test for stack operation
 * give two array of integer,first is the order of push,judge second is the one of the order of pop
 * for example push array 1,2,3,4,5 and pop order 4,5,3,2,1 is correct but 4,3,5,1,2 is not
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2017/3/26
 */
public class StackPushPopRule {

    public static void main(String[] args) {
        Assert.isTrue(isPopToPush(new int[]{1, 2, 3, 4, 5}, new int[]{4, 5, 3, 2, 1}));
        Assert.isTrue(!isPopToPush(new int[]{1, 2, 3, 4, 5}, new int[]{4, 3, 5, 1, 2}));
    }

    public static boolean isPopToPush(int[] pushArray, int[] popArray) {
        if (pushArray == null || popArray == null) {
            return false;
        }
        int index = -1;
        for (int push : pushArray) {
            index += 1;
            if (push == popArray[0]) {
                break;
            }
        }
        if (index < 0) {
            return false;
        }
        int left = index - 1, right = index + 1;
        for (int i = 1; i < popArray.length; i++) {
            if (left >= 0 && pushArray[left] == popArray[i]) {
                left -= 1;
                continue;
            }
            if (right < pushArray.length && pushArray[right] == popArray[i]) {
                right += 1;
                continue;
            }
            return false;
        }
        return true;
    }

}
