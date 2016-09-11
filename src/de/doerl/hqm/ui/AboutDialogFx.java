package de.doerl.hqm.ui;

import java.awt.Window;

import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.VersionHelper;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class AboutDialogFx extends ADialogFx {
	private AboutDialogFx( Stage owner) {
		super( owner);
		setTheme( "about.theme");
//		setResizable( false);
		addAction( BTN_OK);
		addEscapeAction();
	}

	public static void update( Stage owner) {
		AboutDialogFx dlg = new AboutDialogFx( owner);
		dlg.createMain();
		dlg.showDialog();
	}

	public static void update( Window parent) {
		Platform.runLater( () -> {
			AboutDialogFx dlg = new AboutDialogFx( JFXHiddenApplication.getPrimaryStage());
			dlg.createMain();
			dlg.showDialog();
			JFXHiddenApplication.showJavaFXDialog( dlg, parent);
		});
	}

	private Node createContent() {
		HBox hori = new HBox( GAP);
		hori.getChildren().add( new ImageView( ResourceManager.getImageFx( "Krakel1.png")));
		Text msg = new Text( ResourceManager.getString( "config.about") + VersionHelper.VERSION);
//		msg.setWrappingWidth( 250);
		hori.getChildren().add( msg);
		return msg;
	}

	@Override
	protected void createMain() {
		mMain.getChildren().add( createContent());
	}
}
