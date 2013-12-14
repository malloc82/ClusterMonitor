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

    List<String> ClusterNodes;
    HashMap<String, List<String>> NodeMessages;
    private TextView file_content_view;
    private TextView parse_status_view;
    private int remote2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClusterNodes = new ArrayList<String>();
        NodeMessages = new HashMap<String, List<String>>();
        listAdapter  = new ExpandableListAdapter(this, ClusterNodes, NodeMessages);
 
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
        final EditText nameField = (EditText) findViewById(R.id.setup_cluster_name);
        String cluster_name = nameField.getText().toString();

        final EditText ipField = (EditText) findViewById(R.id.setup_server_ip);
        String ip = ipField.getText().toString();

        final EditText locationField = (EditText) findViewById(R.id.setup_file_location);
        String location = locationField.getText().toString();
        
        setContentView(R.layout.read_test);

        TextView read_val_1 = (TextView) findViewById(R.id.read_val_1);
        read_val_1.setText(cluster_name);

        TextView read_val_2 = (TextView) findViewById(R.id.read_val_2);
        read_val_2.setText(ip);

        TextView read_val_3 = (TextView) findViewById(R.id.read_val_3);
        read_val_3.setText(location);

        TextView read_val_4 = (TextView) findViewById(R.id.read_val_4);
        read_val_4.setText("IP : " + getIPAddress(true));

        HashMap<String, String> hostinfo = new HashMap<String, String>();
        hostinfo.put("user", "zcai");
        hostinfo.put("host", "192.168.8.103");
        hostinfo.put("config", "sandbox/app_test/cluster_config.txt");
        file_content_view = (TextView) findViewById(R.id.file_content);
        file_content_view.setText("Waiting ...");

        parse_status_view = (TextView) findViewById(R.id.parse_status);
        parse_status_view.setText("N/A");
        new FetchClusterInfo().execute(hostinfo);
        // try {
        //     String result = new remoteExecution().execute(hostinfo).get();
        //     TextView file_content = (TextView) findViewById(R.id.file_content);
        //     read_val_4.setText(result);
        // } catch(Exception e) {
        //     e.printStackTrace();
        // }
    }


    public void clusterStatus(View button) {
        setContentView(R.layout.activity_main);
 
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setAdapter(listAdapter);

        // HashMap<String, String> hostinfo = new HashMap<String, String>();
        // hostinfo.put("user", "zcai");
        // hostinfo.put("host", "192.168.8.103");
        // hostinfo.put("config", "sandbox/app_test/cluster_config.txt");
        new remoteExecution2().execute(servers.cluster_map.get("boxster"));

        // get the listview
 
        // preparing list data
        // prepareListData();
  
        // Listview Group click listener
        // expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
        //     @Override
        //     public boolean onGroupClick(ExpandableListView parent, View v,
        //             int groupPosition, long id) {
        //         // Toast.makeText(getApplicationContext(),
        //         // "Group Clicked " + ClusterNodes.get(groupPosition),
        //         // Toast.LENGTH_SHORT).show();
        //         return false;
        //     }
        // });
 
        // Listview Group expanded listener
        // expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
        //     @Override
        //     public void onGroupExpand(int groupPosition) {
        //         Toast.makeText(getApplicationContext(),
        //                 ClusterNodes.get(groupPosition) + " Expanded",
        //                 Toast.LENGTH_SHORT).show();
        //     }
        // });
 
        // Listview Group collasped listener
        // expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
        //     @Override
        //     public void onGroupCollapse(int groupPosition) {
        //         Toast.makeText(getApplicationContext(),
        //                 ClusterNodes.get(groupPosition) + " Collapsed",
        //                 Toast.LENGTH_SHORT).show();
 
        //     }
        // });
 
        // Listview on child click listener
        // expListView.setOnChildClickListener(new OnChildClickListener() {
 
        //     @Override
        //     public boolean onChildClick(ExpandableListView parent, View v,
        //             int groupPosition, int childPosition, long id) {
        //         // TODO Auto-generated method stub
        //         Toast.makeText(
        //                 getApplicationContext(),
        //                 ClusterNodes.get(groupPosition)
        //                         + " : "
        //                         + NodeMessages.get(
        //                                 ClusterNodes.get(groupPosition)).get(
        //                                 childPosition), Toast.LENGTH_SHORT)
        //                 .show();
        //         return false;
        //     }
        // });
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() { 
        // Adding child data
        ClusterNodes.add("Node1");
        ClusterNodes.add("Node2");
        ClusterNodes.add("Node3");
 
        // Adding child data
        List<String> Node1 = new ArrayList<String>();
        Node1.add("item1");
        Node1.add("item2");
        
        List<String> Node2 = new ArrayList<String>();
        Node2.add("item1");
        Node2.add("item2");
        Node2.add("item3");
        
 
        List<String> Node3 = new ArrayList<String>();
        Node3.add("first");
        Node3.add("second");
        Node3.add("third");

        NodeMessages.put(ClusterNodes.get(0), Node1); // Node, Node message
        NodeMessages.put(ClusterNodes.get(1), Node2);
        NodeMessages.put(ClusterNodes.get(2), Node3);
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

    public static Cluster parseConfigfile(String file_content, ChannelSftp ch) {
        try {
            Cluster cluster = new Cluster();
            String[] entries = file_content.split("\n");
            for (int i=0; i < entries.length; ++i) {
                entries[i] = entries[i].trim();
                if (entries[i].length() > 0) {
                    String[] fields = entries[i].split("\\s+");
                    String[] user_ip = fields[1].split("@");
                    ClusterNode node = new ClusterNode(fields[0], user_ip[0], user_ip[1], fields[2]);
                    if (!node.getKey(ch)) {
                        Log.w("ClusterMonitor::parseConfigfile", "fail to get keybyte for "+user_ip[1]);
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
                String username    = hostinfo[0].get("user");
                String host        = hostinfo[0].get("host");
                String config_file = hostinfo[0].get("config");
                // final byte[] emptyPassPhrase = new byte[0];
                //InputStream prvkey_stream = Resources.openRawResource(R.raw.id_rsa);
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
                Log.w("cluster monitor debug: ", config_file);
                String result = MainActivity.readStream(sftpChannel.get(config_file));
                Log.w("cluster monitor debug: ", config_file);
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
                servers = parseConfigfile(result, sftpChannel);
                sftpChannel.exit();
                session.disconnect();
                parse_status_view.setText(servers.toString());
                // parse_status_view.setText("Waiting for parsing result ...");
            }
        }
    }

    private class remoteExecution2 extends AsyncTask<ClusterNode, Integer, String> {
        private boolean success;
        @Override
        protected String doInBackground(ClusterNode... node) {
            success = false;
            JSch jsch = new JSch();
            Session session = null;
            try {
                String label    = node[0].getName();
                String username = node[0].getUser();
                String host     = node[0].getIp();
                // final byte[] emptyPassPhrase = new byte[0];
                //InputStream prvkey_stream = Resources.openRawResource(R.raw.id_rsa);
                // InputStream prvkey_stream = resources.openRawResource(R.raw.prvkey);
                // InputStream pubkey_stream = resources.openRawResource(R.raw.pubkey);
                Log.w("debug", node[0].toString());

                ClusterNodes.add(label);
                listAdapter.notifyDataSetChanged();
                int index = ClusterNodes.indexOf(label);


                jsch.addIdentity(label,
                                 node[0].prvkey,
                                 node[0].pubkey,
                                 new byte[0]);
                session = jsch.getSession(username, host, 22);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("PreferredAuthentications", "publickey");
                // out.println("here1");
                // session.setConfig("StrictHostKeyChecking", "no");            
                // session.setPassword("Password");
                Log.w("debug", "here1");
                session.connect();
                Log.w("debug", "here2");
                // out.println("here2");

                Channel channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand("tail -n 1 log.txt");
                channel.setInputStream(null);
                ((ChannelExec)channel).setErrStream(System.err);
                InputStream output_stream=channel.getInputStream();
                Log.w("debug", "here3");
                channel.connect();
                String output = readStream(output_stream);
                List<String> msg = new ArrayList<String>();
                msg.add(output);
                NodeMessages.put(ClusterNodes.get(index), msg);
                listAdapter.notifyDataSetChanged();
                channel.disconnect();
                
                return "good";
            } catch (IOException e) {
                e.printStackTrace();                 
                return "remote execute failed IO";
            } catch (JSchException e) {
                e.printStackTrace(); 
                return "remote execute failed 1";
            }
        }
        // protected void onPostExecute(String result) {
        //     // file_content_view.setText(result);
        //     ClusterNodes.add("Boxster");
        //     List<String> Boxster_msg = new ArrayList<String>();
        //     Boxster_msg.add("ddd");
        //     NodeMessages.put(ClusterNodes.get(0), Boxster_msg);
        //     listAdapter.notifyDataSetChanged();
        // }
    }
}
