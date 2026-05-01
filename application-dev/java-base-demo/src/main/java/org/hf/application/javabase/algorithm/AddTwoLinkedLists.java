package org.hf.application.javabase.algorithm;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * 两个单向链表相加
 *      假设单链表中每一个节点的值都在 0 - 9 之间，那么链表整体就可以代表一个整数。
 *      给定两个这种链表，请生成代表两个整数相加值的结果链表。
 *      要求：空间复杂度O(n)，时间复杂度O(n)
 *      例如：链表 1 为 9->3->7，链表 2 为 6->3，最后生成新的结果链表为 1->0->0->0。
 *
 * 实现方式1: 通过双端队列(通过push()和pop()方法可模拟栈的实现)实现
 */
public class AddTwoLinkedLists {

    public static void main(String[] args) {
        // 构造链表1: 9 -> 3 -> 7
        ListNode l1 = new ListNode(9);
        l1.next = new ListNode(3);
        l1.next.next = new ListNode(7);
        // 构造链表2: 6 -> 3
        ListNode l2 = new ListNode(6);
        l2.next = new ListNode(3);
        // 执行相加
        ListNode result = addTwoNumbers(l1, l2);
        // 输出结果链表: 1 -> 0 -> 0 -> 0
        while (result != null) {
            System.out.print(result.val);
            if (result.next != null) {
                System.out.print(" -> ");
            }
            result = result.next;
        }
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Deque<Integer> stack1 = new LinkedList<>();
        Deque<Integer> stack2 = new LinkedList<>();
        // 将链表 l1 的值压入栈
        while (l1 != null) {
            stack1.push(l1.val);
            l1 = l1.next;
        }
        // 将链表 l2 的值压入栈
        while (l2 != null) {
            stack2.push(l2.val);
            l2 = l2.next;
        }
        // 进位
        int carry = 0;
        ListNode head = null;
        // 从栈中取出元素，逐位相加
        while (!stack1.isEmpty() || !stack2.isEmpty() || carry != 0) {
            int num1 = stack1.isEmpty() ? 0 : stack1.pop();
            int num2 = stack2.isEmpty() ? 0 : stack2.pop();
            int sum = num1 + num2 + carry;
            int digit = sum % 10;
            carry = sum / 10;
            // 插入新节点到结果链表的头部
            ListNode newNode = new ListNode(digit);
            newNode.next = head;
            head = newNode;
        }
        return head;
    }

    /**
     * 定义链表节点类
     */
    public static class ListNode {

        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }
}

/**
 * 实现方式2: 通过栈实现
 */
class Demo2 {

    public static void main(String[] args) {
        ListNode node1 = new ListNode(9);
        node1.next = new ListNode(3);
        node1.next.next = new ListNode(7);
        ListNode node2 = new ListNode(6);
        node2.next = new ListNode(3);
        ListNode node = addInList(node1, node2);
        StringBuilder str = null;
        while (node != null) {
            if (str == null) {
                str = new StringBuilder(node.val + "");
            } else {
                str.append(" -> ").append(node.val);
            }
            node = node.next;
        }
        System.out.println(str);
    }


    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * @param head1 ListNode类
     * @param head2 ListNode类
     * @return ListNode类
     */
    public static ListNode addInList(ListNode head1, ListNode head2) {
        // write code here
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        Stack<ListNode> stack1 = buildStack(head1);
        Stack<ListNode> stack2 = buildStack(head2);
        int num = 0;
        ListNode tempNode = null;
        while (!stack1.isEmpty() || !stack2.isEmpty() || num > 0) {
            int num1 = stack1.isEmpty() ? 0 : stack1.pop().val;
            int num2 = stack2.isEmpty() ? 0 : stack2.pop().val;
            int temp = num1 + num2 + num;
            num = temp / 10;
            ListNode newNode = new ListNode(temp % 10);
            newNode.next = tempNode;
            tempNode = newNode;
        }
        return tempNode;
    }

    public static Stack<ListNode> buildStack(ListNode node) {
        Stack<ListNode> stack = new Stack<>();
        while (node != null) {
            stack.push(node);
            node = node.next;
        }
        return stack;
    }

    public static class ListNode {

        int val;
        ListNode next = null;

        public ListNode(int val) {
            this.val = val;
        }
    }
}