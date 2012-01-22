// The "Tetris" class.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.applet.*;
import java.net.*;
import javax.swing.border.*;
import java.lang.String;
import hsa.TextInputFile;
import hsa.*;

/** Tetris Main
  * @authors Aysar Khalid and Or-el Mousaffi
  * @version 2.0 - January 7th, 2007
  */
public class Tetris extends Frame
{
    // Declare all static and constants
    static int TOP_OFFSET = 82;
    static int BORDER = 6;
    static int IMAGE_WIDTH;
    static int IMAGE_HEIGHT;

    private static final Font gameStatsFont = new Font ("Arial", Font.BOLD, 12);

    // Game field Constants
    public static final int NO_OF_ROWS = 25;
    public static final int NO_OF_COLUMNS = 15;
    public static final int SQUARE_SIZE = 20;

    // Game Field and Shape Vars
    private int[] [] grid; // Game Field
    private int currentColumn;
    private int currentRow;
    private int excludeRow;

    private String file;
    private String mapFile;

    private Image[] blocks = new Image [16];
    private Image[] guiImages = new Image [4];
    private Image[] tipImages = new Image [10];
    private int generateTipImageNo = 0;
    private boolean shouldReplace = false;
    private Shape currentShape;
    private Shape nextShape;
    private boolean gameOn;
    private boolean timeAttackMode;
    private boolean missionMode;
    private boolean validChoice;

    // Menu Items
    public MenuItem normalModeOption;
    public MenuItem timeAttackOption;
    public MenuItem missionModeOption;
    public MenuItem exitGameOption;
    public MenuItem playerName;
    public MenuItem normalModeHighScoreOption;
    public MenuItem quitCurrentGame;
    public MenuItem featuresGameOption;
    public MenuItem helpGameOption;
    public MenuItem tipsGameOption;
    public MenuItem creditsOption;

    public MenuItem musicOff;
    public MenuItem track1;
    public MenuItem track2;
    public MenuItem track3;
    public MenuItem track4;
    public MenuItem track5;

    // Audio Vars
    private AudioClip backgroundTrack;
    private AudioClip backgroundTrack2;
    private AudioClip backgroundTrack3;
    private AudioClip backgroundTrack4;
    private AudioClip backgroundTrack5;
    private AudioClip excludeLineSound;
    private AudioClip boundrySound;
    private AudioClip gameOverSound;
    private AudioClip pauseSound;
    private AudioClip rotateSound;
    private AudioClip welcomeSound;
    private AudioClip changeShapeItemSound;
    private AudioClip decreaseItemSound;
    private AudioClip increaseItemSound;
    private AudioClip removeAllItemSound;
    private AudioClip itemLostSound;

    // Timer Vars
    Timer blockTimer;
    int blockTime;

    Timer gameTimer;
    int gameTime;

    Timer tipImageTimer;
    int tipImageTime;
    boolean tipImageTimeOn;
    int tipImageTimeAllowed;

    // Player/Game Vars
    private boolean isShapeSet;
    private boolean isGameOver;
    private boolean isGamePause;
    private String gameStatus;
    private String playerNameStr;
    private int playerScore;
    private double oldPlayerScore;
    private int speedMultiplyer;

    private String totalPlayerTime;

    private int shapeSpeed;
    private int currentLevel;

    private int blocksUsed;
    private int preBlocks;
    private int excludedLines;
    private int missonModeExcLines;
    private int choice;
    private int timeAttackTime;

    private int rowExclude;
    private int nextShapePosRow;
    private int nextShapePosCol;

    // Item Vars
    private Items item;
    private int itemCount;

    Timer itemTimer;
    boolean itemTimerOn;
    int itemTime;
    int timeAllowed;

    // HighScore Vars
    private int[] topTenClassic = new int [10];
    private String[] topTenNameClassic = new String [10];
    private String[] topTenTimeClassic = new String [10];
    private int[] topTenBlocksClassic = new int [10];

    // Misc Vars
    Image offScreenImage;
    Graphics offScreenBuffer;

