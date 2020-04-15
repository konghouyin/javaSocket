package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import server.Exec;

public class Server {

	public static void main(String[] args) throws UnknownHostException, IOException {
		ServerSocket serverSocket = new ServerSocket(8999);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Exec filExec = new Exec();
		filExec.executeLinuxCmd("ps -aux");
		while (true) {
			// 监听客户端
			Socket socket = serverSocket.accept();
			socket.setKeepAlive(true);
			ServerThread thread = new ServerThread(socket);
			thread.start();

			InetAddress address = socket.getInetAddress();
			System.out.println("[Connection] ip:" + address.getHostAddress() + "\ttime:" + df.format(new Date()));
		}

	}
}

class ServerThread extends Thread {

	private Socket socket = null;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		PrintWriter pw = null;
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String info = null;

			os = socket.getOutputStream();
			pw = new PrintWriter(os);

			while (true) {
				info = br.readLine();
				System.out.println(info);
				pw.write("收到：" + info + "\n");
				pw.write("***---***\n");
				pw.flush();
			}

		} catch (Exception e) {
			System.out.println("[Eror] time:" + df.format(new Date()));
			e.printStackTrace();
			System.out.println("\n");
			// TODO: handle exception
		} finally {
			// 关闭资源
			System.out.println("[close] time:" + df.format(new Date()));
			try {
				if (pw != null)
					pw.close();
				if (os != null)
					os.close();
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (is != null)
					is.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}