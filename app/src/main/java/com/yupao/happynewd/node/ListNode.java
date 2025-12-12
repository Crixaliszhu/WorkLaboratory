package com.yupao.happynewd.node;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode left;
    public ListNode right;


    public ListNode(int s) {
        this.val = s;
        this.next = null;
    }

    public ListNode(int s, ListNode next) {
        this.val = s;
        this.next = next;
    }

    public ListNode(int s, ListNode l, ListNode r) {
        this.val = s;
        this.left = l;
        this.right = r;
    }
}
