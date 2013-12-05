package com.elc4438.clustermonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import android.widget.TextView;
import android.widget.EditText;

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
 
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        read_val_1.setText(cluster_name + " 2 ");

        TextView read_val_2 = (TextView) findViewById(R.id.read_val_2);
        read_val_2.setText(ip);

        TextView read_val_3 = (TextView) findViewById(R.id.read_val_3);
        read_val_3.setText(location);
    }

    public void clusterStatus(View button) {
        setContentView(R.layout.activity_main);
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Group1");
        listDataHeader.add("Group2");
        listDataHeader.add("Group3");
 
        // Adding child data
        List<String> Group1 = new ArrayList<String>();
        Group1.add("item1");
        Group1.add("item2");
        
        List<String> Group2 = new ArrayList<String>();
        Group2.add("item1");
        Group2.add("item2");
        Group2.add("item3");
        
 
        List<String> Group3 = new ArrayList<String>();
        Group3.add("first");
        Group3.add("second");
        Group3.add("third");

 
        listDataChild.put(listDataHeader.get(0), Group1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Group2);
        listDataChild.put(listDataHeader.get(2), Group3);
    }
}
