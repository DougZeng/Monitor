/*
 * 2010-2017 (C) Antonio Redondo
 * http://antonioredondo.com
 * http://github.com/AntonioRedondo/AnotherMonitor
 *
 * Code under the terms of the GNU General Public License v3.
 *
 */

package org.doug.monitor.base;

public class Constans {

    public static final String prefs = "Prefs";
    public static final String sbh = "status_bar_height";
    public static final String nbh = "navigation_bar_height";
    public static final String dimen = "dimen";
    public static final String android = "android";
    public static final String europeLondon = "Europe/London";
    public static final String marketDetails = "market://details?id=";
    public static final int defaultIntervalRead = 1000;
    public static final int defaultIntervalUpdate = 1000;
    public static final int defaultIntervalWidth = 1;

    // ServiceReader
    public static final String readThread = "readThread";

    public static final String actionStartRecord = "actionRecord";
    public static final String actionStopRecord = "actionStop";
    public static final String actionClose = "actionClose";
    public static final String actionSetIconRecord = "actionSetIconRecord";
    public static final String actionDeadProcess = "actionRemoveProcess";
    public static final String actionFinishActivity = "actionCloseActivity";
    public static final String actionAutoTest = "actionAutoTest";

    public static final String pId = "pId";
    public static final String pName = "pName";
    public static final String pPackage = "pPackage";
    public static final String pAppName = "pAppName";
    public static final String pTPD = "pPTD";
    public static final String pSelected = "pSelected";
    public static final String pDead = "pDead";
    public static final String pColour = "pColour";
    public static final String work = "work";
    public static final String workBefore = "workBefore";
    public static final String pFinalValue = "finalValue";
    public static final String process = "process";
    public static final String screenRotated = "screenRotated";
    public static final String listSelected = "listSelected";
    public static final String listProcesses = "listProcesses";

    // ActivityMonitor
    public static final int storagePermission = 1;
    public static final String kB = "kB";
    public static final String percent = "%";
    //	static final String drawThread = "drawThread";
    public static final String menuShown = "menuShown";
    public static final String settingsShown = "settingsShown";
    public static final String orientation = "orientation";
    public static final String processesMode = "processesMode";
    public static final String canvasLocked = "canvasLocked";

    public static final String welcome = "firstTime";
    public static final String welcomeDate = "firstTimeDate";
    public static final String firstTimeProcesses = "firstTimeProcesses";
    public static final String feedbackFirstTime = "feedbackFirstTime";
    public static final String feedbackDone = "feedbackDone";

    public static final String intervalRead = "intervalRead";
    public static final String intervalUpdate = "intervalUpdate";
    public static final String intervalWidth = "intervalWidth";

    public static final String cpuTotal = "cpuTotalD";
    public static final String cpuAM = "cpuAMD";
    public static final String memUsed = "memUsedD";
    public static final String memAvailable = "memAvailableD";
    public static final String memFree = "memFreeD";
    public static final String cached = "cachedD";
    public static final String threshold = "thresholdD";

    // GraphicView
    public static final String processMode = "processMode";
    public static final int processesModeShowCPU = 0;
    public static final int processesModeShowMemory = 1;

    public static final String graphicMode = "graphicMode";
    public static final int graphicModeShowMemory = 0;
    public static final int graphicModeHideMemory = 1;

    // ActivityPreferences
    public static final String currentItem = "ci";

    public static final String mSRead = "mSRead";
    public static final String mSUpdate = "mSUpdate";
    public static final String mSWidth = "mSWidth";

    public static final String mCBMemFreeD = "memFreeD";
    public static final String mCBBuffersD = "buffersD";
    public static final String mCBCachedD = "cachedD";
    public static final String mCBActiveD = "activeD";
    public static final String mCBInactiveD = "inactiveD";
    public static final String mCBSwapTotalD = "swapTotalD";
    public static final String mCBDirtyD = "dirtyD";
    public static final String mCBCpuTotalD = "cpuTotalD";
    public static final String mCBCpuAMD = "cpuAMD";
//	static final String mCBCpuRestD = "cpuRestD";


    public static final String SETTING = "setting";
    public static final String BOTH = "both";
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String CHANGED = "changed";
    public static final String INIT_X = "init_x";
    public static final String INIT_Y = "init_y";
    public static final String IS_SHOWN = "is_shown";

    public static final String IS_FIRST_BOOT = "is_first_boot";
    public static final int DEFAULT_COUNT = 0;


    public static final String DEFAULT_NA = "NA";
    public static final String PASS = "PASS";
    public static final String FAIL = "FAIL";

    public static final int DURATION_DIALOG = 300;

    public static final String DEFAULT_TEST_TIMES = "1500";


    public static final String TEST_ASSEMBLY = "assembly";
    public static final String TEST_ASSEMBLY_0 = "assembly_visualInspection";
    public static final String TEST_ASSEMBLY_1 = "assembly_camera";
    public static final String TEST_ASSEMBLY_2 = "assembly_scanner";
    public static final String TEST_ASSEMBLY_3 = "assembly_touch";
    public static final String TEST_ASSEMBLY_4 = "assembly_audio";
    public static final String TEST_ASSEMBLY_5 = "assembly_ethernet";
    public static final String TEST_ASSEMBLY_6 = "assembly_wifi";

    public static final String TEST_AGING = "aging";
    public static final String TEST_AGING_0 = "aging_factory";

    public static final String TEST_PERFORMANCE = "performance";
    public static final String TEST_PERFORMANCE_0 = "performance_displayVersion";
    public static final String TEST_PERFORMANCE_1 = "performance_serial";
    public static final String TEST_PERFORMANCE_2 = "performance_e_mac";
    public static final String TEST_PERFORMANCE_3 = "performance_w_mac";

    public static final String TEST_PERFORMANCE_4 = "performance_visualInspection";
    public static final String TEST_PERFORMANCE_5 = "performance_camera";
    public static final String TEST_PERFORMANCE_6 = "performance_scanner";
    public static final String TEST_PERFORMANCE_7 = "performance_touch";
    public static final String TEST_PERFORMANCE_8 = "performance_audio";
    public static final String TEST_PERFORMANCE_9 = "performance_ethernet";
    public static final String TEST_PERFORMANCE_10 = "performance_wifi";

    public static final String TEST_CLEAN = "performance_clean";

    public static final String SERIAL = "serialNo";
    public static final String ETH0 = "eth0";
    public static final String WLAN = "wlan";


    public static final String ACTION_A = "org.doug.monitor.action.a";
    public static final String ACTION_P = "org.doug.monitor.action.p";

    public static final int TEST_TIME_TOUCH = 18;
    public static final int TEST_TIME_AUDIO = 8;
    public static final int TEST_TIME_NETWORK = 9;
    public static final int TEST_TIME_VERSION = 12;
    public static final int TEST_TIME_VISUAL = 12;

    public static final long DELAYMILLIS = 2000;
}
