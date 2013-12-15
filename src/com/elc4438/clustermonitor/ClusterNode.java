package com.elc4438.clustermonitor;
import android.util.Log;
import com.jcraft.jsch.*;

// import java.util.HashMap;

public class ClusterNode {
    private String name;
    private String user;
    private String ip;
    private String key_dir;
    private String last_entry;
    public byte[] pubkey;
    public  byte[] prvkey;
    
    public ClusterNode () {
        name   = "noname";
        user   = "none";
        ip     = "0.0.0.0";
        key_dir = "";
        last_entry = "";
    }
    public ClusterNode (String node_name, String node_user, String node_ip, String node_key) {
        name        = node_name;
        user        = node_user;
        ip          = node_ip;
        key_dir     = node_key;
        last_entry  = "";
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getIp () {
        return ip;
    }
    
    public String toString() {
        return "name="+name+";ip="+user+"@"+ip+";key="+key_dir;
    }

    public boolean getKey(ChannelSftp ch) {
        try {
            if (key_dir.equals("")) {
                Log.w("Cluster Monitor::ClusterNode::getKey()", "pubkey_path is empty");
                return false;
            } else {
                pubkey = MainActivity.getkeybytes(ch.get(key_dir+"/pubkey"));
                prvkey = MainActivity.getkeybytes(ch.get(key_dir+"/prvkey"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }    
}
