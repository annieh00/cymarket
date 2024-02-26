package mainPackage.usersPackage;

import java.io.Serializable;
/**
 * @author Junhyung Shim
 * */
public class P2UKey implements Serializable {
    private int pid;
    private String uid;

    @Override
    public boolean equals(Object other){
        if(!(other instanceof P2UKey)||other == null)return false;
        if(other == this)return true;
        return ((P2UKey) other).pid == this.pid && ((P2UKey) other).uid.equals(this.uid);
    }
    public P2UKey(int p, String u){pid = p; uid = u;}
    public P2UKey(){};
}
