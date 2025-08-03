jarsigner -keystore newkey -storepass boardcad dist\BoardCAD.jar boardcad
rmdir /s /q "release"
jpackage ^
  --name BoardCAD ^
  --app-version "3.2.1" ^
  --vendor "BoardCAD Team" ^
  --input dist ^
  --dest release ^
  --main-jar BoardCAD.jar ^
  --main-class boardcad.gui.jdk.BoardCAD ^
  --java-options --add-opens ^
  --java-options java.base/java.lang=ALL-UNNAMED ^
  --java-options --add-opens ^
  --java-options java.desktop/sun.java2d=ALL-UNNAMED ^
  --java-options --add-opens ^
  --java-options java.desktop/sun.awt=ALL-UNNAMED ^
  --java-options --add-opens ^
  --java-options java.desktop/sun.awt.windows=ALL-UNNAMED ^
  --java-options -Dsun.awt.exception.handler=com.example.MyExceptionLogger ^
  --type msi ^
  --icon "src/boardcad/icons/BC 32x32.ico" ^
  --win-menu ^
  --win-shortcut ^
  --verbose
 