<h2>CBL Project 2026, Programming Course</h2>

A simple game in <strong>Java</strong>

<h3>Building and running the project</h3>

The project is done completely in Java and does not require any additional libraries apart from the pre-installed JSwing and AWT.
The program should be started from the <code>CBL.java</code> file implementing the main() method in which the framerate is set when creating the GameEngine class and the only public method of the Engine.GameEngine - run() - is called.

<h3>Project structure</h3>
<h4>src/main/java/</h4>
<ul>
  <li><strong>CBL.java</strong> - main computational file, creates a GameEngine object and runs it</li>
  <li><strong>engine</strong>
    <ul>
      <li><strong>GameEngine.java</strong> - containing and executing the main run cycle of the program</li>
      <li><strong>Sprite.java</strong> - class for drawing a certain image sprite in a certain place (with the drawing methods in it since 0.4.2)</li>
    </ul>
  </li>
  <li><strong>entities</strong>
    <ul>
      <li><strong>Character.java</strong> - OOP class for the character</li>
      <li><strong>Weapon.java</strong> - OOP class for the weapon</li>
      <li><strong>Enemy.java</strong> - OOP class for the enemy</li>
      <li><strong>Bullet.java</strong> - OOP basic class for bullet types</li>
      <li><strong>MovingBullet.java</strong> - extending the Bullet class including movement mechanics</li>
      <li><strong>Upgrade.java</strong> - a class for the shopping options</li>
      <li><strong>UpgradeType.java</strong> - enumerated class for adding new upgrade types with specific features</li>
      <li><strong>Rarity.java</strong> - a class for different rarities used for classifying the upgrades</li
    </ul>
  </li>
  <li><strong>windows</strong>
    <ul>
      <li><strong>GameWindow.java</strong> - basic JFrame window for the game</li>
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
