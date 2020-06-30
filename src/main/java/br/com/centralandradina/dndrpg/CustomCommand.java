package br.com.centralandradina.dndrpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CustomCommand implements ConsoleCommandSender
{

public ArrayList<String> output_lines = new ArrayList<String>();
	
	@Override
	public void sendMessage(String message) {
		this.output_lines.add(message);
	}
	
	@Override
	public void sendMessage(String[] messages) {
		this.output_lines = new ArrayList<String>(Arrays.asList(messages));
	}
	
	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}
	
	@Override
	public String getName() {
		return "DnDRPG";
	}
	
	@Override
	public boolean isPermissionSet(String name) {
		return true;
	}
	
	@Override
	public boolean isPermissionSet(Permission perm) {
		return true;
	}
	
	@Override
	public boolean hasPermission(String name) {
		return true;
	}
	
	@Override
	public boolean hasPermission(Permission perm) {
		return true;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void recalculatePermissions() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isOp() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void setOp(boolean value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isConversing() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void acceptConversationInput(String input) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean beginConversation(Conversation conversation) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void abandonConversation(Conversation conversation) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendRawMessage(String message) {
		// TODO Auto-generated method stub
		
	}
	
	
}