    public Tetris ()
    {
	super ("Tetris - Developed by Aysar Khalid and Or-el Mousaffi"); // Set the frame's name

	// Import grid from text file
	setLocation (200, 100);
	importGrid ("maps/grid.txt");

	//Set block and item image allocations
	blocks [1] = new ImageIcon ("images/blocks/orangeBlock.gif").getImage ();
	blocks [2] = new ImageIcon ("images/blocks/yellowBlock.gif").getImage ();
	blocks [3] = new ImageIcon ("images/blocks/redBlock.gif").getImage ();
	blocks [4] = new ImageIcon ("images/blocks/purpleBlock.gif").getImage ();
	blocks [5] = new ImageIcon ("images/blocks/blueBlock.gif").getImage ();
	blocks [6] = new ImageIcon ("images/blocks/pinkBlock.gif").getImage ();
	blocks [7] = new ImageIcon ("images/blocks/cyanBlock.gif").getImage ();
	blocks [8] = new ImageIcon ("images/blocks/greenBlock.gif").getImage ();
	blocks [9] = new ImageIcon ("images/blocks/preliminaryBlock.gif").getImage ();

	// Items
	blocks [10] = new ImageIcon ("images/items/incPoints.gif").getImage ();
	blocks [11] = new ImageIcon ("images/items/clearBlocks.gif").getImage ();
	blocks [12] = new ImageIcon ("images/items/decPoints.gif").getImage ();
	blocks [13] = new ImageIcon ("images/items/decSpeed.gif").getImage ();
	blocks [14] = new ImageIcon ("images/items/chngShape.gif").getImage ();
	blocks [15] = new ImageIcon ("images/items/incSpeed.gif").getImage ();

	// Set game layout image allocations
	guiImages [0] = new ImageIcon ("images/layout/nextShapeImage.gif").getImage ();
	guiImages [1] = new ImageIcon ("images/layout/gameStatImage.gif").getImage ();
	guiImages [2] = new ImageIcon ("images/layout/tetrisBackLayout.gif").getImage ();
	guiImages [3] = new ImageIcon ("images/layout/gameTipsImage.gif").getImage ();

	// Set the image allocations for the game tips
	tipImages [0] = new ImageIcon ("images/layout/tip1.gif").getImage ();
	tipImages [1] = new ImageIcon ("images/layout/tip2.gif").getImage ();
	tipImages [2] = new ImageIcon ("images/layout/tip3.gif").getImage ();
	tipImages [3] = new ImageIcon ("images/layout/tip4.gif").getImage ();
	tipImages [4] = new ImageIcon ("images/layout/tip5.gif").getImage ();
	tipImages [5] = new ImageIcon ("images/layout/tip6.gif").getImage ();
	tipImages [6] = new ImageIcon ("images/layout/tip7.gif").getImage ();
	tipImages [7] = new ImageIcon ("images/layout/tip8.gif").getImage ();
	tipImages [8] = new ImageIcon ("images/layout/tips9.gif").getImage ();
	tipImages [9] = new ImageIcon ("images/layout/tips10.gif").getImage ();

	//Set all audio files allocations
	excludeLineSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/excludeLine.wav"));
	backgroundTrack = Applet.newAudioClip (getCompleteURL ("sound/wave files/Zanzibar.wav"));
	backgroundTrack2 = Applet.newAudioClip (getCompleteURL ("sound/wave files/Popcorn.wav"));
	backgroundTrack3 = Applet.newAudioClip (getCompleteURL ("sound/wave files/Sandstorm.wav"));
	backgroundTrack4 = Applet.newAudioClip (getCompleteURL ("sound/wave files/Adagio for Strings.wav"));
	backgroundTrack5 = Applet.newAudioClip (getCompleteURL ("sound/wave files/Summer Is Calling.wav"));
	gameOverSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/gameOver.wav"));
	pauseSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/pause.wav"));
	welcomeSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/SP0000.wav"));
	rotateSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/pop.wav"));

	changeShapeItemSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/changeShape.wav"));
	decreaseItemSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/decrease.wav"));
	increaseItemSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/increase.wav"));
	removeAllItemSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/removeAll.wav"));
	itemLostSound = Applet.newAudioClip (getCompleteURL ("sound/wave files/itemLost.wav"));

	// Set the image height and width based on the path image size
	IMAGE_WIDTH = blocks [1].getWidth (this);
	IMAGE_HEIGHT = blocks [1].getHeight (this);

	// Create Shape
	currentShape = new Shape (1, 6);
	nextShapePosRow = 3;
	nextShapePosCol = 19;

	nextShape = new Shape (nextShapePosRow, nextShapePosCol);
	currentRow = 1;
	currentColumn = 6;

	// Set game status
	isGameOver = false;
	gameOn = false;

	// Player config
	playerScore = 0;
	speedMultiplyer = 1;
	playerNameStr = "Default Player";

	// Shape Config
	shapeSpeed = 600;

	// Highscore Config
	highScoreRead ();

	// Set and Create all Menu Items and Menu Bar
	Menu gameMenu = new Menu ("Game");
	Menu newGameSubMenu = new Menu ("New Game");
	Menu optionsMenu = new Menu ("Options");
	Menu audiosettingsSubMenu = new Menu ("Audio Settings");
	Menu highScoresMenu = new Menu ("Highscores");
	Menu helpMenu = new Menu ("Help");

	track1 = new MenuItem ("Zanzibar");
	track2 = new MenuItem ("Popcorn");
	track3 = new MenuItem ("Sandstorm");
	track4 = new MenuItem ("Adagio for Strings");
	track5 = new MenuItem ("Summer is Calling");
	musicOff = new MenuItem ("Audio Off");

	// Menu Items in the Game menu
	normalModeOption = new MenuItem ("Classic Mode");
	normalModeOption.setShortcut (new MenuShortcut (KeyEvent.VK_N));

	timeAttackOption = new MenuItem ("Time Trial Mode");
	timeAttackOption.setShortcut (new MenuShortcut (KeyEvent.VK_T));

	missionModeOption = new MenuItem ("Mission Mode");
	missionModeOption.setShortcut (new MenuShortcut (KeyEvent.VK_M));

	quitCurrentGame = new MenuItem ("Quit Current Game");

	exitGameOption = new MenuItem ("Exit Game");
	exitGameOption.setShortcut (new MenuShortcut (KeyEvent.VK_E));

	// Menu Items in the Options menu
	playerName = new MenuItem ("Change Player Name");
	playerName.setShortcut (new MenuShortcut (KeyEvent.VK_C));

	// Menu Items in the Highscore menu
	normalModeHighScoreOption = new MenuItem ("Classic Mode");

	// Menu Items in the Help menu
	featuresGameOption = new MenuItem ("Game Features");

	helpGameOption = new MenuItem ("Help");
	helpGameOption.setShortcut (new MenuShortcut (KeyEvent.VK_H));

	tipsGameOption = new MenuItem ("Tips");

	creditsOption = new MenuItem ("About Tetris");

	// Add all menu items to their specified menus
	gameMenu.add (newGameSubMenu);
	newGameSubMenu.add (normalModeOption);
	newGameSubMenu.add (timeAttackOption);
	newGameSubMenu.add (missionModeOption);
	gameMenu.add (quitCurrentGame);
	gameMenu.addSeparator ();
	gameMenu.add (exitGameOption);

	optionsMenu.add (playerName);
	optionsMenu.add (audiosettingsSubMenu);
	audiosettingsSubMenu.add (track1);
	audiosettingsSubMenu.add (track2);
	audiosettingsSubMenu.add (track3);
	audiosettingsSubMenu.add (track4);
	audiosettingsSubMenu.add (track5);
	audiosettingsSubMenu.add (musicOff);

	highScoresMenu.add (normalModeHighScoreOption);

	helpMenu.add (featuresGameOption);
	helpMenu.add (helpGameOption);
	helpMenu.add (tipsGameOption);
	helpMenu.addSeparator ();
	helpMenu.add (creditsOption);

	MenuBar mainMenu = new MenuBar ();
	mainMenu.add (gameMenu);
	mainMenu.add (optionsMenu);
	mainMenu.add (highScoresMenu);
	mainMenu.add (helpMenu);
	setMenuBar (mainMenu);

	playerNameStr = "Default Player";
	quitCurrentGame.setEnabled (false);

	// Frame config
	setIconImage (new ImageIcon ("icon.gif").getImage ());
	setResizable (false);
	setLocation (230, 50);
	setVisible (true);                // Show the frame


	welcomeSound.play ();
	playerNameStr = JOptionPane.showInputDialog (this, "Please enter your name below:");

	while ((playerNameStr == null) || (playerNameStr.length () <= 0))
	{
	    playerNameStr = "Default Player";
	    JOptionPane.showMessageDialog (this, "There was no name entered! Please re-enter your name.",
		    "Alert!", JOptionPane.WARNING_MESSAGE);
	    playerNameStr = JOptionPane.showInputDialog (this, "Please enter your name below:");
	}

	backgroundTrack.loop ();

	// Block Timer config
	blockTime = 0;
	blockTimer = new Timer (shapeSpeed, new TimerEventHandler ());
	isGamePause = false;

	//Game Timer config
	gameTime = 0;
	gameTimer = new Timer (1000, new TimerEventHandler2 ());

	// Item Timer Config
	itemTimerOn = true;
	itemTimer = new Timer (100, new itemTimerHandler ());
	itemTime = 0;
	timeAllowed = 50;   //  5 seconds

	tipImageTimeOn = true;
	tipImageTimer = new Timer (100, new imageTimerHandler ());
	tipImageTime = 0;
	tipImageTimeAllowed = 100;
	tipImageTimer.start ();

	repaint ();

    } // Constructor


    /** Gets the URL needed for newAudioClip
    */
    public URL getCompleteURL (String fileName)
    {
	try
	{
	    return new URL ("file:" + System.getProperty ("user.dir") + "/" + fileName);
	}
	catch (MalformedURLException e)
	{
	    System.err.println (e.getMessage ());
	}
	return null;
    }


