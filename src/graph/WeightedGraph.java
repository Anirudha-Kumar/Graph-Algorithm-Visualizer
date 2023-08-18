package graph;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList; 

public class WeightedGraph<V> {

	private Map<V, Map<V, Integer>> graph; // hashMap

	private Collection<GraphAlgorithmObserver<V>> observerList; // hashSet

	public WeightedGraph() {

		this.graph = new HashMap<>();
		this.observerList = new HashSet<>();
	}


	public void addObserver(GraphAlgorithmObserver<V> observer) {

		this.observerList.add(observer);
	}


	public void addVertex(V vertex) {

		if (graph.containsKey(vertex)) {

			throw new IllegalArgumentException();
		} else {

			graph.put(vertex, new HashMap<>());
		}
	}


	public boolean containsVertex(V vertex) {

		boolean check = false;

		for (V value : this.graph.keySet()) {

			if (value.equals(vertex)) {

				check = true;
				break;
			}
		}

		return check;
	}

	public void addEdge(V from, V to, Integer weight) {

		if (!(graph.containsKey(from) || graph.containsKey(to)) || weight < 0) {

			throw new IllegalArgumentException();
		} else {

			Map<V, Integer> tempMap = graph.get(from);
			tempMap.put(to, weight);
		}
	}

	public Integer getWeight(V from, V to) {

		if (!(graph.containsKey(from)) || !(graph.containsKey(to))) {

			throw new IllegalArgumentException();
		}

		Map<V, Integer> tempMap = graph.get(from);

		boolean check = tempMap.containsKey(to);

		if (!check) {

			return null;
		} else {

			return tempMap.get(to);
		}

	}

	public void DoBFS(V start, V end) {

		Set<V> visitedSet = new HashSet<>();
		Queue<V> queue = new LinkedList<>();

		for (GraphAlgorithmObserver<V> value : observerList) { // before

			value.notifyBFSHasBegun();
		}

		queue.add(start);

		while (!queue.isEmpty()) { // checks to see if queue is not empty 

			V temp = queue.remove();

			if (temp.equals(end)) {

				for (GraphAlgorithmObserver<V> value : observerList) {

					value.notifySearchIsOver(); // after
				}
				break;
			}
			else {

				if (!visitedSet.contains(temp)) {
					for (GraphAlgorithmObserver<V> value : observerList) {

						value.notifyVisit(temp); // visit
					}

					visitedSet.add(temp);

					Set<V> listOfOnlyKeys = graph.get(temp).keySet(); // list of neighbors 

					for (V value : listOfOnlyKeys) {

						if (!visitedSet.contains(value)) {

							queue.add(value);
						}
					}
				}

			}

		} // while loop

	}


	public void DoDFS(V start, V end) {

		Set<V> visitedSet = new HashSet<>();
		LinkedList <V> stack = new LinkedList<>(); // stack being incorporated w/ LinkedList 

		for (GraphAlgorithmObserver<V> value : observerList) { // before

			value.notifyDFSHasBegun();
		}

		stack.add(start);

		while (!stack.isEmpty()) { // checks to see if stack is not empty 

			V temp = stack.removeLast(); // retrieves the last element and removes from stack 

			if (temp.equals(end)) {

				for (GraphAlgorithmObserver<V> value : observerList) {

					value.notifySearchIsOver(); // after
				}
				break; 
			}
			else {

				if (!visitedSet.contains(temp)) { // not in visitedSet 
					for (GraphAlgorithmObserver<V> value : observerList) {

						value.notifyVisit(temp); // visit
					}

					visitedSet.add(temp);

					Set<V> listOfOnlyKeys = graph.get(temp).keySet(); // gets neighbors 

					for (V value : listOfOnlyKeys) {

						if (!visitedSet.contains(value)) {

							stack.add(value);
						}
					}
				}

			}

		} // while loop
	}

	public void DoDijsktra(V start, V end) {

		Set<V> finishedSet = new HashSet<>(); // add processed vertices 
		Map<V, V> predecessorMap = new HashMap<>(); // prev map 
		Map<V, Integer> lowestCostMap = new HashMap<>(); // costs 

		int size = graph.size(); 

		for (GraphAlgorithmObserver<V> value : observerList) { // before

			value.notifyDijkstraHasBegun();
		}

		for (V vertex : graph.keySet()) { // populates pred w/ null

			predecessorMap.put(vertex, null);
		}

		for (V vertex : graph.keySet()) { // populates lowestCost w/ max val

			lowestCostMap.put(vertex, Integer.MAX_VALUE);
		}

		lowestCostMap.put(start, 0); // 0 for starting vertex 
		predecessorMap.put(start, start); // pre for start vertext is start

		// processing done after neighbors are processed 

		while (size != finishedSet.size()) { // not all vertices are in the visitedSet 

			Set <V> lowestCosts = lowestCostMap.keySet(); 

			V compare = null;
			Integer currCost;
			Integer updatedCost;

			for (V vertex: lowestCosts) {

				if(!finishedSet.contains(vertex)) { // not in finishedSet 

					if (compare == null) { 

						compare = vertex; 
					} else { 

						currCost = lowestCostMap.get(compare); 

						updatedCost = lowestCostMap.get(vertex); 

						if (updatedCost < currCost) { // switches compare w/ the newly found best index 

							compare = vertex; 
						}
					}
				}
			} // end for each 

			finishedSet.add(compare); 

			for (GraphAlgorithmObserver<V> value : observerList) { // after added 

				value.notifyDijkstraVertexFinished(compare, lowestCostMap.get(compare));
			}

			for (V vertex: graph.get(compare).keySet()) { // neighbors of compare 

				if (!finishedSet.contains(vertex)) {

					// looks to see if getting to vertex w/ compare is faster 
					if (lowestCostMap.get(compare) + getWeight (compare, vertex) < lowestCostMap.get(vertex)) { 

						lowestCostMap.replace(vertex, lowestCostMap.get(compare) + getWeight (compare, vertex));
						predecessorMap.put(vertex, compare); 
					}
				}	
			} // end for each 
		} // while loop finished 


		V vertex = end; 

		ArrayList <V> fastestPath = new ArrayList <> (); 

		while (vertex != start) { // copies the list in the reverse order 

			fastestPath.add(vertex);
			vertex = predecessorMap.get(vertex); 

		} // I B F C  


		Collections.reverse(fastestPath); // list is reversed 

		for (GraphAlgorithmObserver<V> value : observerList) { // after ended  

			value.notifyDijkstraIsOver(fastestPath);
		}

	}


}
