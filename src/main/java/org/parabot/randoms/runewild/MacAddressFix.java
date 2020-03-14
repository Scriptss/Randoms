package org.parabot.randoms.runewild;

import java.util.UUID;
import org.parabot.api.output.Logger;
import org.parabot.core.Context;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.reflect.RefClass;
import org.parabot.core.reflect.RefField;
import org.parabot.environment.randoms.Random;
import org.parabot.environment.randoms.RandomType;

/**
 * @author EmmaStone - Originally for LocoPK
 * @author Shadowrs - fixed for RuneWild with additions
 */
public class MacAddressFix implements Random {

    private boolean done;

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void execute() {
        try {
            
            System.out.println("changing mac");
            final ASMClassLoader classLoader = Context.getInstance().getASMClassLoader();

            RefClass createUID = new RefClass(classLoader.loadClass("com.rw.client.rs.CreateUID"));
            RefField mac = createUID.getField("mac");
            mac.set("lolmkay");

            RefClass animHandler = new RefClass(classLoader.loadClass("com.rw.client.rs.AnimationHandler"));
            String s1 = UUID.randomUUID().toString();
            String s2 = UUID.randomUUID().toString();
            RefField firstId = animHandler.getField("firstId");
            firstId.set(s1);
            RefField secondId = animHandler.getField("secondId");
            secondId.set(s2);
            System.out.println("done changing mac");
            
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        done = true;
    }

    @Override
    public String getName() {
        return "Mac Address Fix";
    }

    @Override
    public String getServer() {
        return "RuneWild";
    }

    @Override
    public RandomType getRandomType() {
        return RandomType.ON_SERVER_START;
    }

    private String randomMacAddress() {
        java.util.Random rand    = new java.util.Random();
        byte[]           macAddr = new byte[6];
        rand.nextBytes(macAddr);

        macAddr[0] = (byte) (macAddr[0] & (byte) 254);  //zeroing last 2 bytes to make it unicast and locally adminstrated

        StringBuilder sb = new StringBuilder(18);
        for (byte b : macAddr) {

            if (sb.length() > 0) {
                sb.append("");
            }

            sb.append(String.format("%02x", b));
        }

        return sb.toString().toUpperCase();
    }
}
