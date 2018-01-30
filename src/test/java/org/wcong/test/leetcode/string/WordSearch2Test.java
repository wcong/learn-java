package org.wcong.test.leetcode.string;

import org.junit.Assert;
import org.junit.Test;
import org.wcong.test.algorithm.leetcode.string.WordSearch2;

import java.util.List;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 26/05/2017
 */
public class WordSearch2Test {

    @Test
    public void testWordSearch2() {
        char[][] board = new char[][]{{'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}};
        WordSearch2 wordSearch2 = new WordSearch2();
        List<String> result = wordSearch2.findWords(board, new String[]{"oath", "pea", "eat", "rain"});
        Assert.assertTrue(result.get(0).equals("oath"));
        Assert.assertTrue(result.get(1).equals("eat"));
    }

}
