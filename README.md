<h2>CBL Project 2026, Programming Course</h2>

A simple game in <strong>Java</strong>
<h3>Project structure</h3>
<h4>src/main/java/</h4>
<ul>
  <li><strong>CBL.java</strong> - main computational file, creates a GameEngine object and runs it</li>
  <li><strong>engine</strong>
    <ul>
      <li><strong>GameEngine.java</strong> - containing and executing the main run cycle of the program</li>
      <li><strong>Sprite.java</strong> - class for drawing a certain image sprite in a certain place</li>
    </ul>
  </li>
  <li><strong>entities</strong>
    <ul>
      <li><strong>Character.java</strong> - OOP class for the character</li>
      <li><strong>Weapon.java</strong> - OOP class for the weapon</li>
      <li><strong>Enemy.java</strong> - OOP class for the enemy</li>
      <li><strong>Bullet.java</strong> - OOP basic class for bullet types</li>
      <li><strong>MovingBullet.java</strong> - extending the Bullet class including movement mechanics</li>
      <li><strong>SpriteEntity.java</strong> - adds the functionality of storing an image path and retrieving it for the all OOP classes</li>
    </ul>
  </li>
  <li><strong>windows</strong>
    <ul>
      <li><strong>GameWindow.java</strong> - basic JFrame window for the game</li>
    </ul>
  </li>
</ul>
<h4>src/main/resources/images</h4> - sprites for the character, the weapon, the bullets, the enemies
<h3>Run cycle logic</h3>
When the game is started, an infinite cycle of updating and drawing elements based on given framerate begins.
<h4>Run cycle guidelines</h4>
<ul>
  <li><strong>Updating</strong> - OOP-based updating of the all objects on the screen</li>
  <li><strong>Drawing</strong> - <b>ONLY</b> properly drawing the sprites of the existing elements, based on their number-given properties</li>
</ul>
