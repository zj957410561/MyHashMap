package Demo1;

import java.util.*;

public class MyHashMap<K, V> implements Map<K, V> {
  Node<K, V>[] table = (Node<K, V>[]) new Node[16]; // 存数据的
  private int size; // 数据长度
  Set<K> keySet;
  Collection<V> values;

  static class Node<K, V> {
    final int hash;
    final K key;
    V value;
    Node<K, V> next;

    Node(int hash, K key, V value, Node<K, V> next) {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  @Override
  public boolean containsValue(Object value) {
    // 判断不为null
    if (this.size > 0 && value != null) {
      // 循环数组
      for (int i = 0; i < this.table.length; i++) {
        // 当前索引的数组里面有数据就进行操作
        if (this.table[i] != null) {
          Node<K, V> node = this.table[i]; // 先拿出数据
          do {
            if (node.value.equals(value)) // 如果相等就直接返回
            {
              return true;
            } else {
              // 如果不相等就判断是否为空 继续下一级找 没有下一级就结束
              node = node.next;
            }
          } while (node != null);
        }
      }
    }
    return false;
  }

  /**
   * key取值
   *
   * @param key
   * @return
   */
  @Override
  public V get(Object key) {
    // 判断不为null
    if (this.size > 0 && key != null) {
      // 循环数组
      for (int i = 0; i < this.table.length; i++) {
        // 当前索引的数组里面有数据就进行操作
        if (this.table[i] != null) {
          Node<K, V> node = this.table[i]; // 先拿出数据
          do {
            if (node.key.equals(key)) // 如果相等就直接返回
            {
              return node.value;
            } else {
              // 如果不相等就判断是否为空 继续下一级找 没有下一级就结束
              node = node.next;
            }
          } while (node != null);
        }
      }
    }
    return null;
  }

  @Override
  public V put(K key, V value) {
    if (key != null && value != null) {
      int ix = key.hashCode() & this.table.length - 1; // 定位到数组中的索引
      if (this.table[ix] == null) {
        this.table[ix] = new Node<>(key.hashCode(), key, value, null);
        this.size++;
        return value;
      } else {
        Node<K, V> node = this.table[ix];
        do {
          // 判断key是否重复
          if (node.key.equals(key)) {
            // key重复 旧的替换为新的
            V tempV = node.value;
            node.value = value;
            return tempV;
          } else {
            if (node.next == null) {
              node.next = new Node<>(key.hashCode(), key, value, null);
              this.size++;
              return value;
            } else {
              node = node.next;
            }
          }
        } while (true);
      }
    }
    return null;
  }

  @Override
  public V remove(Object key) {
    // 1.找到这个key
    if (this.size > 0 && key != null) {
      // 先定位到key
      for (int i = 0; i < this.table.length; i++) {
        Node<K, V> node = table[i];
        if (this.table[i] != null) {
          if (node.key.equals(key)) {
            // 要判断有没有链表下面 不管有没有先存起来在next中
            Node<K, V> next = node.next;
            // 断掉链表
            node.next = null;
            V oldNode = node.value;
            // 将下一个节点前移 给当前数组
            this.table[i] = next;
            size--;
            return oldNode;
          } else {
            while (node.next != null) {
              // 比较链表
              if (node.next.key.equals(key)) {
                Node<K, V> next = node.next.next;
                // 删除指针
                node.next.next = null;
                V oldNode = node.next.value;
                // 在指向指正
                node.next = next;
                size--;
                return oldNode;
              } else {
                node = node.next;
              }
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    // 将map中的数据放进hashmap中
    if (m != null && m.size() > 0) {
      for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
        if (!containsKey(e.getKey())) this.put(e.getKey(), e.getValue());
      }
    }
  }

  @Override
  public void clear() {
    // 官方写法 新建一个数组
    Node<K, V>[] tab;
    // 如果不为空和size不等于0 就给size清空
    if ((tab = table) != null && size > 0) {
      size = 0;
      for (int i = 0; i < tab.length; ++i) {
        tab[i] = null;
      }
    }
  }

  @Override
  public Set<K> keySet() {
    if (keySet == null) {
      keySet = new MyKeySet();
    }
    return keySet;
  }

  private final class MyKeySet extends AbstractSet<K> {

    @Override
    public Iterator<K> iterator() {
      return new keyIterator();
    }

    @Override
    public int size() {
      return MyHashMap.this.size;
    }
  }

  class keyIterator extends MyHashIterator implements Iterator<K> {
    @Override
    public final K next() {
        return nextNode().key;
    }
  }

  abstract class MyHashIterator {
    int indexIterator;
    Node<K, V> temp;

    MyHashIterator() {
      if (MyHashMap.this.size > 0) {
        for (int i = 0; i < MyHashMap.this.table.length; i++) {
          if (MyHashMap.this.table[i] != null) {
            indexIterator = i;
            temp = MyHashMap.this.table[i];
            break;
          }
        }
      }
    }

    public final boolean hasNext() {
      return temp != null;
    }

    Node<K, V> nextNode() {
      Node<K, V> rt = temp;
      if (rt == null) {
        throw new NoSuchElementException();
      }
      if (temp.next != null) {
        temp = temp.next;
      } else {
        for (; indexIterator <= MyHashMap.this.table.length;++indexIterator ) {
          if (indexIterator > MyHashMap.this.table.length - 1) {
            temp = null;
            break;
          }
          if (MyHashMap.this.table[indexIterator] != null) {
            temp = MyHashMap.this.table[indexIterator++];
            break;
          }
        }
      }
      return temp;
    }
  }

  @Override
  public Collection<V> values() {
    return null;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return null;
  }
}
