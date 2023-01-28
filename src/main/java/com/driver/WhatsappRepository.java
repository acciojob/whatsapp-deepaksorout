package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name,String number){
        User user=new User(name,number);
        if(userMobile.contains(number)){
            return "";
        }
        userMobile.add(number);
        return"User Successfully created";
    }

    public Group createUser(List<User> users) {
        int n=users.size();
        if(n==2){
            Group group=new Group(users.get(1).getName(),n);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            return group;
        }
        if(n>2){
            customGroupCount+=1;
            Group group=new Group("Group "+customGroupCount,n);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            return group;
        }
        return null;
    }

    public int createMessage(String content) {
        messageId+=1;
        Message message=new Message(messageId,content,new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {
        if(!groupUserMap.containsKey(group)){
            return -1;
        }
        if(!groupUserMap.get(group).contains(sender)){
            return -2;
        }
        senderMap.put(message,sender);
        if(!groupMessageMap.containsKey(group)){
            List<Message> list=new ArrayList<>();
            list.add(message);
            groupMessageMap.put(group,list);
        }
        else {
            List<Message> list=groupMessageMap.get(group);
            list.add(message);
            groupMessageMap.put(group,list);
        }
        return groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) {
        if(!groupUserMap.containsKey(group)){
            return "";
        }
        if(!adminMap.get(group).equals(approver)){
            return "";
        }
        if(!groupUserMap.get(group).contains(user)){
            return "";
        }
        adminMap.put(group,user);
        return"Successfully admin changed";
    }
}
