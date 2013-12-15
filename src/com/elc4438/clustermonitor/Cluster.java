package com.elc4438.clustermonitor;

import java.util.HashMap;

public class Cluster {
    public HashMap<String, ClusterNode> cluster_map; // name, node
    
    public Cluster () {
        cluster_map = new HashMap<String, ClusterNode>();
    }

    public boolean addNode(ClusterNode node) {
        String node_name = node.getName();
        
        if (!cluster_map.containsKey(node_name)) {
            cluster_map.put(node_name, node);
            return true;
        } else {
            return false;
        }
    }

    public int nodeCount() {
        return cluster_map.size();
    }

    public String toString() {
        String acc = "";
        for (String key : cluster_map.keySet()) {
            acc = acc + cluster_map.get(key).toString() + ";";
        }
        return acc;
    }
}


