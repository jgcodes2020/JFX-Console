package jgcodes.jfxconsole;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jgcodes.jfxconsole.io.ConsoleWriter;

import java.io.PrintWriter;

public class Launcher extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    Console console = new Console();
    PrintWriter consoleOut = new PrintWriter(new ConsoleWriter(console));

    StackPane root = new StackPane();
    console.setMaxHeight(Double.MAX_VALUE);
    console.setMaxWidth(Double.MAX_VALUE);

    console.prefWidthProperty().bind(root.widthProperty());
    console.prefHeightProperty().bind(root.heightProperty());

    root.getChildren().add(console);

    stage.setScene(new Scene(root, 300, 300));
    stage.show();

    consoleOut.println("Hello world");
  }
}
