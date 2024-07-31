package net.brian.rpgcrafting.configs;

import java.util.HashMap;

public class UpgradeConfig {
    public String type,id;

    public HashMap<Integer,UpgradeInfo> map = new HashMap<>();



    public static class UpgradeInfo{
        public float successChance,downGradeChance;
    }

}
