<h2>CBL Project 2026, Programming Course</h2>

A simple game in <strong>Java</strong>

<h3>Building and running the project</h3>

The project is done completely in Java and does not require any additional libraries apart from the pre-installed JSwing and AWT.
The program should be started by running the <code>CBL.java</code> , which implements the <code>main()</code> method in which the framerate is set when creating the GameEngine class and the only public method of the Engine.GameEngine - <code>run()</code> - is called.

The testing was done in NetBeans, since the source directory is a Maven project initially developed and compiled in NetBeans - running it in a different IDE might require a different version of Java.

<h3>Project structure</h3>
<h4><code>src/main/java/</code></h4>
<ul>
  <li><strong><code>CBL.java</code></strong> - main computational file, creates a GameEngine object and runs it</li>
  <li><strong>engine</strong>
    <ul>
      <li><strong><code>GameEngine.java</code></strong> - containing and executing the main run cycle of the program</li>
      <li><strong><code>Sprite.java</code></strong> - class for drawing a certain image sprite in a certain place (with the drawing methods in it since 0.4.2)</li>
    </ul>
  </li>
  <li><strong>entities</strong>
    <ul>
      <li><strong><code>Character.java</code></strong> - OOP class for the character</li>
      <li><strong><code>Weapon.java</code></strong> - OOP class for the weapon</li>
      <li><strong><code>Enemy.java</code></strong> - OOP class for the enemy</li>
      <li><strong><code>Bullet.java</code></strong> - OOP basic class for bullet types</li>
      <li><strong><code>MovingBullet.java</code></strong> - extending the Bullet class including movement mechanics</li>
      <li><strong><code>Upgrade.java</code></strong> - a class for the shopping options</li>
      <li><strong><code>UpgradeType.java</code></strong> - enumerated class for adding new upgrade types with specific features</li>
      <li><strong><code>Rarity.java</code></strong> - a class for different rarities used for classifying the upgrades</li>
    </ul>
  </li>
  <li><strong>windows</strong>
    <ul>
      <li><strong><code>GameWindow.java</code></strong> - basic JFrame window for the game</li>
    </ul>
  </li>
</ul>
<h4>src/main/resources/images</h4> - sprites for the character, the weapon, the bullets, the enemies, the background etc.
<h3>Run cycle logic</h3>
When the game is started, an infinite cycle of updating and drawing elements based on given framerate begins.
<h4>Run cycle guidelines</h4>
<ul>
  <li><strong>Receiving input</strong> - from the keyboard, the mouse</li>
  <li><strong>Updating</strong> - OOP-based updating of the all objects on the screen</li>
  <li><strong>Drawing</strong> - <b>ONLY</b> properly drawing the sprites of the existing elements, based on their number-given properties</li>
</ul>
