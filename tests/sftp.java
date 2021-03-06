import static java.lang.System.out;
import com.jcraft.jsch.*;

public class sftp {
        private byte[] getkeybytes(int res_id) {
            InputStream key_stream = resources.openRawResource(res_id);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                int nRead;
                byte[] data = new byte[1024];
                
                while ((nRead = key_stream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                
                buffer.flush();
                key_stream.close();
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
    public static void main(String[] args) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            final byte[] emptyPassPhrase = new byte[0];
            jsch.addIdentity("/Users/zcai/.ssh/id_rsa", 
                             "/Users/zcai/.ssh/id_rsa.pub", 
                             emptyPassPhrase);
            session = jsch.getSession("zcai", "localhost", 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey");
            out.println("here1");
            // session.setConfig("StrictHostKeyChecking", "no");            
            // session.setPassword("Password");
            session.connect();
            out.println("here2");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            sftpChannel.get("dummy.txt", "dummy_copy.txt");
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace(); 
        } catch (SftpException e) {
            e.printStackTrace();
        }
    } 
}

