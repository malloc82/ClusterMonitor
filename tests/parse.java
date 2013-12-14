import static java.lang.System.out;
import java.io.*;
import java.lang.StringBuilder;
import java.util.HashMap;

public class parse {
    public static void main(String[] args) {
        // for (int i=0; i < args.length; ++i) {
        //     out.println("arg[" + String.valueOf(i) + "]: " + args[i]);
        // }
        new parse().readtest1(args[0]);
        new parse().readtest2(args[0]);
    }
    public void readtest2(String filename) {
        out.println(" ---------- read test 2 -------------");
        
        try {
            InputStream input = new FileInputStream(filename);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
                
            while ((nRead = input.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
                
            buffer.flush();
            input.close();
            String content = buffer.toString();
            out.println("All : \n" + content);
            
            String[] lines = content.split("\n");
            for (int i=0; i < lines.length; ++i) {
                lines[i] = lines[i].trim();
                if (lines[i].length() > 0) {
                    out.println("line " +  String.valueOf(i) + ": " + lines[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readtest1(String filename) {
        out.println(" ---------- read test 1 -------------");
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = buffer.readLine();
            while (line != null) {
                sb.append(line);
                sb.append('\n');
                out.println("line = " + line);
                String[] array = line.trim().split("\\s+");
                for (int i = 0; i < array.length; ++i) {
                    out.println("       line: " + String.valueOf(i) + "  " + array[i]);
                }
                line=buffer.readLine();
            }
            out.println("print all: " + sb.toString());
            buffer.close();
        } catch (FileNotFoundException e) {
            out.println("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// public class Cluster {
//     List<ClusterNode> cluster;
//     public Cluster () {
        
//     }
//     public class ClusterNode {
//     private String name;
//     private String ip;
//     private String pubkey;
    
//     public ClusterNode () {
//         name   = "noname";
//         ip     = "0.0.0.0";
//         pubkey = "";
//     }
//     public ClusterNode () {
//         name   = "noname";
//         ip     = "0.0.0.0";
//         pubkey = "";
//     }
//     String toString() {
        
//     }
// }

// }

