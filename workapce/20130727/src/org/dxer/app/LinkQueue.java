package org.dxer.app;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class LinkQueue {

	// 已访问队列
	private static Set<String> visited = new HashSet<String>();

	// 未访问队列
	private static Queue<String> unVisited = new PriorityQueue<String>();

	/**
	 * 
	 * @return
	 */
	public static Queue<String> getUnVisitedQueue() {
		return unVisited;
	}

	/**
	 * 添加url到已访问队列
	 * 
	 * @param url
	 */
	public static void addVisited(String url) {
		visited.add(url);
	}

	/**
	 * 从已访问队列中删除url
	 * 
	 * @param url
	 */
	public static void removeVisited(String url) {
		visited.remove(url);
	}

	/**
	 * 从未访问队列中获得一条数据,并将该数据从该队列中删除
	 * 
	 * @return
	 */
	public static String getUnVisited() {
		return unVisited.poll();
	}

	/**
	 * 将url添加到未访问队列中
	 * 
	 * @param url
	 */
	public static void addUnVisited(String url) {
		if (url != null && !url.trim().equals("") && !visited.contains(url)
				&& !unVisited.contains(url))
			unVisited.add(url);
	}

	/**
	 * 统计已访问url数量
	 * 
	 * @return
	 */
	public static int getVisitedNum() {
		return visited.size();
	}

	/**
	 * 判断未访问队列是否为空
	 * 
	 * @return
	 */
	public static boolean isUnVisitedEmpty() {
		return unVisited.isEmpty();
	}
}
