package com.dm.memorandum.uml.originator;

import com.dm.memorandum.uml.activity.MainApplication;
import com.dm.memorandum.uml.memoto.Memoto;
import com.yline.log.LogFileUtil;

public class CallOfDuty
{
    private int mCheckPoint = 1;
    
    private int mLifeValue = 100;
    
    private String mWeapon = "沙漠之鹰";
    
    // 玩游戏
    public void play()
    {
        LogFileUtil.v(MainApplication.TAG, "play:" + String.format("第%d关", mCheckPoint) + ",战斗中");
        mLifeValue -= 10;
        LogFileUtil.v(MainApplication.TAG, "play: 进度升级!!");
        mCheckPoint++;
        LogFileUtil.v(MainApplication.TAG, "play:" + "到达:" + String.format("第%d关", mCheckPoint));
    }
    
    // 退出游戏
    public void quit()
    {
        LogFileUtil.v(MainApplication.TAG, "quit:-----------------------------");
        LogFileUtil.v(MainApplication.TAG, "quit: 退出前游戏属性," + this.toString());
        LogFileUtil.v(MainApplication.TAG, "quit: 退出游戏");
        LogFileUtil.v(MainApplication.TAG, "quit:-----------------------------");
    }
    
    // 创建备忘录
    public Memoto createMemoto()
    {
        Memoto memoto = new Memoto();
        memoto.mCheckPoint = mCheckPoint;
        memoto.mLifeValue = mLifeValue;
        memoto.mWeapon = mWeapon;
        return memoto;
    }
    
    // 恢复游戏
    public void restore(Memoto memoto)
    {
        this.mCheckPoint = memoto.mCheckPoint;
        this.mLifeValue = memoto.mLifeValue;
        this.mWeapon = memoto.mWeapon;
        LogFileUtil.v(MainApplication.TAG, "restore: 恢复后的游戏属性," + this.toString());
    }
    
    @Override
    public String toString()
    {
        return "CallOfDuty [mCheckPoint=" + mCheckPoint + ", mLifeValue=" + mLifeValue + ", mWeapon=" + mWeapon + "]";
    }
}
