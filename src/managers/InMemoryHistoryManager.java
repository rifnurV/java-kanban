package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    public class Node<T> {

        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            Node<Task> node = historyMap.get(task.getId());
            removeNode(node);
        }
        linkLast(task);
        Node<Task> newNode = tail;
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            Node<Task> node = historyMap.get(id);
            removeNode(node);
        }

    }

    private void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> curNode = head;
        while (curNode != null) {
            if (curNode.data != null) {
                tasks.add(curNode.data);
            }
            curNode = curNode.next;
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node == head) {
            head = node.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
        } else if (node == tail) {
            tail = node.prev;
            if (tail != null) {
                tail.next = null;
            } else {
                head = null;
            }
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        historyMap.remove(node.data.getId());
        size--;
    }

}
