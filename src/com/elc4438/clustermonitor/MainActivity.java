package com.elc4438.clustermonitor;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;

import java.io.*;
import java.net.*;
import java.util.*;   
import android.net.Uri;
import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;

import android.os.AsyncTask;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import android.util.Log;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.widget.TextView;
import android.widget.EditText;

import android.content.res.Resources;

import com.jcraft.jsch.*;


// public class MainActivity extends Activity {
//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         setContentView(R.layout.project_setup);
//     }
    
//     public void sendFeedback(View button) {
//         setContentView(R.layout.activity_main);
//     }
// }
 
public class MainActivity extends Activity {
 
	Resources resources;

    ExpandableListAdapter listAdapter;
    ExpandableListView    expListView;

    Cluster servers;

    List<String> ClusterNodes_list;
    HashMap<String, List<String>> NodeMessages;
    private TextView file_content_view;
    private TextView parse_status_view;
    private int remote2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClusterNodes_list = new ArrayList<String>();
        NodeMessages = new HashMap<String, List<String>>();
        listAdapter  = new ExpandableListAdapter(this, ClusterNodes_list, NodeMessages);
 
        resources = this.getResources();
        setContentView(R.layout.project_setup);
    }

    public void project_setup(View button) {
        setContentView(R.layout.project_setup);
    }

    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);

    public void submission_test(View button) {
        final EditText login_field = (EditText) findViewById(R.id.login_name);
        String login = login_field.getText().toString();

        final EditText ip_field = (EditText) findViewById(R.id.server_ip);
        String[] ip_port = ip_field.getText().toString().split(":");
        String port = "22";
        String ip = ip_port[0];
        if (ip_port.length > 1) {
            port = ip_port[1];
        }

        final EditText config_file_Field = (EditText) findViewById(R.id.setup_config_file);
        String config_file = config_file_Field.getText().toString();
        
        setContentView(R.layout.read_test);

        TextView read_val_1 = (TextView) findViewById(R.id.read_val_1);
        read_val_1.setText(login);

        TextView read_val_2 = (TextView) findViewById(R.id.read_val_2);
        read_val_2.setText(ip + ":" + port);

        TextView read_val_3 = (TextView) findViewById(R.id.read_val_3);
        read_val_3.setText(config_file);

        TextView read_val_4 = (TextView) findViewById(R.id.read_val_4);
        read_val_4.setText("IP : " + getIPAddress(true));

        HashMap<String, String> hostinfo = new HashMap<String, String>();

        hostinfo.put("user", "zcai");
        hostinfo.put("host", "192.168.8.103");
        hostinfo.put("port", "22");
        hostinfo.put("config", "sandbox/app_test/cluster_config.txt");
        hostinfo.put("key_folder", "sandbox/app_test");

        // hostinfo.put("user", login);
        // hostinfo.put("host", ip);
        // hostinfo.put("port", port);
        // hostinfo.put("config", config_file);

        file_content_view = (TextView) findViewById(R.id.file_content);
        file_content_view.setText("Waiting ...");

        parse_status_view = (TextView) findViewById(R.id.parse_status);
        parse_status_view.setText("N/A");

        try {
            new FetchClusterInfo().execute(hostinfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void clusterStatus(View button) {
        setContentView(R.layout.activity_main);
 
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setAdapter(listAdapter);

        for (Map.Entry entry : servers.cluster_map.entrySet()) {
            // System.out.print("key,val: ");
            // System.out.println(entry.getKey() + "," + entry.getValue());
            final ClusterNode node = (ClusterNode)entry.getValue();
            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {       
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                                public void run() {       
                                    try {
                                        new NodeUpdate().execute(node);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    }
                };
            timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 10s
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    } 

    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { 
            ex.printStackTrace();
        } // for now eat exceptions
        return "";
    }

    public static Cluster parseConfigfile(String file_content, ChannelSftp ch, String key_folder) {
        try {
            Cluster cluster = new Cluster();
            String[] entries = file_content.split("\n");
            for (int i=0; i < entries.length; ++i) {
                entries[i] = entries[i].trim();
                if (entries[i].length() > 0) {
                    String[] fields = entries[i].split("\\s+");
                    String[] user_ip = fields[1].split("@");
                    String[] ip_port = user_ip[1].split(":");
                    String port="22";
                    if (ip_port.length > 1) {
                        port = ip_port[1];
                    }
                    ClusterNode node = new ClusterNode(fields[0], user_ip[0], ip_port[0], port, fields[2]);
                    if (!node.getKey(ch, key_folder)) {
                        Log.w("ClusterMonitor::parseConfigfile", "fail to get keybyte for "+user_ip[1]);
                    } else {
                        Log.w("ClusterMonitor::parseConfigfile", ip_port[0] + ":" + port  + " --   key fetched");
                    }
                    cluster.addNode(node);
                }
            }
            return cluster;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // public static  byte[] getkeybytes(int res_id) {
    public static  byte[] getkeybytes(InputStream stream) {
        // InputStream key_stream = resources.openRawResource(res_id);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            int nRead;
            byte[] data = new byte[1024];
                
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
                
            buffer.flush();
            stream.close();
            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static String readStream(InputStream in) {
        ByteArrayOutputStream  buffer = new ByteArrayOutputStream();
        try {
            int nRead;
            byte[] data = new byte[1024];
                
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
                
            buffer.flush();
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private class FetchClusterInfo extends AsyncTask<HashMap<String, String>, Integer, String> {
        private boolean success;
        private JSch jsch = new JSch();
        private Session session = null;
        private ChannelSftp sftpChannel;
        @Override
        protected String doInBackground(HashMap<String, String>... hostinfo) {
            success = false;
            jsch = new JSch();
            session = null;
            try {
                Log.w("App Debug : ", "in FetchClusterInfo 1");
                String username    = hostinfo[0].get("user");
                String host        = hostinfo[0].get("host");
                String port        = hostinfo[0].get("port");
                String config_file = hostinfo[0].get("config");
                String key_folder  = hostinfo[0].get("key_folder");
                Log.w("App Debug: ", "in FetchClusterInfo " 
                      + username + " " + host + ":" + port + " " + config_file);
                InputStream prvkey_stream = resources.openRawResource(R.raw.prvkey);
                InputStream pubkey_stream = resources.openRawResource(R.raw.pubkey);
                
                jsch.addIdentity("boxster",
                                 MainActivity.getkeybytes(prvkey_stream),
                                 MainActivity.getkeybytes(pubkey_stream),
                                 new byte[0]);
                session = jsch.getSession(username, host, 22);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("PreferredAuthentications", "publickey");
                // out.println("here1");
                // session.setConfig("StrictHostKeyChecking", "no");            
                // session.setPassword("Password");
                session.connect();
                // out.println("here2");

                Channel channel = session.openChannel("sftp");
                channel.connect();
                sftpChannel = (ChannelSftp) channel;

                // sftpChannel.get("dummy.txt", "dummy_copy.txt");
                String result = MainActivity.readStream(sftpChannel.get(config_file));
                Log.w("cluster monitor debug: ", config_file);

                servers = parseConfigfile(result, sftpChannel, key_folder);

                // sftpChannel.exit();
                // session.disconnect();
                success = true;
                return result;
            } catch (JSchException e) {
                e.printStackTrace(); 
                return "download failed 1";
            } catch (SftpException e) {
                e.printStackTrace();
                return "download failed 2";
            }
        }

        protected void onPostExecute(String result) {
            file_content_view.setText(result);
            if (success == true) {
                try {
                    Log.w("Debug: ", "onPostExecute  " + result);
                    Log.w("Debug: ", "Server setup complete");
                    sftpChannel.exit();
                    session.disconnect();
                    parse_status_view.setText(servers.toString());
                    // parse_status_view.setText("Waiting for parsing result ...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class NodeUpdate extends AsyncTask<ClusterNode, Integer, String> {
        private boolean success;
        private int index;
        private String node_name;
        @Override
        protected String doInBackground(ClusterNode... node) {
            success = false;
            JSch jsch = new JSch();
            Session session = null;
            try {
                node_name       = node[0].getName();
                String username = node[0].getUser();
                String host     = node[0].getIp();
                int    port     = Integer.parseInt(node[0].getPort());
                Log.w("debug", node[0].toString());
                byte[] prvkey_copy = node[0].prvkey.clone();
                jsch.addIdentity(node_name,
                                 prvkey_copy, // key will be zeroed out after the call
                                 node[0].pubkey,
                                 new byte[0]);
                if (Arrays.equals(prvkey_copy, node[0].prvkey)) {
                    Log.w("Debuge NodeUPdate: ", "keys are equal before and after calling addIdentity");
                } else {
                    Log.w("Debuge NodeUPdate: ", "keys changed.");
                }
                session = jsch.getSession(username, host, port);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("PreferredAuthentications", "publickey");
                session.connect();
                Log.w("lego debug:", node_name + "  here1");
                Channel channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand("tail -n 1 log.txt");
                channel.setInputStream(null);
                ((ChannelExec)channel).setErrStream(System.err);
                InputStream output_stream=channel.getInputStream();
                Log.w("lego debug:", node_name + "  here2");
                channel.connect();
                Log.w("lego debug:", node_name + "  here3");
                String output = readStream(output_stream);
                channel.disconnect();
                return output;
            } catch (IOException e) {
                e.printStackTrace();                 
                return "remote execute failed IO";
            } catch (JSchException e) {
                e.printStackTrace(); 
                return "remote execute failed 1";
            } catch (Exception e) {
                e.printStackTrace();
                return "bad ...";
            }
        }
        protected void onPostExecute(String result) {
            // file_content_view.setText(result);
            int index = ClusterNodes_list.indexOf(node_name);
            if (index < 0) {
                ClusterNodes_list.add(node_name);
                index = ClusterNodes_list.size()-1;
            }

            if (NodeMessages.containsKey(node_name)) {
                NodeMessages.get(node_name).add(result);
            } else {
                List<String> msg = new ArrayList<String>();
                msg.add(result);
                NodeMessages.put(node_name, msg);
            }
            listAdapter.notifyDataSetChanged();
        }
    }
}
