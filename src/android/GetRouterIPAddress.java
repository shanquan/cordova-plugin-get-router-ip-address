package com.vallieres.plugin.getrouteripaddress;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;

import java.net.InetAddress;  
import java.net.NetworkInterface;  
import java.net.SocketException;
import java.util.Enumeration; 

public class GetRouterIPAddress extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if(action.equals("getMacAddress")){
			try {
				String ip = getRouterIPAddress();
				if (ip==null||ip.equals("0.0.0.0")) {
					callbackContext.error("No valid IP address");
					return true;
				}
				callbackContext.success(ip);
				return true;
			} catch(Exception e) {
				callbackContext.error("Error while retrieving the IP address. " + e.getMessage());
				return true;
			}
		}
		return false;
	}

	private String formatIP(int ip) {
		return String.format(
			"%d.%d.%d.%d",
			(ip & 0xff),
			(ip >> 8 & 0xff),
			(ip >> 16 & 0xff),
			(ip >> 24 & 0xff)
		);
	}

	private String getRouterIPAddress() {
		// WifiManager wifiManager = (WifiManager) cordova.getActivity().getSystemService(Context.WIFI_SERVICE);
		// DhcpInfo dhcp = wifiManager.getDhcpInfo();
		// int ip = dhcp.gateway;
		// return formatIP(ip);
		try {  
	            for (Enumeration<NetworkInterface> en = NetworkInterface  
	                            .getNetworkInterfaces(); en.hasMoreElements();) {  
	                    NetworkInterface intf = en.nextElement(); 
	                    if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {   
	                    for (Enumeration<InetAddress> enumIpAddr = intf  
	                                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                            InetAddress inetAddress = enumIpAddr.nextElement();  
	                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) { 
	                            	return inetAddress.getHostAddress().toString(); 
	                            }  
	                    } 
	                  } 
	            }  
	    } catch (SocketException ex) {  
	            System.out.println("WifiPreference IpAddress"+ex.toString());  
	    }  
	    return null; 
	}
}
