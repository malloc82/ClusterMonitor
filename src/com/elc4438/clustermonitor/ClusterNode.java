package com.elc4438.clustermonitor;
import android.util.Log;
import com.jcraft.jsch.*;

// import java.util.HashMap;

public class ClusterNode {
    private String name;
    private String user;
    private String ip;
    private String port;
    private String work_dir;
    private String last_entry;
    public  byte[] pubkey;
    public  byte[] prvkey;
    
    public ClusterNode () {
        name   = "noname";
        user   = "none";
        ip     = "0.0.0.0";
        work_dir = "";
        last_entry = "";
    }
    public ClusterNode (String node_name, String node_user, String node_ip, String node_port, String dir) {
        name        = node_name;
        user        = node_user;
        ip          = node_ip;
        port        = node_port;
        work_dir    = dir;
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

    public String getPort () {
        return port;
    }
    
    public String toString() {
        return "name="+name+";ip="+user+"@"+ip+":"+port+";work_dir="+work_dir;
    }

    public boolean getKey(ChannelSftp ch, String key_folder) {
        try {
            pubkey = MainActivity.getkeybytes(ch.get(key_folder+"/"+name+"/pubkey"));
            prvkey = MainActivity.getkeybytes(ch.get(key_folder+"/"+name+"/prvkey"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }    
}
