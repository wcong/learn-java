package org.wcong.test.algorithm.leetcode.string;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Given a 2D board and a list of words from the dictionary,
 * find all words in the board.
 * Each word must be constructed from letters of sequentially adjacent cell,
 * where "adjacent" cells are those horizontally or vertically neighboring.
 * The same letter cell may not be used more than once in a word.
 * For example,
 * Given words = ["oath","pea","eat","rain"] and board =
 * [
 * ['o','a','a','n'],
 * ['e','t','a','e'],
 * ['i','h','k','r'],
 * ['i','f','l','v']
 * ]
 * Return ["eat","oath"].
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 26/05/2017
 */
public class WordSearch2 {

    public List<String> findWords(char[][] board, String[] words) {
        Map<Character, List<int[]>> characterListMap = new HashMap<>();
        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            char[] row = board[rowIndex];
            for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                char column = row[columnIndex];
                List<int[]> indexList = characterListMap.get(column);
                if (indexList == null) {
                    indexList = new LinkedList<>();
                    characterListMap.put(column, indexList);
                }
                int[] index = new int[2];
                index[0] = rowIndex;
                index[1] = columnIndex;
                indexList.add(index);
            }
        }
        List<String> wordList = new LinkedList<>();
        for (String word : words) {
            List<int[]> firstIndexList = characterListMap.get(word.charAt(0));
            if (firstIndexList == null) {
                continue;
            }
            boolean contains = false;
        }
        return wordList;
    }
}