    /** Sets the actions taken when a menu item is clicked on
      * @param options The option or menu item variable name that is being
		       clicked on.
      * @param args
      * @return true if the action is valid and has been taken
    */
    public boolean action (Event option, Object args)
    {
	if (option.target == exitGameOption)
	{
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();


	    JOptionPane.showMessageDialog (this, "Thank you for playing Tetris!", "Thank You!",
		    JOptionPane.INFORMATION_MESSAGE);

	    hide ();
	    System.exit (0);
	}

	else if (option.target == quitCurrentGame)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    if (JOptionPane.showConfirmDialog (this, "Are you sure you want to quit your current game?",
			"Message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	    {
		clearGrid ();
		isGameOver = true;
		normalModeOption.setEnabled (true);
		timeAttackOption.setEnabled (true);
		missionModeOption.setEnabled (true);
		quitCurrentGame.setEnabled (false);
		preBlocks = 0;
	    }
	    else
	    {
		isGamePause = false;
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}

	else if (option.target == playerName)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    playerNameStr = JOptionPane.showInputDialog (this, "Please enter your name below:");

	    while ((playerNameStr == null) || (playerNameStr.length () <= 0))
	    {
		playerNameStr = "Default Player";
		JOptionPane.showMessageDialog (this, "There was no name entered! Please re-enter your name.",
			"Alert!", JOptionPane.WARNING_MESSAGE);
		playerNameStr = JOptionPane.showInputDialog (this, "Please enter your name below:");
	    }

	    isGamePause = false;
	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}
	else if (option.target == musicOff)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    backgroundTrack.stop ();
	    backgroundTrack2.stop ();
	    backgroundTrack3.stop ();
	    backgroundTrack4.stop ();
	    backgroundTrack5.stop ();

	    isGamePause = false;
	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}

	else if (option.target == track1)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    backgroundTrack5.stop ();
	    backgroundTrack2.stop ();
	    backgroundTrack3.stop ();
	    backgroundTrack4.stop ();
	    backgroundTrack.loop ();

