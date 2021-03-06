package runner;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.RemoteHelper;
//import service.IOService;
//import ui.MainFrame;

public class ClientRunner {
	private RemoteHelper remoteHelper;
	private static String[] mainArgs;
	
	public ClientRunner() {
		linkToServer();
		initGUI();
	}
	
	private void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8887/DataRemoteObject"));
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
//		MainFrame mainFrame = new MainFrame();
		ui.Main.main(mainArgs);
	}
	
//	public void test(){
//		try {
//			System.out.println(remoteHelper.getUserService().login("admin", "123456a"));
//			System.out.println(remoteHelper.getIOService().writeFile("2", "admin", "testFile"));
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args){
		mainArgs = args;
		@SuppressWarnings("unused")
		ClientRunner cr = new ClientRunner();
		//cr.test();
	}
}
