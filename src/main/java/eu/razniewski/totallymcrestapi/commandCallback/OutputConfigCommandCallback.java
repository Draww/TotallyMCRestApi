/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.razniewski.totallymcrestapi.commandCallback;

import com.google.gson.Gson;
import eu.razniewski.totallymcrestapi.TotallyMCRestApi;
import eu.razniewski.totallymcrestapi.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author adamr
 */
public class OutputConfigCommandCallback extends CommandCallback {

    private String command;
    private Map<String, String> additionalParams;
    
    private transient ArrayList<String> returnFromCommand = new ArrayList<>();

    public OutputConfigCommandCallback(String command, Map<String, String> additionalParams) {
        this.command = command;
        this.additionalParams = additionalParams;
    }
    
    
    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    @Override
    public Object callWithParams(Map<String, String> params) {
        try {
            this.returnFromCommand = new ArrayList<>();
            String toExecute = parseCommandWithInternalParams(getCommand());
            toExecute = parseCommandWithRequestParams(params, toExecute);
            final String fin = toExecute;
            TotallyMCRestApi instance = TotallyMCRestApi.getInstance();
            TotallyCommandSender sender = new TotallyCommandSender(this, instance.getServer().getConsoleSender());
            org.spigotmc.AsyncCatcher.enabled = false;
            instance.getServer().dispatchCommand(sender, toExecute);
            org.spigotmc.AsyncCatcher.enabled = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
    
        return returnFromCommand;
        
    }

    public void setReturnFromCommand(String returnFromCommand) {
        this.returnFromCommand.add(returnFromCommand);
    }
    
}