	    isGamePause = false;
	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}

	else if (option.target == track2)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    backgroundTrack.stop ();
	    backgroundTrack5.stop ();
	    backgroundTrack3.stop ();
	    backgroundTrack4.stop ();
	    backgroundTrack2.loop ();

	    isGamePause = false;
	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}

	else if (option.target == track3)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    backgroundTrack.stop ();
	    backgroundTrack2.stop ();
	    backgroundTrack5.stop ();
	    backgroundTrack4.stop ();
	    backgroundTrack3.loop ();

	    isGamePause = false;
	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}

	else if (option.target == track4)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    backgroundTrack.stop ();
	    backgroundTrack2.stop ();
	    backgroundTrack3.stop ();
	    backgroundTrack5.stop ();
	    backgroundTrack4.loop ();

	    isGamePause = false;
	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}

	else if (option.target == track5)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    backgroundTrack.stop ();
	    backgroundTrack2.stop ();
	    backgroundTrack3.stop ();
	    backgroundTrack4.stop ();
	    backgroundTrack5.loop ();

	    isGamePause = false;
	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }

	    repaint ();
	}
	else if (option.target == normalModeOption)
	{

	    ImageIcon classic = new ImageIcon ("images/layout/classic.gif");

	    Object[] objects = {classic};

	    JOptionPane.showMessageDialog (this, objects, "Classic Mode", JOptionPane.INFORMATION_MESSAGE);

	    mapFile = "maps/normalModeGrid" + getPlayerChoice (0, 6, choice, "Number of Lines for Start", "Number of Lines", "Line(s)") + ".txt";

	    blockTimer.stop ();
	    int chosenSpeed = getPlayerChoice (1, 4, choice, "Starting Speed", "Speed", "Level");

	    if (chosenSpeed == 0)
		chosenSpeed = getPlayerChoice (1, 4, choice, "Starting Speed", "Speed", "Level");

	    if (chosenSpeed == 1)
		shapeSpeed = 600;
	    else if (chosenSpeed == 2)
		shapeSpeed = 500;
	    else if (chosenSpeed == 3)
		shapeSpeed = 400;
	    else if (chosenSpeed == 4)
		shapeSpeed = 300;

	    blockTimer = new Timer (shapeSpeed, new TimerEventHandler ());

	    restartGame ();
	    importGrid (mapFile);
	    preBlocks ();
	    gameTimer.start ();
	    blockTimer.start ();
	    itemTimer.start ();
	    blocksUsed = 0;
	    blocksUsed++;
	    playerScore += 5;

	    normalModeOption.setEnabled (false);
	    timeAttackOption.setEnabled (false);
	    missionModeOption.setEnabled (false);
	    quitCurrentGame.setEnabled (true);

	}
	else if (option.target == timeAttackOption)
	{

	    ImageIcon timeTrial = new ImageIcon ("images/layout/timeTrial.gif");

	    Object[] objects = {timeTrial};
	    JOptionPane.showMessageDialog (this, timeTrial,
		    "Time Trial Mode", JOptionPane.INFORMATION_MESSAGE);

	    timeAttackMode = true;
	    timeAttackTime = (getPlayerChoice (1, 5, choice, "Duration of Game", "Time to Play", "Level") * 2);

	    while (timeAttackTime == 0)
		timeAttackTime = (getPlayerChoice (1, 5, choice, "Duration of Game", "Time to Play", "Level") * 2);

	    restartGame ();
	    importGrid ("maps/normalModeGrid0.txt");
	    preBlocks ();
	    blockTimer.stop ();
	    shapeSpeed = 600;
	    blockTimer = new Timer (shapeSpeed, new TimerEventHandler ());
	    gameTimer.start ();
	    blockTimer.start ();
	    blocksUsed = 0;
	    blocksUsed++;
	    playerScore += 5;
	    timeAttackMode = true;

	    normalModeOption.setEnabled (false);
	    timeAttackOption.setEnabled (false);
	    missionModeOption.setEnabled (false);
	    quitCurrentGame.setEnabled (true);


	}

	else if (option.target == missionModeOption)
	{
	    ImageIcon mission = new ImageIcon ("images/layout/mission.gif");

	    Object[] objects = {mission};

	    JOptionPane.showMessageDialog (this, mission,
		    "Mission Mode", JOptionPane.INFORMATION_MESSAGE);

	    missionMode = true;
	    currentLevel = getPlayerChoice (1, 6, choice, "Number of Lines to Exclude", "Number of Lines", "Line(s)");

	    while (currentLevel == 0)
		currentLevel = getPlayerChoice (1, 6, choice, "Number of Lines to Finish", "Number of Lines", "Line(s)");

	    mapFile = "maps/normalModeGrid" + currentLevel + ".txt";

	    restartGame ();
	    importGrid (mapFile);
	    preBlocks ();
	    blockTimer.stop ();
	    playerScore = 0;
	    shapeSpeed = 600;
	    blockTimer = new Timer (shapeSpeed, new TimerEventHandler ());
	    gameTimer.start ();
	    blockTimer.start ();
	    blocksUsed = 0;
	    missonModeExcLines = 0;
	    blocksUsed++;
	    playerScore += 5;

	    normalModeOption.setEnabled (false);
	    timeAttackOption.setEnabled (false);
	    missionModeOption.setEnabled (false);
	    quitCurrentGame.setEnabled (true);


	}
	else if (option.target == normalModeHighScoreOption)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    // Display high scores..

	    Object[] objects = {
		"1.  " + topTenClassic [0] + " scored by " + topTenNameClassic
		[0] + " using " + topTenBlocksClassic [0] + " blocks within "
		+ topTenTimeClassic [0]
		, "2.  " + topTenClassic [1] + " scored by " + topTenNameClassic
		[1] + " using " + topTenBlocksClassic [1] + " blocks within "
		+ topTenTimeClassic [1]
		, "3.   " + topTenClassic [2] + " scored by " + topTenNameClassic
		[2] + " using " + topTenBlocksClassic [2] + " blocks within "
		+ topTenTimeClassic [2]
		, "4.   " + topTenClassic [3] + " scored by " + topTenNameClassic
		[3] + " using " + topTenBlocksClassic [3] + " blocks within "
		+ topTenTimeClassic [3]
		, "5.   " + topTenClassic [4] + " scored by " + topTenNameClassic
		[4] + " using " + topTenBlocksClassic [4] + " blocks within " +
		topTenTimeClassic [4]
		, "6.   " + topTenClassic [5] + " scored by " + topTenNameClassic
		[5] + " using " + topTenBlocksClassic [5] + " blocks within " +
		topTenTimeClassic [5]
		, "7.   " + topTenClassic [6] + " scored by " + topTenNameClassic [6]
		+ " using " + topTenBlocksClassic [6] + " blocks within " +
		topTenTimeClassic [6]
		, "8.   " + topTenClassic [7] + " scored by " + topTenNameClassic [7]
		+ " using " + topTenBlocksClassic [7] + " blocks within " +
		topTenTimeClassic [7]
		, "9.   " + topTenClassic [8] + " scored by " + topTenNameClassic [8]
		+ " using " + topTenBlocksClassic [8] + " blocks within " +
		topTenTimeClassic [8]
		, "10. " + topTenClassic [9] + " scored by " + topTenNameClassic [9]
		+ " using " + topTenBlocksClassic [9] + " blocks within " +
		topTenTimeClassic [9] };
	    JOptionPane.showMessageDialog (this, objects,
		    "Top Ten Highscores for Classic Mode", JOptionPane.INFORMATION_MESSAGE);

	    isGamePause = false;

	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }
	    repaint ();
	}
	else if (option.target == featuresGameOption)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    // Using arrays you can create a multiple object message
	    ImageIcon features = new ImageIcon ("images/layout/features.gif");

	    Object[] objects = {features};
	    JOptionPane.showMessageDialog (this, objects,
		    "Game Features", JOptionPane.INFORMATION_MESSAGE);

	    isGamePause = false;

	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }
	    repaint ();

	}
	else if (option.target == helpGameOption)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    // Using arrays you can create a multiple object message
	    ImageIcon help = new ImageIcon ("images/layout/help.gif");

	    Object[] objects = {help};
	    JOptionPane.showMessageDialog (this, objects,
		    "Help", JOptionPane.INFORMATION_MESSAGE);

	    isGamePause = false;

	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }
	    repaint ();

	}
	else if (option.target == tipsGameOption)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    // Using arrays you can create a multiple object message
	    ImageIcon tips = new ImageIcon ("images/layout/tips.gif");

	    Object[] objects = {tips};
	    JOptionPane.showMessageDialog (this, objects,
		    "Tips", JOptionPane.INFORMATION_MESSAGE);

	    isGamePause = false;

	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }
	    repaint ();

	}
	else if (option.target == creditsOption)
	{
	    isGamePause = true;
	    blockTimer.stop ();
	    gameTimer.stop ();
	    itemTimer.stop ();

	    // Using arrays you can create a multiple object message
	    ImageIcon credits = new ImageIcon ("images/layout/credits.gif");

	    Object[] objects = {credits};
	    JOptionPane.showMessageDialog (this, objects,
		    "About Tetris", JOptionPane.INFORMATION_MESSAGE);

	    isGamePause = false;

	    if (gameOn)
	    {
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
	    }
	    repaint ();

	}
	else
	    return false;
	return true;
    }


    /** Gets the player's choice when he/she selects a radio button from
	the game prompts
      * @param low is the lowest num of choices
      * @param high is the highest num of choices
      * @param choice is the choice picked
      * @param optionBoxType the type of option box selected
      * @param optionBoxTitle the title of the option box
      * @param choiceStr the choice string var
      * @return true if the action is valid and has been taken
    */
    public int getPlayerChoice (int low, int high, int choice, String optionBoxType, String optionBoxTitle, String choiceStr)
    {
	// Create a panel with radio buttons
	JPanel panel = new JPanel ();
	Border lowerEtched =
	    BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

	panel.setBorder (BorderFactory.createTitledBorder (
		    lowerEtched, optionBoxTitle));
	int noOfChoices = high - low + 1;
	int noOfColumns = (int) Math.sqrt (noOfChoices);
	panel.setLayout (new GridLayout (noOfChoices / noOfColumns, noOfColumns));

	// Create a group of radio buttons to add to the Panel
	ButtonGroup TypeGroup = new ButtonGroup ();
	JRadioButton[] buttonList = new JRadioButton [noOfChoices];

	// Create and add each radio button to the panel
	int selectedChoice = choice;
	for (int index = 0 ; index < buttonList.length ; index++)
	{
	    int playerChoice = index + low;
	    if ((playerChoice == selectedChoice) && (!timeAttackMode))
		buttonList [index] = new JRadioButton (playerChoice + " " + choiceStr, true);
	    else
		buttonList [index] = new JRadioButton (playerChoice + " " + choiceStr);

	    if ((playerChoice == selectedChoice) && (timeAttackMode))
		buttonList [index] = new JRadioButton (choiceStr + " " + playerChoice, true);
	    else
		buttonList [index] = new JRadioButton (choiceStr + " " + playerChoice);
	    TypeGroup.add (buttonList [index]);
	    panel.add (buttonList [index]);
	}

	// Show a dialog with the panel attached
	int playerChoice = JOptionPane.showConfirmDialog (this, panel,
		optionBoxType,
		JOptionPane.OK_CANCEL_OPTION,
		JOptionPane.DEFAULT_OPTION);

	while ((playerChoice == JOptionPane.CANCEL_OPTION))
	{

	    JOptionPane.showMessageDialog (this, "There was no option chosen!\n Please make a selection.",
		    "Alert!", JOptionPane.WARNING_MESSAGE);

	    playerChoice = JOptionPane.showConfirmDialog (this, panel,
		    optionBoxType,
		    JOptionPane.OK_CANCEL_OPTION,
		    JOptionPane.DEFAULT_OPTION);
	}

	if ((choice == JOptionPane.OK_OPTION))
	{
	    for (int index = 0 ; index < buttonList.length ; index++)
		if (buttonList [index].isSelected ())
		    selectedChoice = index + low;
	}
	return selectedChoice;


    }


    /** Looks for and keeps count of the number of preliminary blocks on a given field
      * @param grid The grid or game field that will be played on
    */
    public void preBlocks ()
    {
	for (int row = 0 ; row < grid.length - 1 ; row++)
	{
	    for (int col = 1 ; col < grid [0].length - 1 ; col++)
	    {
		if (grid [row] [col] > 0)
		{
		    preBlocks++;
		    playerScore += 6;
		}
	    }
	}
    }


    /** Resets all stats, timers and game status vars
    */
    public void resetStats ()
    {
	gameOn = false;
	clearGrid ();
	isGameOver = false;

	// Re-enable the mode buttons to clickable
	normalModeOption.setEnabled (true);
	timeAttackOption.setEnabled (true);
	missionModeOption.setEnabled (true);
	quitCurrentGame.setEnabled (false);

	// Reset all trackers and counting vars
	gameTime = 0;
	blockTime = 0;
	itemTime = 0;

	playerScore = 0;
	shapeSpeed = 600;
	blocksUsed = 0;
	preBlocks = 0;
	excludedLines = 0;
    }


    /** Restarts a game by setting everything back to a starting point
    */
    private void restartGame ()
    {
	clearGrid ();

	// Create and Drop a new shape
	currentShape = nextShape;
	currentShape.changePosition (1, 6);
	nextShape = new Shape (nextShapePosRow, nextShapePosCol);
	blocksUsed++;
	playerScore += 5;

	// Reset all timers
	gameTime = 0;
	blockTime = 0;
	playerScore = 0;

	gameOn = true;
	isGameOver = false;
	isGamePause = false;
    }


    /** Timer handler for blocks.  Keeps track of the movement of all falling
	blocks on the field and when to drop a new block
    */
    private class TimerEventHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    if (isGameOver)
		return;

	    // If the game isn't paused, run the game
	    if (!isGamePause)
	    {
		blockTimer.start ();

		// Finds any items placed on the field
		if (lookForItem ())
		{
		    // If no item found then display a new item
		    item = new Items ();
		    itemTimer.start ();
		    itemTime = 0;

		    // Add the new item to the field
		    item.addToGrid (grid);
		}

		// Checks if the item has been picked
		if (isItemPicked ())
		    removeItem ();


		// Constantly moves the current shape down one row
		if (currentShape.move (grid, 1, 0))
		{
		    blockTime++;
		}
		else // if the current moving item cannot move down any more
		{
		    // This means that the shape is set down and should
		    // be added to the grid
		    currentShape.addToGrid (grid);

		    while (isLineFull ()) // Once the shape is set, this means there is
		    { // an opportunity for a full line
			clearRow ();
			excludedLines++;

			if (missionMode)
			{
			    missonModeExcLines++;
			}

			excludeLineSound.play ();

			if (playerScore >= (1000 * speedMultiplyer))
			{
			    blockTimer.stop ();
			    shapeSpeed -= 50;
			    blockTimer = new Timer (shapeSpeed, new TimerEventHandler ());
			    speedMultiplyer++;
			    blockTimer.start ();
			}

			oldPlayerScore = playerScore;

			// Increase player's score depending on how fast the shapes
			// are dropping and how high his/her score is.  Player's score
			// increases more by how high his score is and how fast his
			// shapes are moving... The higher score and faster speed means more points!
			// This makes it fair for players that play at hard very fast paced
			// games and than those who play at slow speed- both players shouldn't get
			// same points..
			playerScore += (int) (10 * (oldPlayerScore / shapeSpeed));
		    }

		    if (!isGameOver)
		    {
			// If the game isn't over and the shape has been set on the board
			// then drop a new shape
			currentShape = nextShape;
			currentShape.changePosition (1, 6);
			nextShape = new Shape (nextShapePosRow, nextShapePosCol);
			blocksUsed++;
			playerScore += 5;
		    }

		    // If the shape has been set and a new
		    // shape has been dropped and can't be
		    // moved, then this means game is over
		    if (!currentShape.move (grid, 1, 0))
		    {
			// Stop all timers
			isGameOver = true;
			blockTimer.stop ();
			gameOverSound.play ();

			// Set the game mode menu items to clickable
			normalModeOption.setEnabled (true);
			timeAttackOption.setEnabled (true);
			missionModeOption.setEnabled (true);
			playerName.setEnabled (true);
		    }
		}
	    }
	    else // if the game is paused then stop the block timer
		blockTimer.stop ();

	    repaint ();
	}
    }


    /** Clears a full row
    */
    private void clearRow ()
    {
	for (int row = excludeRow ; row >= 2 ; row--)
	{
	    for (int col = 1 ; col < grid [0].length - 1 ; col++)
	    {
		grid [row] [col] = grid [row - 1] [col];
	    }
	}
	for (int col = 1 ; col < grid [0].length - 1 ; col++)
	{
	    grid [2] [col] = 0;
	}
    }



    /** Checks if a line/row has been filled with blocks
      * @return True if the a line is full
    */
    private boolean isLineFull ()
    {
	int blockCount = 0;
	for (int row = 0 ; row < grid.length - 1 ; row++)
	{
	    blockCount = 0;

	    for (int col = 1 ; col < grid [0].length - 1 ; col++)
	    {
		if (grid [row] [col] > 0)
		{
		    blockCount++;
		}

		if (blockCount == 15)
		{
		    excludeRow = row;
		    return true;
		}
	    }
	}

	return false;
    }


    /** The timer that keeps track of the game time in seconds
    */
    private class TimerEventHandler2 implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{

	    if (isGameOver) // if the game is over, then submit
		// player score to the top ten score table
		{
		    gameTimer.stop ();
		    submitToHighScore (playerScore);
		    highScoreWrite ();
		}

	    if ((timeAttackMode) && (gameTime == (timeAttackTime * 60)))
	    {
		clearGrid ();
		blockTimer.stop ();
		gameTimer.stop ();
		itemTimer.stop ();
		gameOn = false;

		JOptionPane.showMessageDialog (Tetris.this,
			"Time is Up", "Time Attack", JOptionPane.INFORMATION_MESSAGE);

		JOptionPane.showMessageDialog (Tetris.this,
			"Your score was: " + playerScore, "Time Attack", JOptionPane.INFORMATION_MESSAGE);

		quitCurrentGame.setEnabled (false);
	    }

	    else if ((missionMode) && (currentLevel < 6) && (missonModeExcLines >= currentLevel))
	    {
		blockTimer.stop ();
		gameTimer.stop ();
		itemTimer.stop ();
		JOptionPane.showMessageDialog (Tetris.this, "Level Up!", "Message",
			JOptionPane.INFORMATION_MESSAGE);

		shapeSpeed -= 100;
		blockTimer = new Timer (shapeSpeed, new TimerEventHandler ());
		blockTimer.start ();
		gameTimer.start ();
		itemTimer.start ();
		missonModeExcLines = 0;

		currentLevel++;
		mapFile = "maps/normalModeGrid" + currentLevel + ".txt";
		restartGame ();
		importGrid (mapFile);
	    }
	    else if ((missionMode) && (currentLevel == 6) && (excludedLines == currentLevel))
	    {
		clearGrid ();
		blockTimer.stop ();
		gameTimer.stop ();
		itemTimer.stop ();
		gameOn = false;

		JOptionPane.showMessageDialog (Tetris.this,
			"Congratulations, you have finished Mission Mode", "Mission Mode", JOptionPane.INFORMATION_MESSAGE);

		JOptionPane.showMessageDialog (Tetris.this, "Please select another mode to play again.", "Message",
			JOptionPane.INFORMATION_MESSAGE);

		quitCurrentGame.setEnabled (false);
	    }

	    gameTime++;
	    repaint ();
	}
    }



    /** The item timer that keeps track of the time
	an item is put and displayed on the game field
    */
    private class itemTimerHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{

	    // Time is up!
	    if (itemTime >= timeAllowed && itemTimerOn)
	    {
		removeItem ();
		itemTime = 0;
	    }
	    else
	    {
		// Increment the time (you could also count down)
		itemTime++;
	    }
	}
    }


    /** The game tips image timer that keeps track of how long
	each game tips image should stay up there and then
	be replaced by a randomly different image. (After 10 secs)
    */
    private class imageTimerHandler implements ActionListener
    {
	public void actionPerformed (ActionEvent event)
	{
	    // Time is up!
	    if (tipImageTime >= tipImageTimeAllowed && tipImageTimeOn)
	    {
		shouldReplace = true;
		tipImageTime = 0;
		generateTipImageNo = (int) (Math.random () * 9);
	    }
	    else
	    {
		tipImageTimer.start ();
		// Increment the time
		tipImageTime++;
	    }
	}
    }


    /** Imports the given grid from a given .txt file
      * @return name the name of the .txt file
    */
    public void importGrid (String name)
    {
	TextInputFile gridFile = new TextInputFile (name);

	int noOfRows = 1;
	String rowStr = gridFile.readLine ();
	int noOfColomns = rowStr.length ();

	while (!gridFile.eof ())
	{
	    gridFile.readLine ();
	    noOfRows++;
	}

	gridFile.close ();

	grid = new int [noOfRows] [noOfColomns];

	gridFile = new TextInputFile (name);
	for (int row = 0 ; row < grid.length ; row++)
	{
	    rowStr = gridFile.readLine ();
	    for (int col = 0 ; col < grid [0].length ; col++)
	    {
		grid [row] [col] = (int) (rowStr.charAt (col) - '0');
	    }
	}

	gridFile.close ();

	// Set the size of the frame according to the game field
	setSize ((noOfColomns + 10) * SQUARE_SIZE + 2 * BORDER - 10,
		(noOfRows - 3) * SQUARE_SIZE + TOP_OFFSET + BORDER + 85);
    }


    /** Searches the game field looking for an item
      * @return true if there is no item
    */
    private boolean lookForItem ()
    {
	for (int row = 0 ; row < grid.length ; row++)
	{
	    for (int col = 0 ; col < grid [row].length ; col++)
	    {
		if (grid [row] [col] > 9)
		{
		    return false;
		}
	    }
	}
	return true;
    }


    /** Checks if the item on the field has been picked
	by the player
      * @return true if the player has picked the item up
    */
    private boolean isItemPicked ()
    {
	for (int row = 0 ; row < grid.length ; row++)
	{
	    for (int col = 0 ; col < grid [row].length ; col++)
	    {
		if (grid [row] [col] > 9)
		{
		    if (currentShape.containsShape (grid, row, col))
		    {
			return true;
		    }
		}
	    }
	}
	return false;
    }


    /** Searches the game field replacing any items with 0's (nothing)
    */
    public void removeItem ()
    {
	for (int row = 0 ; row < grid.length ; row++)
	{
	    for (int col = 0 ; col < grid [row].length ; col++)
	    {
		if (grid [row] [col] > 9)
		{
		    // If the item was picked up then do the effect
		    // of the item
		    if (currentShape.containsShape (grid, row, col))
		    {
			itemEffects ();
			grid [row] [col] = 0;
			itemCount--;
		    }
		    else // Just remove it with no effect
			// This means the item didn't pick it up
			// and time of the item to be displayed is up (5 secs)
			{
			    itemLostSound.play ();
			}
		    grid [row] [col] = 0;
		    itemCount--;
		}
	    }
	}
    }


    /** The item effects of each item when its picked up
    */
    public void itemEffects ()
    {
	if (item.findCurrentItem () == 10)
	{
	    // Increases player score
	    increaseItemSound.play ();
	    playerScore += 75;
	}


	if (item.findCurrentItem () == 11)
	{
	    // Clears all blocks on a field
	    removeAllItemSound.play ();
	    clearAllBlocks ();
	}


	if (item.findCurrentItem () == 12)
	{
	    // decreases the player score
	    // only if the player score is greater than
	    // 149, this prevents negative scores

	    decreaseItemSound.play ();

	    if (playerScore > 100)
	    {
		playerScore -= 100;
	    }
	}


	if (item.findCurrentItem () == 13)
	{
	    // increase the shape dropping speed

	    decreaseItemSound.play ();
	    shapeSpeed += 50;
	}


	if (item.findCurrentItem () == 14)
	{
	    // removes the current shape and replaces it
	    // with the next shape
	    changeShapeItemSound.play ();
	    currentShape = nextShape;
	    currentShape.changePosition (1, 6);
	    nextShape = new Shape (nextShapePosRow, nextShapePosCol);
	    blocksUsed++;

	    playerScore += 5;
	}


	if (item.findCurrentItem () == 15)
	{
	    // Decreases the shape dropping speed
	    increaseItemSound.play ();

	    shapeSpeed -= 50;
	}
    }


    /** Clears all blocks on the game field
    */
    public void clearGrid ()
    {
	gameOn = false;
	for (int row = 0 ; row < grid.length - 1 ; row++)
	{
	    for (int col = 1 ; col < grid [0].length - 1 ; col++)
	    {
		grid [row] [col] = 0;
	    }
	}
    }


    /** The method is called when the item with the
	'Clear all blocks on field' effect is picked up by
	the player
    */
    public void clearAllBlocks ()
    {
	for (int row = 0 ; row < grid.length - 1 ; row++)
	{
	    for (int col = 1 ; col < grid [0].length - 1 ; col++)
	    {
		grid [row] [col] = 0;
	    }
	}
    }


    /** Takes the final score of the player's and checks
	if it is good enough to be set in the top ten highscores
      * @param playerScore the final score of the player before game over
    */
    public void submitToHighScore (int playerScore)
    {
	int[] newArrayScores = new int [topTenClassic.length + 1];
	String[] newArrayNames = new String [topTenNameClassic.length + 1];
	String[] newArrayTime = new String [topTenTimeClassic.length + 1];
	int[] newArrayBlocks = new int [topTenBlocksClassic.length + 1];

	//  The highscore keeps track of the top ten players' score, name, blocks used and
	// time used.
	System.arraycopy (topTenClassic, 0, newArrayScores, 0, topTenClassic.length);
	System.arraycopy (topTenNameClassic, 0, newArrayNames, 0, topTenNameClassic.length);
	System.arraycopy (topTenTimeClassic, 0, newArrayTime, 0, topTenTimeClassic.length);
	System.arraycopy (topTenBlocksClassic, 0, newArrayBlocks, 0, topTenBlocksClassic.length);

	int index = newArrayScores.length - 1;

	while (index - 1 >= 0 && newArrayScores [index - 1] < playerScore)
	{
	    newArrayScores [index] = newArrayScores [index - 1];
	    newArrayNames [index] = newArrayNames [index - 1];

	    newArrayTime [index] = newArrayTime [index - 1];
	    newArrayBlocks [index] = newArrayBlocks [index - 1];

	    index--;
	}


	newArrayScores [index] = playerScore;
	newArrayNames [index] = playerNameStr;
	newArrayTime [index] = totalPlayerTime;
	newArrayBlocks [index] = blocksUsed;

	topTenClassic = newArrayScores;
	topTenNameClassic = newArrayNames;
	topTenTimeClassic = newArrayTime;
	topTenBlocksClassic = newArrayBlocks;
    }


    /** Writes the top ten scores from the array to a .txt file and
	inputs them into the array
    */
    public void highScoreWrite ()
    {
	// Open the file
	TextOutputFile outFile = new TextOutputFile ("score/highScoreClassic.txt");

	// Write to the File
	outFile.println (topTenClassic [0]);
	outFile.println (topTenClassic [1]);
	outFile.println (topTenClassic [2]);
	outFile.println (topTenClassic [3]);
	outFile.println (topTenClassic [4]);
	outFile.println (topTenClassic [5]);
	outFile.println (topTenClassic [6]);
	outFile.println (topTenClassic [7]);
	outFile.println (topTenClassic [8]);
	outFile.println (topTenClassic [9]);

	outFile.println (topTenNameClassic [0]);
	outFile.println (topTenNameClassic [1]);
	outFile.println (topTenNameClassic [2]);
	outFile.println (topTenNameClassic [3]);
	outFile.println (topTenNameClassic [4]);
	outFile.println (topTenNameClassic [5]);
	outFile.println (topTenNameClassic [6]);
	outFile.println (topTenNameClassic [7]);
	outFile.println (topTenNameClassic [8]);
	outFile.println (topTenNameClassic [9]);

	// Closing File
	outFile.close ();

	TextOutputFile outFile2 = new TextOutputFile ("score/highScoreTimeClassic.txt");

	outFile2.println (topTenTimeClassic [0]);
	outFile2.println (topTenTimeClassic [1]);
	outFile2.println (topTenTimeClassic [2]);
	outFile2.println (topTenTimeClassic [3]);
	outFile2.println (topTenTimeClassic [4]);
	outFile2.println (topTenTimeClassic [5]);
	outFile2.println (topTenTimeClassic [6]);
	outFile2.println (topTenTimeClassic [7]);
	outFile2.println (topTenTimeClassic [8]);
	outFile2.println (topTenTimeClassic [9]);

	outFile2.println (topTenBlocksClassic [0]);
	outFile2.println (topTenBlocksClassic [1]);
	outFile2.println (topTenBlocksClassic [2]);
	outFile2.println (topTenBlocksClassic [3]);
	outFile2.println (topTenBlocksClassic [4]);
	outFile2.println (topTenBlocksClassic [5]);
	outFile2.println (topTenBlocksClassic [6]);
	outFile2.println (topTenBlocksClassic [7]);
	outFile2.println (topTenBlocksClassic [8]);
	outFile2.println (topTenBlocksClassic [9]);

	// Closing File
	outFile2.close ();

    }


    /** Reads the top ten scores from a .txt file and inputs them
	in the array
    */
    public void highScoreRead ()
    {
	// Open the file
	TextInputFile inFile = new TextInputFile ("score/highScoreClassic.txt");

	// Read the file
	topTenClassic [0] = inFile.readInt ();
	topTenClassic [1] = inFile.readInt ();
	topTenClassic [2] = inFile.readInt ();
	topTenClassic [3] = inFile.readInt ();
	topTenClassic [4] = inFile.readInt ();
	topTenClassic [5] = inFile.readInt ();
	topTenClassic [6] = inFile.readInt ();
	topTenClassic [7] = inFile.readInt ();
	topTenClassic [8] = inFile.readInt ();
	topTenClassic [9] = inFile.readInt ();

	topTenNameClassic [0] = inFile.readLine ();
	topTenNameClassic [1] = inFile.readLine ();
	topTenNameClassic [2] = inFile.readLine ();
	topTenNameClassic [3] = inFile.readLine ();
	topTenNameClassic [4] = inFile.readLine ();
	topTenNameClassic [5] = inFile.readLine ();
	topTenNameClassic [6] = inFile.readLine ();
	topTenNameClassic [7] = inFile.readLine ();
	topTenNameClassic [8] = inFile.readLine ();
	topTenNameClassic [9] = inFile.readLine ();

	inFile.close ();

	TextInputFile inFile2 = new TextInputFile ("score/highScoreTimeClassic.txt");

	topTenTimeClassic [0] = inFile2.readLine ();
	topTenTimeClassic [1] = inFile2.readLine ();
	topTenTimeClassic [2] = inFile2.readLine ();
	topTenTimeClassic [3] = inFile2.readLine ();
	topTenTimeClassic [4] = inFile2.readLine ();
	topTenTimeClassic [5] = inFile2.readLine ();
	topTenTimeClassic [6] = inFile2.readLine ();
	topTenTimeClassic [7] = inFile2.readLine ();
	topTenTimeClassic [8] = inFile2.readLine ();
	topTenTimeClassic [9] = inFile2.readLine ();

	topTenBlocksClassic [0] = inFile2.readInt ();
	topTenBlocksClassic [1] = inFile2.readInt ();
	topTenBlocksClassic [2] = inFile2.readInt ();
	topTenBlocksClassic [3] = inFile2.readInt ();
	topTenBlocksClassic [4] = inFile2.readInt ();
	topTenBlocksClassic [5] = inFile2.readInt ();
	topTenBlocksClassic [6] = inFile2.readInt ();
	topTenBlocksClassic [7] = inFile2.readInt ();
	topTenBlocksClassic [8] = inFile2.readInt ();
	topTenBlocksClassic [9] = inFile2.readInt ();

	inFile2.close ();
    }


    /** Displays the time and takes care of conversions
      * @param time the current game time in seconds given by
		    the gameTimer handler
      * @return A string variable that displays the time in minutes
		and seconds.
    */
    public String timeDisplay (int time)
    {
	int minutes = 0;
	int seconds = time;
	String timeD = "";

	while (seconds > 59)
	{
	    seconds -= 60;
	    minutes++;
	}


	if ((minutes < 10) && (seconds < 10))
	{
	    timeD = ("0" + minutes + ":0" + seconds);
	}


	else if (seconds < 10)
	{
	    timeD = ("" + minutes + ":0" + seconds);
	}


	else if (minutes < 10)
	{
	    timeD = ("0" + minutes + ":" + seconds);
	}


	return timeD;
    }


    /** Responds to the specified keyboard keys
      * @param event keeps track of event modifiers for this key down event
      * @param key   the key that was pressed
      * @return      true if the key down event was handled, false otherwise
      */
    public boolean keyDown (Event evt, int key)
    {
	if (gameOn)
	{
	    if (key == Event.DOWN)
	    {
		if (!isGamePause)
		{
		    if (currentShape.move (grid, 1, 0))
		    {
			if (isItemPicked ())
			    removeItem ();
			playerScore += 1;

			if (playerScore >= (1000 * speedMultiplyer))
			{
			    blockTimer.stop ();
			    shapeSpeed -= 50;
			    blockTimer = new Timer (shapeSpeed, new TimerEventHandler ());
			    speedMultiplyer++;
			    blockTimer.start ();
			}
		    }
		}
		repaint ();
	    }
	    else if (key == Event.RIGHT)
	    {
		if (!isGamePause)
		{
		    if (!isGameOver)
			if (currentShape.move (grid, 0, 1))
			{
			    if (isItemPicked ())
				removeItem ();
			}
		}
	    }

	    else if (key == Event.LEFT)
	    {
		if (!isGamePause)
		{
		    if (!isGameOver)
			if (currentShape.move (grid, 0, -1))
			{
			    if (isItemPicked ())
				removeItem ();
			}
		}
	    }

	    else if (key == 'a' || key == 'A')
	    {
		if (!isGamePause)
		{
		    if (!isGameOver)
		    {
			rotateSound.play ();
			currentShape.rotateRightwards (grid);

			if (isItemPicked ())
			{
			    removeItem ();
			}

		    }
		}
	    }

	    else if (key == 's' || key == 'S')
	    {
		if (!isGamePause)
		{
		    if (!isGameOver)
		    {
			rotateSound.play ();
			currentShape.rotateLeftwards (grid);

			if (isItemPicked ())
			{
			    removeItem ();
			}

		    }
		}
	    }

	    else if (key == Event.ESCAPE)
	    {
		if (!isGameOver)
		{
		    if (isGamePause)
		    {
			isGamePause = false;
			blockTimer.start ();
			gameTimer.start ();
			itemTimer.start ();
			pauseSound.play ();
		    }
		    else
		    {
			isGamePause = true;
			blockTimer.stop ();
			gameTimer.stop ();
			itemTimer.stop ();
			pauseSound.play ();
		    }
		}

	    }
	}


	else

	    return false;
	repaint ();
	return true;
    }


    /** The paint method that deals with draw all images
      * @param g Graphics
      */
    public void paint (Graphics g)
    {
	if (offScreenBuffer == null) // Fliker
	{
	    offScreenImage = createImage (size ().width, size ().height);
	    offScreenBuffer = offScreenImage.getGraphics ();
	}


	// Get Layout Images
	offScreenBuffer.drawImage (guiImages [2], 2, 48, null); // Back Layout
	offScreenBuffer.drawImage (guiImages [0], 354, 94, null); // Next Shape Image
	offScreenBuffer.drawImage (guiImages [1], 354, 235, null); // Game Status Image
	//offScreenBuffer.drawImage (guiImages [3], 354, 445, null); // Game Tips Image

	offScreenBuffer.setFont (gameStatsFont);
	FontMetrics myFontMetrics = Toolkit.getDefaultToolkit ().getFontMetrics (offScreenBuffer.getFont ());

	Color fieldBG = new Color (254, 201, 40);
	Color fieldBorder = new Color (253, 185, 19);

	for (int row = 1 ; row <= NO_OF_ROWS ; row++)
	{
	    for (int col = 1 ; col <= NO_OF_COLUMNS ; col++)
	    {
		int xPos = (col) * SQUARE_SIZE + BORDER + 15;
		int yPos = TOP_OFFSET + row * SQUARE_SIZE - 6;

		// Draw the squares
		offScreenBuffer.setColor (fieldBG);
		offScreenBuffer.fillRect (xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);

		offScreenBuffer.setColor (fieldBorder);
		offScreenBuffer.drawRect (xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);

		int imageNo = grid [row] [col];
		if (imageNo > 0)
		{
		    offScreenBuffer.drawImage (blocks [imageNo],
			    (col) * 20 + 21,
			    76 + (row) * 20, 20, 20, null);
		}
	    }
	}



	// Redraw the grid with current images
	if (gameOn)
	{
	    currentShape.draw (offScreenBuffer, blocks);
	}

	// Get Game Status

	if (!gameOn)
	    gameStatus = "Welcome!";
	else if (!isGamePause)
	    gameStatus = "Running...";
	else
	    gameStatus = "Paused";
	if (isGameOver)
	    gameStatus = "Game Over";

	// display a new game tips image every 10 secs..
	if (shouldReplace == true)
	{
	    offScreenBuffer.drawImage (tipImages [generateTipImageNo + 1], 354, 445, null);
	}

	// Display Side info, eg. Score, Game Time...
	if (gameOn)
	{
	    Color yelloText = new Color (254, 102, 0);
	    offScreenBuffer.setColor (yelloText);

	    nextShape.draw (offScreenBuffer, blocks);

	    totalPlayerTime = timeDisplay (gameTime);
	    offScreenBuffer.drawString ("Player: " + playerNameStr, 365, 270);
	    offScreenBuffer.drawString ("Time: " + timeDisplay (gameTime), 365, 290);
	    offScreenBuffer.drawString ("Score: " + playerScore, 365, 310);
	    offScreenBuffer.drawString ("Block Speed: " + shapeSpeed, 365, 330);
	    offScreenBuffer.drawString ("Blocks Used: " + blocksUsed, 365, 350);
	    offScreenBuffer.drawString ("P. Blocks: " + preBlocks, 365, 370);
	    offScreenBuffer.drawString ("Lines Excluded: " + excludedLines, 365, 390);
	    offScreenBuffer.drawString ("Game Status: " + gameStatus, 365, 410);

	}
	else if (!gameOn)
	{
	    Color liteYelloText = new Color (254, 78, 0);
	    offScreenBuffer.setColor (liteYelloText);

	    offScreenBuffer.drawString ("Player: " + playerNameStr, 365, 270);
	    offScreenBuffer.drawString ("Time: 00:00", 365, 290);
	    offScreenBuffer.drawString ("Score:  0", 365, 310);
	    offScreenBuffer.drawString ("Block Speed:  0", 365, 330);
	    offScreenBuffer.drawString ("Blocks Used:  0", 365, 350);
	    offScreenBuffer.drawString ("P. Blocks: 0", 365, 370);
	    offScreenBuffer.drawString ("Lines Excluded: 0", 365, 390);
	    offScreenBuffer.drawString ("Game Status: " + gameStatus, 365, 410);
	    offScreenBuffer.drawImage (tipImages [0], 354, 445, null);
	}


	g.drawImage (offScreenImage, 0, 0, this);

    } // paint method


    // We need to override update when using the offScreenBuffer
    // To prevent the automatic clearing of the screen
    public void update (Graphics g)
    {
	paint (g);
    }


    // Handles the close button on the window
    public boolean handleEvent (Event evt)
    {
	if (evt.id == Event.WINDOW_DESTROY)
	{
	    hide ();
	    System.exit (0);
	    return true;
	}


	// If not handled, pass the event along
	return super.handleEvent (evt);
    }


    public static void main (String[] args)
    {
	new Tetris ();       // Create a ShapeTest frame
    } // main method
} // Tetris class
//this
//is
//year
//2008
