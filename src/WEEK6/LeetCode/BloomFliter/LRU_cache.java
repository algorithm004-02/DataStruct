package WEEK6.LeetCode.BloomFliter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制。它应该支持以下操作： 获取数据 get 和 写入数据 put 。

 获取数据 get(key) - 如果密钥 (key) 存在于缓存中，则获取密钥的值（总是正数），否则返回 -1。
 写入数据 put(key, value) - 如果密钥不存在，则写入其数据值。当缓存容量达到上限时，它应该在写入新数据之前删除最近最少使用的数据值，从而为新的数据值留出空间。

 进阶:

 你是否可以在 O(1) 时间复杂度内完成这两种操作？

 示例:

 LRUCache cache = new LRUCache( 2 /* 缓存容量 / )
 **/

//        cache.put(1, 1);
//        cache.put(2, 2);
//        cache.get(1);       // 返回  1
//        cache.put(3, 3);    // 该操作会使得密钥 2 作废
//        cache.get(2);       // 返回 -1 (未找到)
//        cache.put(4, 4);    // 该操作会使得密钥 1 作废
//        cache.get(1);       // 返回 -1 (未找到)
//        cache.get(3);       // 返回  3
//        cache.get(4);       // 返回  4
//
//


public class LRU_cache {
    //default
//    public LRU_cache(int capacity) {
//
//    }
//
//    public int get(int key) {
//        return 0;
//    }
//
//    public void put(int key, int value) {
//
//    }
    /**
     * LRUCache 对象会以如下语句构造和调用:
     * LRUCache obj = new LRUCache(capacity);
     * int param_1 = obj.get(key);
     * obj.put(key,value);
     */

    class LRUCache extends LinkedHashMap<Integer, Integer> {
        private int capacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75F, true);
            this.capacity = capacity;
        }

        public int get(int key) {
            return super.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return size() > capacity;
        }
    }

    public class LRUCache2 {

        class DLinkedNode {
            int key;
            int value;
            DLinkedNode prev;
            DLinkedNode next;
        }

        private void addNode(DLinkedNode node) {
            /**
             * Always add the new node right after head.
             */
            node.prev = head;
            node.next = head.next;

            head.next.prev = node;
            head.next = node;
        }

        private void removeNode(DLinkedNode node) {
            /**
             * Remove an existing node from the linked list.
             */
            DLinkedNode prev = node.prev;
            DLinkedNode next = node.next;

            prev.next = next;
            next.prev = prev;
        }

        private void moveToHead(DLinkedNode node) {
            /**
             * Move certain node in between to the head.
             */
            removeNode(node);
            addNode(node);
        }

        private DLinkedNode popTail() {
            /**
             * Pop the current tail.
             */
            DLinkedNode res = tail.prev;
            removeNode(res);
            return res;
        }

        private Hashtable<Integer, DLinkedNode> cache =
                new Hashtable<Integer, DLinkedNode>();
        private int size;
        private int capacity;
        private DLinkedNode head, tail;

        public LRUCache2(int capacity) {
            this.size = 0;
            this.capacity = capacity;

            head = new DLinkedNode();
            // head.prev = null;

            tail = new DLinkedNode();
            // tail.next = null;

            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            DLinkedNode node = cache.get(key);
            if (node == null) return -1;

            // move the accessed node to the head;
            moveToHead(node);

            return node.value;
        }

        public void put(int key, int value) {
            DLinkedNode node = cache.get(key);

            if (node == null) {
                DLinkedNode newNode = new DLinkedNode();
                newNode.key = key;
                newNode.value = value;

                cache.put(key, newNode);
                addNode(newNode);

                ++size;

                if (size > capacity) {
                    // pop the tail
                    DLinkedNode tail = popTail();
                    cache.remove(tail.key);
                    --size;
                }
            } else {
                // update the value.
                node.value = value;
                moveToHead(node);
            }
        }
    }

    public class LRUCache3 {
        private Map<Integer, DLinkNode> cache;
        DLinkNode tail = null;
        DLinkNode head = null;
        int capacity;

        public LRUCache3(int capacity) {
            cache = new HashMap<Integer, DLinkNode>();
            this.capacity = capacity;
        }

        public int get(int key) {
            if (cache.containsKey(key)) {
                DLinkNode target = cache.get(key);
                int value = target.value;
                target.update();
                return value;
            } else return -1;
        }

        public void set(int key, int value) {
            if (cache.containsKey(key)) {
                DLinkNode target = cache.get(key);
                target.value = value;
                target.update();
            } else {
                if (capacity == 0) return;
                if (cache.size() == capacity) {
                    cache.remove(head.key);
                    head.removeFromHead();
                }
                DLinkNode newNode = new DLinkNode(key, value);
                newNode.append();
                cache.put(key, newNode);
            }
        }

        class DLinkNode {
            int key;
            int value;
            DLinkNode left = null;
            DLinkNode right = null;
            public DLinkNode(int key, int value) {
                this.key = key;
                this.value = value;
            }
            // remove head from list and update head reference.
            private void removeFromHead() {
                // if 'this' is the only node, set both head and tail to null.
                if (tail == this) {
                    head = null;
                    tail = null;
                } else {
                    head = this.right;
                    head.left = null;
                }
            }
            private void update() {
                // no need to update if accessing the most revently used value.
                if (tail == this) return;
                else {
                    // remove from current postion and update nodes (if any) on both sides.
                    if (this != head) {
                        this.left.right = this.right;
                    } else {
                        head = this.right;
                    }
                    this.right.left = this.left;
                    // append to tail.
                    this.append();
                }
            }
            private void append() {
                // inserting the first node.
                if (tail == null) {
                    head = this;
                    tail = this;
                    // append as tail and update tail reference.
                } else {
                    this.right = null;
                    this.left = tail;
                    tail.right =this;
                    tail = this;
                }
            }
        }
    }

    public class LRUCache4 {

        public LRUCache4(int capacity) {
            this.count = 0;
            this.capacity = capacity;

            head = new DLinkedNode();
            head.pre = null;

            tail = new DLinkedNode();
            tail.post = null;

            head.post = tail;
            tail.pre = head;
        }

        class DLinkedNode {
            int key;
            int value;
            DLinkedNode pre;
            DLinkedNode post;
        }

        /**
         * Always add the new node right after head;
         */
        private void addNode(DLinkedNode node) {

            node.pre = head;
            node.post = head.post;

            head.post.pre = node;
            head.post = node;
        }

        /**
         * Remove an existing node from the linked list.
         */
        private void removeNode(DLinkedNode node){
            DLinkedNode pre = node.pre;
            DLinkedNode post = node.post;

            pre.post = post;
            post.pre = pre;
        }

        /**
         * Move certain node in between to the head.
         */
        private void moveToHead(DLinkedNode node){
            this.removeNode(node);
            this.addNode(node);
        }

        // pop the current tail.
        private DLinkedNode popTail(){
            DLinkedNode res = tail.pre;
            this.removeNode(res);
            return res;
        }

        private Hashtable<Integer, DLinkedNode>
                cache = new Hashtable<Integer, DLinkedNode>();
        private int count;
        private int capacity;
        private DLinkedNode head, tail;


        public int get(int key) {

            DLinkedNode node = cache.get(key);
            if(node == null){
                return -1; // should raise exception here.
            }

            // move the accessed node to the head;
            this.moveToHead(node);

            return node.value;
        }

        public void put(int key, int value) {
            DLinkedNode node = cache.get(key);

            if(node == null){

                DLinkedNode newNode = new DLinkedNode();
                newNode.key = key;
                newNode.value = value;

                this.cache.put(key, newNode);
                this.addNode(newNode);

                ++count;

                if(count > capacity){
                    // pop the tail
                    DLinkedNode tail = this.popTail();
                    this.cache.remove(tail.key);
                    --count;
                }
            }else{
                // update the value.
                node.value = value;
                this.moveToHead(node);
            }
        }

    }

}
