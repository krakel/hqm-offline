package de.doerl.hqm.ui;

import java.awt.Window;

import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JFXHiddenApplication extends Application {
	private static Stage sPrimaryStage;
	private static boolean sIsEnabled = false;

	static void closeApplication() {
		if (sIsEnabled) {
			Platform.exit();
		}
	}

	private static void fakeModalDialog( Stage fxDialog, Window parent) {
		fxDialog.setOnShown( event -> {
			SwingUtilities.invokeLater( () -> {
				parent.setEnabled( false);
//				if (parent.getJMenuBar() != null) {
//					parent.getJMenuBar().setEnabled( false);
//				}
			});
		});
		fxDialog.setOnHidden( event -> {
			SwingUtilities.invokeLater( () -> {
				parent.setEnabled( true);
//				if (parent.getJMenuBar() != null) {
//					parent.getJMenuBar().setEnabled( true);
//				}
				parent.toFront();
			});
		});
	}

	public static Stage getPrimaryStage() {
		return sPrimaryStage;
	}

	public static boolean isIsEnabled() {
		return sIsEnabled;
	}

	static void launchApplication() {
		if (sIsEnabled) {
			new Thread( JFXHiddenApplication::launch).start();
		}
	}

	public static void showJavaFXDialog( Stage fxDialog, Window parent) {
		fxDialog.setOpacity( 0d);
		fxDialog.show();
		fxDialog.hide();
		fxDialog.setOpacity( 1d);
		fxDialog.setX( parent.getBounds().getCenterX() - fxDialog.getWidth() / 2);
		fxDialog.setY( parent.getBounds().getCenterY() - fxDialog.getHeight() / 2);
		fakeModalDialog( fxDialog, parent);
		fxDialog.setAlwaysOnTop( true);
		fxDialog.showAndWait();
	}

	@Override
	public void start( Stage primaryStage) throws Exception {
		JFXHiddenApplication.sPrimaryStage = primaryStage;
		primaryStage.initStyle( StageStyle.TRANSPARENT);
		primaryStage.setScene( new Scene( new Pane(), 1, 1));
		primaryStage.show();
	}
}
